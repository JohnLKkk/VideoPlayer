//
// Created by Void on 2020/11/30.
//
#include <jni.h>
#include <string>
#include <iostream>
#include <cstdlib>
#include <android/native_window.h>
#include <android/native_window_jni.h>

#ifndef TESTEXAMPLE_NATIVE_LIB_DEFINE_H
#define TESTEXAMPLE_NATIVE_LIB_DEFINE_H

#include "NativePlayer.h"

class NativeLibDefine {
public:
    JavaVM *g_jvm = nullptr;
    jobject g_obj = nullptr;
    JNIEnv *env = nullptr;
    jmethodID playStatusCallback = nullptr;
    jmethodID errorCallback = nullptr;

    NativeLibDefine();

    void jniPlayStatusCallback(int status) {
        if (g_jvm->AttachCurrentThread(&env, nullptr) != JNI_OK) {
            LOGE("%s: AttachCurrentThread() failed", __FUNCTION__);
            return;
        }

        env->CallVoidMethod(g_obj, playStatusCallback, status);
        if (g_jvm->DetachCurrentThread() != JNI_OK) {
            LOGE("%s: DetachCurrentThread() failed", __FUNCTION__);
        }
    }

    void jniErrorCallback(int errorCode, char const *msg) {
        if (g_jvm->AttachCurrentThread(&env, nullptr) != JNI_OK) {
            LOGE("%s: AttachCurrentThread() failed", __FUNCTION__);
            return;
        }
        jstring jmsg = env->NewStringUTF(msg);
        env->CallVoidMethod(g_obj, errorCallback, errorCode, jmsg);
        env->DeleteLocalRef(jmsg);
        if (g_jvm->DetachCurrentThread() != JNI_OK) {
            LOGE("%s: DetachCurrentThread() failed", __FUNCTION__);
        }
    }
};


#endif //TESTEXAMPLE_NATIVE_LIB_DEFINE_H