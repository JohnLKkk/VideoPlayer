//
// Created by Void on 2020/11/30.
//

#include "NativePlayer.h"

int isRelease;

int NativePlayer::init_filters(const char *filters_descr) {
    char args[512];
    int ret;
    AVFilter *buffersrc = avfilter_get_by_name("buffer");
    AVFilter *buffersink = avfilter_get_by_name("buffersink");
    AVFilterInOut *outputs = avfilter_inout_alloc();
    AVFilterInOut *inputs = avfilter_inout_alloc();
    AVRational time_base = pFormatCtx->streams[videoIndex]->time_base;
    enum AVPixelFormat pix_fmts[] = {AV_PIX_FMT_YUV420P, AV_PIX_FMT_NONE};

    filter_graph = avfilter_graph_alloc();
    if (!outputs || !inputs || !filter_graph) {
        ret = AVERROR(ENOMEM);
        goto end;
    }

    /* buffer video source: the decoded frames from the decoder will be inserted here. */
    snprintf(args, sizeof(args),
             "video_size=%dx%d:pix_fmt=%d:time_base=%d/%d:pixel_aspect=%d/%d",
             vCodecCtx->width, vCodecCtx->height, vCodecCtx->pix_fmt,
             time_base.num, time_base.den,
             vCodecCtx->sample_aspect_ratio.num, vCodecCtx->sample_aspect_ratio.den);

    ret = avfilter_graph_create_filter(&buffersrc_ctx, buffersrc, "in",
                                       args, nullptr, filter_graph);
    if (ret < 0) {
        LOGE("Cannot create buffer source");
        goto end;
    }

    /* buffer video sink: to terminate the filter chain. */
    ret = avfilter_graph_create_filter(&buffersink_ctx, buffersink, "out",
                                       nullptr, nullptr, filter_graph);
    if (ret < 0) {
        LOGE("Cannot create buffer sink");
        goto end;
    }

    ret = av_opt_set_int_list(buffersink_ctx, "pix_fmts", pix_fmts,
                              AV_PIX_FMT_NONE, AV_OPT_SEARCH_CHILDREN);
    if (ret < 0) {
        LOGE("Cannot set output pixel format");
        goto end;
    }

    outputs->name = av_strdup("in");
    outputs->filter_ctx = buffersrc_ctx;
    outputs->pad_idx = 0;
    outputs->next = nullptr;

    inputs->name = av_strdup("out");
    inputs->filter_ctx = buffersink_ctx;
    inputs->pad_idx = 0;
    inputs->next = nullptr;

    if ((ret = avfilter_graph_parse_ptr(filter_graph, filters_descr,
                                        &inputs, &outputs, nullptr)) < 0)
        goto end;

    if ((ret = avfilter_graph_config(filter_graph, nullptr)) < 0)
        goto end;

    end:
    avfilter_inout_free(&inputs);
    avfilter_inout_free(&outputs);

    return ret;
}

int NativePlayer::playVideo(const char *vPath, ANativeWindow *nativeWindow) {
    isRelease = 0;
    //初始化所有组件
    av_register_all();
    //分配一个AVFormatContext结构
    pFormatCtx = avformat_alloc_context();
    //打开文件
    if (avformat_open_input(&pFormatCtx, vPath, nullptr, nullptr) != 0) {
        LOGE("Could not open input stream");
        goto end_line;
    }
    //3.查找文件的流信息
    if (avformat_find_stream_info(pFormatCtx, nullptr) < 0) {
        LOGE("Could not find stream information");
        goto end_line;
    }
    //4.查找视频轨(视频数据类型)
    for (int index = 0; index < pFormatCtx->nb_streams; index++) {
        if (pFormatCtx->streams[index]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            videoIndex = index;
            break;
        }
    }
    if (videoIndex == -1) {
        LOGE("Could not find a video stream");
        goto end_line;
    }
    //5.查找解码器
    vCodec = avcodec_find_decoder(pFormatCtx->streams[videoIndex]->codecpar->codec_id);
    if (vCodec == nullptr) {
        LOGE("could not find codec");
        goto end_line;
    }
    //6.配置解码器
    vCodecCtx = avcodec_alloc_context3(vCodec);
    avcodec_parameters_to_context(vCodecCtx, pFormatCtx->streams[videoIndex]->codecpar);


    //7.打开解码器
    if (avcodec_open2(vCodecCtx, vCodec, nullptr) < 0) {
        LOGE("Could not open codec");
        goto end_line;
    }
    width = vCodecCtx->width;
    height = vCodecCtx->height;
    //注册过滤器
    avfilter_register_all();
    filter_frame = av_frame_alloc();
    if (filter_frame == nullptr) {
        LOGE("Couldn't allocate filter frame.");
        goto end_line;
    }
    //分配一个帧指针，指向解码后的原始帧
    vFrame = av_frame_alloc();
    if (vFrame == nullptr) {
        LOGE("无法分配过滤器框架");
        goto end_line;
    }
    vPacket = (AVPacket *) av_malloc(sizeof(AVPacket));
    pFrameRGBA = av_frame_alloc();
    //绑定输出buffer
    bufferSize = av_image_get_buffer_size(AV_PIX_FMT_RGBA, width, height, 1);
    out_buffer = (uint8_t *) av_malloc(bufferSize * sizeof(uint8_t));
    av_image_fill_arrays(pFrameRGBA->data, pFrameRGBA->linesize, out_buffer, AV_PIX_FMT_RGBA,
                         width, height, 1);
    //创建SwsContext用于缩放、转换操作
    sws_ctx = sws_getContext(width, height, vCodecCtx->pix_fmt,
                             width, height, AV_PIX_FMT_RGBA, SWS_BILINEAR, nullptr, nullptr,
                             nullptr);
    //更改窗口缓冲区的格式和大小。
    if (ANativeWindow_setBuffersGeometry(nativeWindow, width, height, WINDOW_FORMAT_RGBA_8888) <
        0) {
        LOGE("Could not set buffers geometry");
        ANativeWindow_release(nativeWindow);
        goto end_line;
    }
    int ret;
    ret = init_filters("drawgrid=w=iw/3:h=ih/3:t=2:c=white@0.5");
    if (ret < 0) {
        LOGE("init_filter error, ret=%d\n", ret);
        goto end_line;
    }
    //读取帧
    while (av_read_frame(pFormatCtx, vPacket) >= 0) {
        if (isRelease)break;
        if (vPacket->stream_index != videoIndex) continue;
        //视频解码
        if (avcodec_send_packet(vCodecCtx, vPacket) != 0) break;
        //从解码器接收返回的帧数据
        while (avcodec_receive_frame(vCodecCtx, vFrame) == 0) {
            if (isRelease)break;
            if (av_buffersrc_add_frame_flags(buffersrc_ctx, vFrame, AV_BUFFERSRC_FLAG_KEEP_REF) <
                0) {
                LOGE("Error while feeding the filter_graph");
                break;
            }
            av_buffersink_get_frame(buffersink_ctx, filter_frame);
            //转化格式
            sws_scale(sws_ctx, (const uint8_t *const *) filter_frame->data, filter_frame->linesize,
                      0,
                      vCodecCtx->height,
                      pFrameRGBA->data, pFrameRGBA->linesize);
            if (ANativeWindow_lock(nativeWindow, &windowBuffer, nullptr) >= 0) {
                //逐行复制
                auto *bufferBits = (uint8_t *) windowBuffer.bits;
                for (int h = 0; h < height; h++) {
                    memcpy(bufferBits + h * windowBuffer.stride * 4,
                           out_buffer + h * pFrameRGBA->linesize[0],
                           pFrameRGBA->linesize[0]);
                }
                ANativeWindow_unlockAndPost(nativeWindow);
            } else {
                LOGE("cannot lock window");
            }
        }
        av_frame_unref(filter_frame);
        av_packet_unref(vPacket);
    }
    //释放内存
    sws_freeContext(sws_ctx);

    end_line:
    av_free(vPacket);
    av_frame_free(&vFrame);
    av_frame_free(&pFrameRGBA);
    av_frame_free(&filter_frame);
    avfilter_free(buffersrc_ctx);
    avfilter_free(buffersink_ctx);
    avfilter_graph_free(&filter_graph);
    avcodec_close(vCodecCtx);
    avformat_close_input(&pFormatCtx);
    avformat_free_context(pFormatCtx);
    avcodec_free_context(&vCodecCtx);
    ANativeWindow_release(nativeWindow);
    return 0;
}

/**
 * 释放视频播放
 */
void NativePlayer::mDestroy() {
    isRelease = 1;
    LOGE("释放视频资源，终止播放");
}