//
// Created by Void on 2020/12/9.
//

#include "Test.h"

extern "C" {
VIDEO_PLAYER_FUNC(void, setCallback, jobject callback) {
    jclass jniActivity = env->GetObjectClass(callback);
    if (!jniActivity) {
        LOGE("jniActivity not found...");
        return;
    }
    jmethodID onCallback = env->GetMethodID(jniActivity, "onCallback", "(Ljava/lang/String;)V");
    if (!onCallback) {
        LOGE("jniActivity not found...");
        return;
    }
    jstring tmp = env->NewStringUTF("你好");
    env->CallVoidMethod(callback, onCallback, tmp);
}
}