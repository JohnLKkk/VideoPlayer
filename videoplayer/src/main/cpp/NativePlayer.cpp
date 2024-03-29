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
AVCodecContext *audioCodecCtx = nullptr;

//分别为:解码后的原始帧vFrame，参考帧pFrameRGBA,滤镜帧filter_frame(该处理解还彻底，后续补充、修改)
AVFrame *vFrame = nullptr, *pFrameRGBA = nullptr, *filter_frame = nullptr;
SwsContext *sws_ctx = nullptr;
SwrContext *audio_swr_ctx = nullptr;
uint8_t *video_out_Buffer = nullptr;
uint8_t *audio_out_Buffer = nullptr;

AVRational time_base;
enum AVSampleFormat out_sample_fmt;
int mWidth = 0;
int mHeight = 0;
int out_channel_nb;
int got_frame;

void *playVideo(void *arg) {
    int ret;
    ret = nativePlayer.change_filter();
    if (ret < 0) {
        goto end_line;
    }

    ANativeWindow_Buffer windowBuffer;
    AVPacket avPacket;
    //读取帧
    while (av_read_frame(pFormatCtx, &avPacket) >= 0) {
        if (nativePlayer.getPlayStatus() == 4 || nativePlayer.getPlayStatus() == 5)break;
        //如果是停止播放，将停止读取帧
        if (nativePlayer.getPlayStatus() == 2)goto stopPlay;

        if (avPacket.stream_index == nativePlayer.videoIndex) {
            //视频解码
            if (avcodec_send_packet(vCodecCtx, &avPacket) != 0) break;
            //从解码器接收返回的帧数据 vFrame
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
                          mHeight,
                          pFrameRGBA->data, pFrameRGBA->linesize);
                //锁定窗口
                if (ANativeWindow_lock(nativeWindow, &windowBuffer,
                                       nullptr) >= 0) {
                    //逐行复制
                    auto *bufferBits = (uint8_t *) windowBuffer.bits;
                    for (int h = 0; h < mHeight; h++) {
                        memcpy(bufferBits + h * windowBuffer.stride * 4,
                               video_out_Buffer + h * pFrameRGBA->linesize[0],
                               pFrameRGBA->linesize[0]);
                    }
                    ANativeWindow_unlockAndPost(nativeWindow);
                } else {
                    LOGE("cannot lock window");
                }
            }
        } else if (avPacket.stream_index == nativePlayer.audioIndex && nativePlayer.isPlayAudio) {
            nativePlayer.writeAudioData(&avPacket, vFrame);
        }
        av_packet_unref(&avPacket);
        av_frame_unref(filter_frame);
    }
    //释放内存
    sws_freeContext(sws_ctx);

    end_line:
//    av_free(&vPacket);
    av_frame_free(&vFrame);
    av_frame_free(&pFrameRGBA);
    av_frame_free(&filter_frame);
    avfilter_free(buffersrc_ctx);
    avfilter_free(buffersink_ctx);
    avfilter_graph_free(&filter_graph);
    avcodec_close(vCodecCtx);
    avformat_close_input(&pFormatCtx);
    avcodec_free_context(&vCodecCtx);
    ANativeWindow_release(nativeWindow);
    stopPlay:
    LOGW("stop play ......");
    return 0;
}

void log_callback(void *ptr, int level, const char *format, va_list args) {
    if (IsCloseLog_v)return;
    switch (level) {
        case AV_LOG_DEBUG:
            LOGD_v(format, args);
            break;
        case AV_LOG_WARNING:
            LOGW_v(format, args);
            break;
        case AV_LOG_ERROR:
            LOGE_v(format, args);
            break;
        default:
            LOGI_v(format, args);
            break;
    }
}

int NativePlayer::init_player() {
    // 设置日志输出等级
    av_log_set_level(AV_LOG_INFO);
    // 设置日志回调
    av_log_set_callback(log_callback);

    int ret;
    if ((ret = open_file(file_name)) < 0) {
        LOGE("");
        goto error;
    }
    //注册过滤器
    avfilter_register_all();
    filter_frame = av_frame_alloc();
    if (filter_frame == nullptr) {
        libDefine->jniErrorCallback(INIT_FAIL, "init filter_frame fail");
        goto error;
    }
    init_audio();
    return 0;

    error:
    return ret;
}

int NativePlayer::open_file(const char *file_path) {
    if (file_path == nullptr || strlen(file_path) <= 0)return -1;
    int ret;
    nativePlayer.videoIndex = -1;
    nativePlayer.audioIndex = -1;
    //初始化所有组件
    av_register_all();
    //分配一个AVFormatContext结构
    pFormatCtx = avformat_alloc_context();
    //打开文件
    if (avformat_open_input(&pFormatCtx, file_path, nullptr, nullptr) != 0) {
        LOGE("Could not open input stream:%s", file_path);
        libDefine->jniErrorCallback(VIDEO_STREAM_NOT_FOUNT, "Could not open input stream");
        return -1;
    }
    //3.查找文件的流信息
    if (avformat_find_stream_info(pFormatCtx, nullptr) < 0) {
        libDefine->jniErrorCallback(VIDEO_STREAM_NOT_FOUNT, "Could not find stream information");
        nativePlayer.findFileInfo_Ok = 1;
        return -1;
    } else {
        nativePlayer.findFileInfo_Ok = 0;
    }
    //得到的总时长,*1000是将s->ms
    nativePlayer.jniMaxTime = (long) pFormatCtx->duration / AV_TIME_BASE * 1000;
    //4.查找视频轨、音轨(视频数据类型)
    for (int index = 0; index < pFormatCtx->nb_streams; index++) {
        if (nativePlayer.videoIndex != -1 && nativePlayer.audioIndex != -1)break;
        if (pFormatCtx->streams[index]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO &&
            nativePlayer.videoIndex == -1) {
            nativePlayer.videoIndex = index;
        } else if (pFormatCtx->streams[index]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO &&
                   nativePlayer.audioIndex == -1) {
            nativePlayer.audioIndex = index;
        }
    }
    if (nativePlayer.videoIndex == -1 || nativePlayer.audioIndex == -1) {
        libDefine->jniErrorCallback(VIDEO_STREAM_NOT_FOUNT, "Could not find a video、audio stream");
        return -1;
    }
    //5.查找视频解码器
    nativePlayer.vCodec = avcodec_find_decoder(
            pFormatCtx->streams[nativePlayer.videoIndex]->codecpar->codec_id);
    if (nativePlayer.vCodec == nullptr) {
        libDefine->jniErrorCallback(CODEC_NOT_FOUNT, "could not find codec");
        return -1;
    }
    //6.配置解码器
    vCodecCtx = avcodec_alloc_context3(nativePlayer.vCodec);
    avcodec_parameters_to_context(vCodecCtx,
                                  pFormatCtx->streams[nativePlayer.videoIndex]->codecpar);
    //7.打开解码器
    if (avcodec_open2(vCodecCtx, nativePlayer.vCodec, nullptr) < 0) {
        libDefine->jniErrorCallback(OPEN_CODEC_FAIL, "Could not open codec");
        return -1;
    }
    mWidth = vCodecCtx->width;
    mHeight = vCodecCtx->height;
    if (nativeWindow== nullptr)throw "nativeWindow not null";
    //更改窗口缓冲区的格式和大小。
    if (ANativeWindow_setBuffersGeometry(nativeWindow, mWidth,
                                         mHeight, WINDOW_FORMAT_RGBA_8888) <
        0) {
        LOGE("Could not set buffers geometry");
        ANativeWindow_release(nativeWindow);
        return -1;
    }

    //分配一个帧指针，指向解码后的原始帧
    vFrame = av_frame_alloc();
    pFrameRGBA = av_frame_alloc();
    if (vFrame == nullptr || pFrameRGBA == nullptr) {
        libDefine->jniErrorCallback(INIT_FAIL, "init vFrame fail");
        return -1;
    }
    //绑定输出buffer
    int bufferSize = av_image_get_buffer_size(AV_PIX_FMT_RGBA, mWidth, mHeight, 1);
    video_out_Buffer = (uint8_t *) av_malloc(bufferSize * sizeof(uint8_t));
    av_image_fill_arrays(pFrameRGBA->data, pFrameRGBA->linesize,
                         video_out_Buffer, AV_PIX_FMT_RGBA,
                         mWidth, mHeight, 1);
    //创建SwsContext用于缩放、转换操作
    sws_ctx = sws_getContext(mWidth, mHeight,
                             vCodecCtx->pix_fmt,
                             mWidth, mHeight, AV_PIX_FMT_RGBA,
                             SWS_BILINEAR, nullptr, nullptr,
                             nullptr);
    return 0;
}

int NativePlayer::change_filter() const {
    LOGD("设置的滤镜参数：%s", filter_descr);
    int ret;
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
             mWidth, mHeight, vCodecCtx->pix_fmt,
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
//    LOGE("output--filter:%s", filter_descr);
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
        libDefine->jniErrorCallback(FILTER_CHANGE_FAIL, errorStr);
    } else {
        libDefine->jniPlayStatusCallback(5);
        LOGD("切换滤镜完成");
    }
    return ret;
}

int NativePlayer::init_audio() {
    //查找音频解码器
    nativePlayer.aCodec = avcodec_find_decoder(
            pFormatCtx->streams[nativePlayer.audioIndex]->codecpar->codec_id);
    if (nativePlayer.aCodec == nullptr) {
        libDefine->jniErrorCallback(CODEC_NOT_FOUNT, "could not find audio codec");
        return -1;
    }
    //6.配置解码器
    audioCodecCtx = avcodec_alloc_context3(nativePlayer.aCodec);
    avcodec_parameters_to_context(audioCodecCtx,
                                  pFormatCtx->streams[nativePlayer.audioIndex]->codecpar);
    if (avcodec_open2(audioCodecCtx, nativePlayer.aCodec, nullptr) < 0) {
        libDefine->jniErrorCallback(OPEN_CODEC_FAIL, "Could not open audio codec");
        return -1;
    }
    audio_swr_ctx = swr_alloc();
    enum AVSampleFormat in_sample_fmt = audioCodecCtx->sample_fmt;
    out_sample_fmt = AV_SAMPLE_FMT_S16;
    int in_sample_rate = audioCodecCtx->sample_rate;
    int out_sample_rate = in_sample_rate;
    uint64_t in_ch_layout = audioCodecCtx->channel_layout;
    uint64_t out_ch_layout = AV_CH_LAYOUT_STEREO;

    swr_alloc_set_opts(audio_swr_ctx,
                       out_ch_layout, out_sample_fmt, out_sample_rate,
                       in_ch_layout, in_sample_fmt, in_sample_rate,
                       0, NULL);
    swr_init(audio_swr_ctx);
    out_channel_nb = av_get_channel_layout_nb_channels(out_ch_layout);

    JNIEnv *env = libDefine->get_env();
    env->CallVoidMethod(libDefine->g_obj, libDefine->createAudioTrack, out_sample_rate,
                        out_channel_nb);
    env->CallVoidMethod(libDefine->g_obj, libDefine->playAudioMethod);
    audio_out_Buffer = (uint8_t *) av_malloc(MAX_AUDIO_FRAME_SIZE);
    return 0;
}

void NativePlayer::setPlayInfo(ANativeWindow *aNWindow) {
    nativeWindow = aNWindow;
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
    av_seek_frame(pFormatCtx, -1, t * 1000L, AVSEEK_FLAG_BACKWARD);
}

void NativePlayer::setPlayStatus(int status) {
    if (status < -1 || status > 5)return;
    if (playStatus == status)return;
    if (status == 1) {
        pthread_create(&libDefine->pt[2], nullptr, &playVideo, nullptr);
    }
    playStatus = status;
}

int NativePlayer::getPlayStatus() const {
    return playStatus;
}

void NativePlayer::writeAudioData(AVPacket *packet, AVFrame *frame) {
    int ret;
    if ((ret = avcodec_decode_audio4(audioCodecCtx, frame, &got_frame, packet)) < 0) {
        LOGE("avcodec_decode_audio4 fail:%d", ret);
        return;
    }
    //无法解码
    if (got_frame <= 0)return;
    swr_convert(audio_swr_ctx, &audio_out_Buffer, MAX_AUDIO_FRAME_SIZE,
                (const uint8_t **) frame->data, frame->nb_samples);
    int out_buffer_size = av_samples_get_buffer_size(NULL, out_channel_nb,
                                                     frame->nb_samples, out_sample_fmt, 1);
    JNIEnv *env = libDefine->get_env();
    jbyteArray audio_sample_array = env->NewByteArray(out_buffer_size);
    jbyte *sample_byte_array = env->GetByteArrayElements(audio_sample_array, NULL);
    memcpy(sample_byte_array, audio_out_Buffer, (size_t) out_buffer_size);
    env->ReleaseByteArrayElements(audio_sample_array, sample_byte_array, 0);
    env->CallIntMethod(libDefine->g_obj, libDefine->writeAudioDataMethod, audio_sample_array,
                       0, out_buffer_size);
    env->DeleteLocalRef(audio_sample_array);
    usleep(1000);
}
