//
// Created by Void on 2020/12/9.
//

#include "Test.h"
#include "pthread.h"
#include<cstdio>
#include<cstdlib>
#include<unistd.h>
#include <cstring>
#include "list_define.h"
#include "list_2_define.h"

#define NUMTHREADS 5

extern "C" {
JavaVM *jvm = nullptr;
jobject g_obj = nullptr;
List *jniBeanList = initList(10, 10);
bool isRealse = false;

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
//    jstring tmp = env->NewStringUTF("你好");
    while (isRealse) {

//        env->CallVoidMethod(thiz, onCallback, 1, tmp);
    }
//    env->CallVoidMethod(thiz, onCallback, 1, tmp);
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

VIDEO_PLAYER_FUNC(void, postMsg, jobject bean) {
    jclass jniBean = env->GetObjectClass(bean);
    jfieldID id_msg = env->GetFieldID(jniBean, "msg", "Ljava/lang/String;");
    jfieldID id_code = env->GetFieldID(jniBean, "code", "I");
    jobject msg = env->GetObjectField(bean, id_msg);
    jint code = env->GetIntField(bean, id_code);
    const char *str = env->GetStringUTFChars((jstring) (msg), nullptr);
    LOGI("code:%d ;msg:%s", code, str);

    int size1 = jniBeanList->size;
    listSet(jniBeanList, 0, JniBean(code, str).getObj());

    int size2 = jniBeanList->size;
    auto *item_bean = (struct JniBean *) listGet(jniBeanList, 0);
    LOGI("size:%d ;size2:%d", size1, size2);

//    jclass jniBean_ = env->GetObjectClass(item_bean);
//    jfieldID id_msg_ = env->GetFieldID(jniBean_, "msg", "Ljava/lang/String;");
//    jfieldID id_code_ = env->GetFieldID(jniBean_, "code", "I");
//    jobject msg_ = env->GetStringUTFChars(item_bean.msg, nullptr);
    jint code_ = item_bean->code;
//    const char *str_ = env->GetStringUTFChars((jstring) (msg_), nullptr);
    LOGI("code__:%d ;msg:%s", code_, item_bean->msg);
}

List *jniBeanList_2;
List_2 mList = List_2();

JNIEXPORT int JNICALL
Java_com_example_testdemo_ExampleInstrumentedTest_goFun(JNIEnv *env, jobject thiz,
                                                        int code, jstring msg) {
    const char *tmp = env->GetStringUTFChars(msg, nullptr);

    //    LOGE("------------start-list-1------------");
//    LOGI("code:%d; msg:%s", code, tmp);
//    jniBeanList_2 = initList(10, sizeof(JniBean(0, "1")));
//
//    listSet(jniBeanList_2, 0, JniBean(code, tmp).getObj());
//
//    auto *item_bean = (struct JniBean *) listGet(jniBeanList_2, 0);
//    LOGI("code__:%d ;msg:%s", item_bean->code, item_bean->msg);
//    LOGE("------------end-list-1------------");

    LOGE("------------start-list-2------------");
    mList.addItem(JniBean(code, tmp).getObj());

    auto *item_bean = (struct JniBean *) mList.get(0);
    LOGI("code__:%d ;msg:%s", item_bean->code, item_bean->msg);
    item_bean->code = 555666;
    item_bean->msg = "cccddd hellow";
    mList.set(0, item_bean);
    LOGI("code__:%d ;msg:%s", item_bean->code, item_bean->msg);
    LOGE("------------end-list-2------------");
    return 0;
}
JNIEXPORT void JNICALL
Java_com_example_testdemo_ExampleInstrumentedTest_mRelease(JNIEnv *env, jobject thiz) {
    mList.release();
}
}