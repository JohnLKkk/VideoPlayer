//
// Created by Void on 2020/11/30.
//

#include "native_lib_define.h"
#include "NativePlayer.h"


extern NativeLibDefine *libDefine;
extern NativePlayer nativePlayer;

ANativeWindow *nativeWindow;
AVFilterContext *buffersink_ctx;
AVFilterContext *buffersrc_ctx;
AVFilterGraph *filter_graph;
AVFormatContext *pFormatCtx = nullptr;
AVCodecContext *vCodecCtx = nullptr;

AVPacket *vPacket = nullptr;
//分别为:解码后的原始帧vFrame，参考帧pFrameRGBA,滤镜帧filter_frame(该处理解还彻底，后续补充、修改)
AVFrame *vFrame = nullptr, *pFrameRGBA = nullptr, *filter_frame = nullptr;
SwsContext *sws_ctx = nullptr;
uint8_t *out_buffer = nullptr;
ANativeWindow_Buffer windowBuffer;
AVRational time_base;
int width = 0;
int height = 0;

int NativePlayer::init_filters(bool isInit) {
    LOGD("设置的滤镜参数：%s", filter_descr);
    int ret;
//    if (isInit) {
    AVFilter *buffersrc = avfilter_get_by_name("buffer");
    AVFilter *buffersink = avfilter_get_by_name("buffersink");
    AVFilterInOut *outputs = avfilter_inout_alloc();
    AVFilterInOut *inputs = avfilter_inout_alloc();
    time_base = pFormatCtx->streams[videoIndex]->time_base;
    enum AVPixelFormat pix_fmts[] = {AV_PIX_FMT_YUV420P, AV_PIX_FMT_NONE};

    filter_graph = avfilter_graph_alloc();
    if (!outputs || !inputs || !filter_graph) {
        ret = AVERROR(ENOMEM);
        goto end;
    }

    char args[512];
    /* buffer video source: the decoded frames from the decoder will be inserted here. */
    snprintf(args, sizeof(args),
             "video_size=%dx%d:pix_fmt=%d:time_base=%d/%d:pixel_aspect=%d/%d",
             vCodecCtx->width, vCodecCtx->height, vCodecCtx->pix_fmt,
             time_base.num, time_base.den,
             vCodecCtx->sample_aspect_ratio.num, vCodecCtx->sample_aspect_ratio.den);

    ret = avfilter_graph_create_filter(&buffersrc_ctx, buffersrc, "in",
                                       args, nullptr, filter_graph);
    if (ret < 0) {
        LOGE("init_filter-Cannot create buffer source, ret=%d", ret);
        goto end;
    }

    /* buffer video sink: to terminate the filter chain. */
    ret = avfilter_graph_create_filter(&buffersink_ctx, buffersink, "out",
                                       nullptr, nullptr, filter_graph);
    if (ret < 0) {
        LOGE("init_filter-Cannot create buffer sink, ret=%d", ret);
        goto end;
    }

    ret = av_opt_set_int_list(buffersink_ctx, "pix_fmts", pix_fmts,
                              AV_PIX_FMT_NONE, AV_OPT_SEARCH_CHILDREN);
    if (ret < 0) {
        LOGE("init_filter-Cannot set output pixel format, ret=%d", ret);
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
//    }
    LOGE("output--filter:%s", filter_descr);
    if ((ret = avfilter_graph_parse_ptr(filter_graph, filter_descr,
                                        &inputs, &outputs, nullptr)) < 0) {
        LOGE("init_filter-avfilter_graph_parse_ptr error, ret=%d", ret);
        goto end;
    }

    if ((ret = avfilter_graph_config(filter_graph, nullptr)) < 0) {
        LOGE("init_filter-avfilter_graph_config error, ret=%d", ret);
        goto end;
    }
    avfilter_inout_free(&inputs);
    avfilter_inout_free(&outputs);

    end:
    if (ret < 0) {
        char errorStr[512];
        snprintf(errorStr, sizeof(errorStr), "切换滤镜失败:%d", ret);
        onErrorCallback(INIT_FAIL, errorStr);
    }
    return ret;
}

void NativePlayer::setPlayInfo(ANativeWindow *aNWindow) {
    nativeWindow = aNWindow;
    setPlayStatus(0);
    libDefine->jniPlayStatusCallback(0);
}

void *playVideo(void *arg) {
    //初始化所有组件
    av_register_all();
    //分配一个AVFormatContext结构
    pFormatCtx = avformat_alloc_context();
    //打开文件
    if (avformat_open_input(&pFormatCtx, nativePlayer.file_name, nullptr, nullptr) !=
        0) {
        LOGE("Could not open input stream:%s", nativePlayer.file_name);
        nativePlayer.onErrorCallback(VIDEO_STREAM_NOT_FOUNT, "Could not open input stream");
        goto end_line;
    }
    //3.查找文件的流信息
    if (avformat_find_stream_info(pFormatCtx, nullptr) < 0) {
        nativePlayer.onErrorCallback(VIDEO_STREAM_NOT_FOUNT, "Could not find stream information");
        nativePlayer.findFileInfo_Ok = 1;
        goto end_line;
    } else {
        nativePlayer.findFileInfo_Ok = 0;
    }
    //得到的总时长,*1000是将s->ms
    nativePlayer.jniMaxTime = (long) pFormatCtx->duration / AV_TIME_BASE * 1000;
    //4.查找视频轨(视频数据类型)
    for (int index = 0; index < pFormatCtx->nb_streams; index++) {
        if (pFormatCtx->streams[index]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            nativePlayer.videoIndex = index;
            break;
        }
    }
    if (nativePlayer.videoIndex == -1) {
        nativePlayer.onErrorCallback(VIDEO_STREAM_NOT_FOUNT, "Could not find a video stream");
        goto end_line;
    }
    //5.查找解码器
    nativePlayer.vCodec = avcodec_find_decoder(
            pFormatCtx->streams[nativePlayer.videoIndex]->codecpar->codec_id);
    if (nativePlayer.vCodec == nullptr) {
        nativePlayer.onErrorCallback(CODEC_NOT_FOUNT, "could not find codec");
        goto end_line;
    }
    //6.配置解码器
    vCodecCtx = avcodec_alloc_context3(nativePlayer.vCodec);
    avcodec_parameters_to_context(vCodecCtx,
                                  pFormatCtx->streams[nativePlayer.videoIndex]->codecpar);

    //7.打开解码器
    if (avcodec_open2(vCodecCtx, nativePlayer.vCodec, nullptr) < 0) {
        nativePlayer.onErrorCallback(OPEN_CODEC_FAIL, "Could not open codec");
        goto end_line;
    }
    width = vCodecCtx->width;
    height = vCodecCtx->height;
    //注册过滤器
    avfilter_register_all();
    filter_frame = av_frame_alloc();
    if (filter_frame == nullptr) {
        nativePlayer.onErrorCallback(INIT_FAIL, "init filter_frame fail");
        goto end_line;
    }
    //分配一个帧指针，指向解码后的原始帧
    vFrame = av_frame_alloc();
    if (vFrame == nullptr) {
        nativePlayer.onErrorCallback(INIT_FAIL, "init vFrame fail");
        goto end_line;
    }
    vPacket = (AVPacket *) av_malloc(sizeof(AVPacket));
    pFrameRGBA = av_frame_alloc();
    //绑定输出buffer
    nativePlayer.bufferSize = av_image_get_buffer_size(AV_PIX_FMT_RGBA, width,
                                                       height, 1);
    out_buffer = (uint8_t *) av_malloc(nativePlayer.bufferSize * sizeof(uint8_t));
    av_image_fill_arrays(pFrameRGBA->data, pFrameRGBA->linesize,
                         out_buffer, AV_PIX_FMT_RGBA,
                         width, height, 1);
    //创建SwsContext用于缩放、转换操作
    sws_ctx = sws_getContext(width, height,
                             vCodecCtx->pix_fmt,
                             width, height, AV_PIX_FMT_RGBA,
                             SWS_BILINEAR, nullptr, nullptr,
                             nullptr);
    //更改窗口缓冲区的格式和大小。
    if (ANativeWindow_setBuffersGeometry(nativeWindow, width,
                                         height, WINDOW_FORMAT_RGBA_8888) <
        0) {
        LOGE("Could not set buffers geometry");
        ANativeWindow_release(nativeWindow);
        goto end_line;
    }
    int ret;
    ret = nativePlayer.init_filters(true);
    if (ret < 0) {
        goto end_line;
    }
    //读取帧
    while (av_read_frame(pFormatCtx, vPacket) >= 0) {
        if (nativePlayer.getPlayStatus() == 4 || nativePlayer.getPlayStatus() == 5)break;
        //如果是停止播放，将停止读取帧
        if (nativePlayer.getPlayStatus() == 2)goto stopPlay;

        if (vPacket->stream_index == nativePlayer.videoIndex) {
            //视频解码
            if (avcodec_send_packet(vCodecCtx, vPacket) != 0) break;
            //从解码器接收返回的帧数据
            while (avcodec_receive_frame(vCodecCtx, vFrame) == 0) {
                if (nativePlayer.getPlayStatus() == 5)break;
                //获取当前帧对应的播放进度时间，并且忽略无效的时间戳
                int64_t tmp = vFrame->pts * av_q2d(time_base) * 1000;
                if (tmp >= 0) nativePlayer.jniCurrentTime = tmp;

                if (av_buffersrc_add_frame_flags(buffersrc_ctx, vFrame,
                                                 AV_BUFFERSRC_FLAG_KEEP_REF) <
                    0) {
                    LOGE("Error while feeding the filter_graph");
                    break;
                }
                av_buffersink_get_frame(buffersink_ctx, filter_frame);
                //转化格式
                sws_scale(sws_ctx,
                          (const uint8_t *const *) filter_frame->data,
                          filter_frame->linesize,
                          0,
                          vCodecCtx->height,
                          pFrameRGBA->data, pFrameRGBA->linesize);
                if (ANativeWindow_lock(nativeWindow, &windowBuffer,
                                       nullptr) >= 0) {
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
        }
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
    stopPlay:
    LOGW("stop play ......");
    pthread_exit(nullptr);
}

long long NativePlayer::getPlayProgress(int type) const {
    if (findFileInfo_Ok == 1)return 0;
    if (type == 0) {
//        LOGE("获取当前时间进度(ms),%ld", jniCurrentTime);
        return jniCurrentTime;
    } else {
        //得到的总时长(s)
//        LOGE("获取总时长(ms):%ld", jniMaxTime);
        return jniMaxTime;
    }
}

void NativePlayer::seekTo(int t) {
    //java传过来的时间单位是ms，这里需要将其转为微秒us
    long targetTime = t * 1000;
    av_seek_frame(pFormatCtx, -1, targetTime, AVSEEK_FLAG_BACKWARD);
}

void NativePlayer::setPlayStatus(int status) {
    if (status < -1 || status > 5)return;
    if (playStatus == status)return;
    if (status == 1) {
        pthread_create(&libDefine->pt[1], nullptr, &playVideo, nullptr);
    }
    playStatus = status;
}

int NativePlayer::getPlayStatus() const {
    return playStatus;
}

void NativePlayer::onErrorCallback(int errorCode, char const *msg) {
    errorStatus = errorCode;
    libDefine->jniErrorCallback(errorCode, msg);
}
