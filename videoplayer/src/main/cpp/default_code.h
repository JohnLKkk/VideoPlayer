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


/**
* @brief 拼接字符串
*
* @return char* 组合后的字符串指针
*/
char *join1(const char *, const char *);

char *join1(const char *s1, const char *s2) {
    char *result = (char *) malloc(strlen(s1) + strlen(s2) + 1);
    if (result == NULL)
        exit(1);
    strcpy(result, s1);
    strcat(result, s2);
    return result;
}

#endif //TESTEXAMPLE_DEFAULT_CODE_H
