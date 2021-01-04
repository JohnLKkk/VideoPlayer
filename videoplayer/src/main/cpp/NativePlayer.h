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
private:
public:
    int bufferSize = 0;
    //playStatus  -1=未知状态 0=准备 1=播放中 2=暂停中 3=播放完成 4=播放取消 5=释放资源
    int playStatus = -1;
    int errorStatus = -1;
    //播放进度(ms)
    long jniCurrentTime = 0L;

    const char *file_name;
    const char *filter_descr = "colorbalance=bs=0.3";
//    const char *filter_descr="lutrgb='r=0:g=0'";
    int findFileInfo_Ok = 1;
    int videoIndex = -1;
    long jniMaxTime = 0;

    AVCodec *vCodec = NULL;

    int init_filters(bool isInit);

    void setPlayInfo(ANativeWindow *aNWindow);

    /**
     * 获取播放进度
     * @param type 0当前进度 1总时长
     * @return 返回对应时间单位毫秒(ms)
     */
    long long getPlayProgress(int type) const;

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
};


#endif //TESTEXAMPLE_NATIVEPLAYER_H
