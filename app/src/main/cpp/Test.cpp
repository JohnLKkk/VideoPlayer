//
// Created by Void on 2020/12/9.
//

#include "Test.h"
#include "pthread.h"
#include<cstdio>
#include<cstdlib>
#include<unistd.h>
#include <cstring>

#define NUMTHREADS 5

extern "C" {
JavaVM *jvm = nullptr;
jobject g_obj = nullptr;

char *join1(char *s1, char *s2) {
    char *result = (char *) malloc(strlen(s1) + strlen(s2) + 1);
    if (result == nullptr)
        exit(1);
    strcpy(result, s1);
    strcat(result, s2);
    return result;
}
void *thread_fun(void *arg) {
    JNIEnv *env;
    jclass cls;
    jmethodID mid;
    //Attach主线程
    if (jvm->AttachCurrentThread(&env, nullptr) != JNI_OK) {
        LOGE("%s: AttachCurrentThread() failed", __FUNCTION__);
        return nullptr;
    }
    //找到对应的类
    cls = env->GetObjectClass(g_obj);
    if (cls == nullptr) {
        LOGE("FindClass() Error.....");
        goto error;
    }
    //再获得类中的方法
    mid = env->GetMethodID(cls, "fromJNI", "(I)V");
    if (mid == nullptr) {
        LOGE("GetMethodID() Error.....");
        goto error;
    }
    //最后调用java中的静态方法

    env->CallVoidMethod(g_obj, mid, (int) arg);

    error:
    //Detach主线程
    if (jvm->DetachCurrentThread() != JNI_OK) {
        LOGE("%s: DetachCurrentThread() failed", __FUNCTION__);
    }


    pthread_exit(nullptr);
}
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
    env->CallVoidMethod(thiz, onCallback, 1, tmp);
}
VIDEO_PLAYER_FUNC(void, initThreadJni) {
    env->GetJavaVM(&jvm);
    g_obj = env->NewGlobalRef(thiz);
}
VIDEO_PLAYER_FUNC(void, createAndRunThread) {
    pthread_t pt[NUMTHREADS];
    for (int i = 0; i < NUMTHREADS; ++i) {
        pthread_create(&pt[i], nullptr, &thread_fun, (void *) (i));
    }
}
}