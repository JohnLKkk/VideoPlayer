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
    int findFileInfoOk = 1;
    //playStatus  -1=未知状态 0=准备 1=播放中 2=暂停中 3=播放完成 4=播放取消 5=释放资源
    int playStatus = -1;
    int errorStatus = -1;

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

    /**
     * 设置播放状态
     * 注：设置状态5后，该引用将失效，需要重新初始化。
     * @param status -1=未知状态 0=准备 1=播放中 2=暂停中 3=播放完成 4=播放取消 5=释放资源
     */
    void setPlayStatus(int status);

    int getPlayStatus() const;

    /**
     * 回调错误到Java层处理
     * @param errorCode
     */
    void onErrorCallback(int errorCode,char const* msg);
};


#endif //TESTEXAMPLE_NATIVEPLAYER_H
