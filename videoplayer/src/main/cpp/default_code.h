//
// Created by Void on 2020/9/8.
//

#ifndef TESTEXAMPLE_DEFAULT_CODE_H
#define TESTEXAMPLE_DEFAULT_CODE_H

#include "ErrorCodeDefine.h"
#include "logger.h"

//线程类型
#define THREAD_MAIN 1
#define THREAD_CHILD 1

//引用该库所指定的类名
#define decoderClassName "Java_com_yoy_videoPlayer_processing_decoder_VideoFFMPEGDecoder_"

#define VIDEO_PLAYER_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_yoy_videoPlayer_processing_decoder_VideoFFMPEGDecoder_ ## FUNC_NAME \
    (JNIEnv *env, jobject thiz, ##__VA_ARGS__)\

#endif //TESTEXAMPLE_DEFAULT_CODE_H
