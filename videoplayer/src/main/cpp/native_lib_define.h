//
// Created by Void on 2020/11/30.
//
#include <jni.h>
#include <string>
#include <iostream>
#include <cstdlib>
#include <unistd.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>

#ifndef TESTEXAMPLE_NATIVE_LIB_DEFINE_H
#define TESTEXAMPLE_NATIVE_LIB_DEFINE_H

#include "NativePlayer.h"

typedef struct JniBeanNode {
    BaseNode node;
    int code;
    const char *msg;

    JniBeanNode(int code) {
        this->code = code;
        this->msg = "-";
    }

    JniBeanNode(int code, const char *msg) {
        this->code = code;
        checkUtf8Bytes((char *) msg);
        if (strlen(msg) == 0) {
            msg = "-";
        }
        this->msg = msg;
    }
} JniBean;

class NativeLibDefine {
public:
    LinkedList *stateCallbackList;
    LinkedList *errorCallbackList;
    //全局释放标识
    bool isRelease = false;
    //线程指针数组
    pthread_t pt[10];

    JavaVM *g_jvm = nullptr;
    jobject g_obj = nullptr;
    jmethodID playStatusCallback = nullptr;
    jmethodID errorCallback = nullptr;
    jmethodID createAudioTrack = nullptr;
    jmethodID playAudioMethod = nullptr;
    jmethodID stopAudioMethod = nullptr;
    jmethodID releaseJniMethod = nullptr;
    jmethodID writeAudioDataMethod = nullptr;

    NativeLibDefine();

    void jniPlayStatusCallback(int status) const;

    void jniErrorCallback(int errorCode, char const *msg) const;

    void onRelease();

    JNIEnv *get_env() {
        if (g_jvm == nullptr)return nullptr;
        JNIEnv *env = nullptr;
        int status = g_jvm->GetEnv((void **) &env, JNI_VERSION_1_6);
        if (status == JNI_EDETACHED || env == nullptr) {
            status = g_jvm->AttachCurrentThread(&env, nullptr);
            if (status < 0) env = nullptr;
        }
        return env;
    }

    void del_env() {
        g_jvm->DetachCurrentThread();
    }
};


#endif //TESTEXAMPLE_NATIVE_LIB_DEFINE_H