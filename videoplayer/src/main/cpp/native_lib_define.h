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
    JNIEnv *env= nullptr;
    jmethodID playStatusCallback= nullptr;
    jmethodID errorCallback= nullptr;
    jclass ffmpegDecoder= nullptr;

    NativeLibDefine();
    void jniPlayStatusCallback(int status){

        env->CallVoidMethod(ffmpegDecoder,playStatusCallback,status);
    }
    void jniErrorCallback(int errorCode,char const* msg){
        env->CallVoidMethod(ffmpegDecoder,errorCallback,errorCode,msg);
    }
};


#endif //TESTEXAMPLE_NATIVE_LIB_DEFINE_H