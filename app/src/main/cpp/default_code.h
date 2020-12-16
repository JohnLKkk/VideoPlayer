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


char *join1(char *s1, char *s2) {
    char *result = (char *) malloc(strlen(s1) + strlen(s2) + 1);
    if (result == nullptr)
        exit(1);
    strcpy(result, s1);
    strcat(result, s2);
    return result;
}

#endif //TESTEXAMPLE_DEFAULT_CODE_H
