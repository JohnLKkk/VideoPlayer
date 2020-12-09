//
// Created by Void on 2020/12/9.
//

#include "Test.h"

extern "C" {
VIDEO_PLAYER_FUNC(void, setCallback, jobject callback) {
    jclass jniActivity = env->GetObjectClass(callback);

    jmethodID onCallback = env->GetMethodID(jniActivity, "onCallback", "(Ljava/lang/String;)");
    env->CallVoidMethod(callback, onCallback,"你好");
}
}