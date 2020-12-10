//
// Created by Void on 2020/12/9.
//

#include "Test.h"

extern "C" {
VIDEO_PLAYER_FUNC(void, setCallback) {
    jclass jniActivity = env->GetObjectClass(thiz);
    if (!jniActivity) {
        LOGE("jniActivity not found...");
        return;
    }
    jmethodID onCallback = env->GetMethodID(jniActivity, "onCallback", "(ILjava/lang/String;)V");
    if (!onCallback) {
        LOGE("jniActivity not found...");
        return;
    }
    jstring tmp = env->NewStringUTF("你好");
    env->CallVoidMethod(thiz, onCallback,1, tmp);
}
}