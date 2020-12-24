//
// Created by Void on 2020/9/8.
//

#ifndef TESTEXAMPLE_DEFAULT_CODE_H
#define TESTEXAMPLE_DEFAULT_CODE_H

#include "logger.h"
#include "jni.h"
#include <cstring>
#include <cstdlib>

#define VIDEO_PLAYER_FUNC(RETURN_TYPE, FUNC_NAME, ...) \
    JNIEXPORT RETURN_TYPE JNICALL Java_com_example_testdemo_testModel_jniTest_JniActivity_ ## FUNC_NAME \
    (JNIEnv *env, jobject thiz, ##__VA_ARGS__)\

#endif //TESTEXAMPLE_DEFAULT_CODE_H
