//
// Created by Void on 2020/9/8.
//

#ifndef TESTEXAMPLE_DEFAULT_CODE_H
#define TESTEXAMPLE_DEFAULT_CODE_H

#include "ErrorCodeDefine.h"
#include "define/linked_list_define.cpp"
#include "logger.h"

//线程类型
#define THREAD_MAIN 1
#define THREAD_CHILD 1

//引用该库所指定的类名
#define decoderClassName "Java_com_yoy_videoPlayer_processing_decoder_FFMPEGDecoderJni_"

#define VIDEO_PLAYER_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_yoy_videoPlayer_processing_decoder_FFMPEGDecoderJni_ ## FUNC_NAME \
    (JNIEnv *env, jobject thiz, ##__VA_ARGS__)\


inline void checkUtf8Bytes(char *bytes) {
    char three = 0;
    while (*bytes != '\0') {
        unsigned char utf8 = *(bytes++);
        three = 0;
// Switch on the high four bits.
        switch (utf8 >> 4) {
            case 0x00:
            case 0x01:
            case 0x02:
            case 0x03:
            case 0x04:
            case 0x05:
            case 0x06:
            case 0x07:
// Bit pattern 0xxx. No need for any extra bytes.
                break;
            case 0x08:
            case 0x09:
            case 0x0a:
            case 0x0b:
            case 0x0f:
/*
 * Bit pattern 10xx or 1111, which are illegal start bytes.
 * Note: 1111 is valid for normal UTF-8, but not the
 * modified UTF-8 used here.
 */
                *(bytes - 1) = '?';
                break;
            case 0x0e:
// Bit pattern 1110, so there are two additional bytes.
                utf8 = *(bytes++);
                if ((utf8 & 0xc0) != 0x80) {
                    --
                            bytes;
                    *(bytes - 1) = '?';
                    break;
                }
                three = 1;
// Fall through to take care of the final byte.
            case 0x0c:
            case 0x0d:
// Bit pattern 110x, so there is one additional byte.
                utf8 = *(bytes++);
                if ((utf8 & 0xc0) != 0x80) {
                    --
                            bytes;
                    if (three)
                        --
                                bytes;
                    *(bytes - 1) = '?';
                }
                break;
        }
    }
}

#endif //TESTEXAMPLE_DEFAULT_CODE_H
