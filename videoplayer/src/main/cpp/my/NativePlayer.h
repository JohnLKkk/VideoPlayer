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

#ifdef __cplusplus
}
#endif

class NativePlayer {
    int width = 0;
    int height = 0;
    int bufferSize = 0;
    int videoIndex = -1;

    AVPacket *vPacket = NULL;
    AVFrame *vFrame = NULL, *pFrameRGBA = NULL;
    AVCodecContext *vCodecCtx = NULL;
    SwsContext *sws_ctx = NULL;
    AVFormatContext *pFormatCtx = NULL;
    uint8_t *out_buffer = NULL;
    ANativeWindow_Buffer windowBuffer;

    AVCodec *vCodec = NULL;
public:
    int playVideo(const char *vPath, ANativeWindow *nativeWindow);

    void mDestroy();
};


#endif //TESTEXAMPLE_NATIVEPLAYER_H
