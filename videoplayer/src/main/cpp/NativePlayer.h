//
// Created by Void on 2020/11/30.
//

#include <android/native_window.h>
#include "default_code.h"

#ifndef TESTEXAMPLE_NATIVEPLAYER_H
#define TESTEXAMPLE_NATIVEPLAYER_H
#ifdef __cplusplus
extern "C" {
#endif

#include "libavformat/avformat.h"
#include "libavutil/imgutils.h"
#include "libswscale/swscale.h"
#include "libavfilter/avfilter.h"
#include "libavutil/opt.h"
#include <libavfilter/buffersrc.h>
#include <libavfilter/buffersink.h>

#ifdef __cplusplus
}
#endif

class NativePlayer {
    int width = 0;
    int height = 0;
    int bufferSize = 0;
    int videoIndex = -1;

    AVPacket *vPacket = NULL;
    AVFrame *vFrame = NULL, *pFrameRGBA = NULL, *filter_frame = NULL;
    AVCodecContext *vCodecCtx = NULL;
    SwsContext *sws_ctx = NULL;
    AVFormatContext *pFormatCtx = NULL;
    uint8_t *out_buffer = NULL;
    ANativeWindow_Buffer windowBuffer;
    AVRational time_base;
    AVCodec *vCodec = NULL;

    AVFilterContext *buffersink_ctx;
    AVFilterContext *buffersrc_ctx;
    AVFilterGraph *filter_graph;
    int findFileInfoOk = 1;
public:
    int init_filters(const char *filters_descr);

    int playVideo(const char *vPath, ANativeWindow *nativeWindow);

    /**
     * 获取播放进度
     * @param type 0当前进度 1总时长
     * @return 返回对应时间单位毫秒(ms)
     */
    long long getPlayProgress(int type);

    /**
     * 设置播放进度
     * @param t 目标时间戳单位：ms
     */
    void seekTo(int t);

    void mDestroy();
};


#endif //TESTEXAMPLE_NATIVEPLAYER_H
