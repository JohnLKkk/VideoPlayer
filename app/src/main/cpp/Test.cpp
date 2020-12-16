//
// Created by Void on 2020/12/9.
//

#include "Test.h"
#include "pthread.h"
#include <cstdio>
#include <unistd.h>
#include <sys/time.h>
#include "define/linked_list_define.cpp"

#define NUMTHREADS 2
typedef struct JniBeanNode {
    BaseNode node;
    int code;
    const char *msg;

    JniBeanNode(int code, const char *msg) {
        this->code = code;
        this->msg = msg;
    }
} JniBean;

extern "C" {
JavaVM *jvm = nullptr;
jobject g_obj = nullptr;
LinkedList *mList = new LinkedList();
bool isRealse = false;


void *thread_addMsg(void *arg) {
//    LOGI("---开始发送消息");
    for (int i = 0; i < 50; ++i) {
        usleep(500*1000);
        auto *bean = new JniBean(i, "error---");
//        LOGI("---添加消息：%d, %s", bean->code, bean->msg);
        mList->add((BaseNode *) bean);
    }
    pthread_exit(nullptr);
}

void *thread_callback(void *arg) {
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
    mid = env->GetMethodID(cls, "onCallback", "(ILjava/lang/String;)V");
    if (mid == nullptr) {
        LOGE("GetMethodID() Error.....");
        goto error;
    }
    //最后调用java中的静态方法
//    LOGI("-------开始等待信息");
    JniBean *bean;
    while (!isRealse) {
        usleep(200*1000);
        if (mList->Size() <= 0)continue;
        bean = (JniBean *) mList->get(0);
//        LOGI("---收到消息，回调到Java：%d, %s", bean->code, bean->msg);
        jstring tmp = env->NewStringUTF(bean->msg);
        env->CallVoidMethod(g_obj, mid, bean->code, tmp);
        mList->removeAt(0);
    }

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

    pthread_t pt[NUMTHREADS];
    pthread_create(&pt[0], nullptr, &thread_callback, nullptr);
    LOGI("跑了吗");
    usleep(200*1000);
    pthread_create(&pt[1], nullptr, &thread_addMsg, nullptr);
}
VIDEO_PLAYER_FUNC(void, mRelease) {
    isRealse = true;
    mList->release();
}

VIDEO_PLAYER_FUNC(void, postMsg, jobject bean) {
    jclass jniBean = env->GetObjectClass(bean);
    jfieldID id_msg = env->GetFieldID(jniBean, "msg", "Ljava/lang/String;");
    jfieldID id_code = env->GetFieldID(jniBean, "code", "I");
    jobject msg = env->GetObjectField(bean, id_msg);
    jint code = env->GetIntField(bean, id_code);
    const char *str = env->GetStringUTFChars((jstring) (msg), nullptr);
    LOGI("code:%d ;msg:%s", code, str);

//    int size1 = jniBeanList->size;
//    listSet(jniBeanList, 0, JniBean(code, str).getObj());
//
//    int size2 = jniBeanList->size;
//    auto *item_bean = (struct JniBean *) listGet(jniBeanList, 0);
//    LOGI("size:%d ;size2:%d", size1, size2);

//    jclass jniBean_ = env->GetObjectClass(item_bean);
//    jfieldID id_msg_ = env->GetFieldID(jniBean_, "msg", "Ljava/lang/String;");
//    jfieldID id_code_ = env->GetFieldID(jniBean_, "code", "I");
//    jobject msg_ = env->GetStringUTFChars(item_bean.msg, nullptr);
//    jint code_ = item_bean->code;
//    const char *str_ = env->GetStringUTFChars((jstring) (msg_), nullptr);
//    LOGI("code__:%d ;msg:%s", code_, item_bean->msg);
}
}