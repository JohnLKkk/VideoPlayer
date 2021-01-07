
#include "native_lib_define.h"


NativeLibDefine *libDefine;
NativePlayer nativePlayer;

NativeLibDefine::NativeLibDefine() {
    libDefine = this;
    nativePlayer = NativePlayer();
    stateCallbackList = new LinkedList();
    errorCallbackList = new LinkedList();
}

void NativeLibDefine::jniPlayStatusCallback(int status) const {
    if (isRelease || stateCallbackList == nullptr)return;
    LOGD("addCallback-playStatus--status:%d", status);
    stateCallbackList->add((BaseNode *) new JniBean(status));
}

void NativeLibDefine::jniErrorCallback(int errorCode, const char *msg) const {
    if (isRelease || errorCallbackList == nullptr)return;
    LOGE("addCallback-error--status:%d, msg:%s", errorCode, msg);
    errorCallbackList->add((BaseNode *) new JniBean(errorCode, msg));
}

/**
 * 回调程序
 * 专门处理回调内容的的线程
 */
void *onCallbackThread(void *arg) {
    JNIEnv *env = libDefine->get_env();
    JniBean *bean;
    jstring jmsg = nullptr;

    if (env == nullptr) goto delEnv;

    while (!libDefine->isRelease) {
        usleep(200 * 1000);
        if (libDefine->isRelease)break;
        if (libDefine->stateCallbackList != nullptr && libDefine->stateCallbackList->Size() > 0) {
            bean = (JniBean *) libDefine->stateCallbackList->get(0);
//            LOGD("回调线程运行中--playState--:code:%d", bean->code);
            env->CallVoidMethod(libDefine->g_obj, libDefine->playStatusCallback,
                                bean->code);
            libDefine->stateCallbackList->removeAt(0);
        }
        if (libDefine->errorCallbackList != nullptr && libDefine->errorCallbackList->Size() > 0) {
            bean = (JniBean *) libDefine->errorCallbackList->get(0);
//            LOGE("回调线程运行中--error--:code:%d ;msg:%s", bean->code, bean->msg);

            jmsg = env->NewStringUTF(bean->msg);
            env->CallVoidMethod(libDefine->g_obj, libDefine->errorCallback, bean->code,
                                jmsg);
            libDefine->errorCallbackList->removeAt(0);
        }
    }
    env->DeleteLocalRef(jmsg);
    delEnv:
    libDefine->del_env();
    pthread_exit(nullptr);
}

void NativeLibDefine::onRelease() {
    stateCallbackList->release();
    stateCallbackList = nullptr;
    errorCallbackList->release();
    errorCallbackList = nullptr;
}

extern "C" {
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    libDefine = new NativeLibDefine();
    libDefine->g_jvm = vm;
    return JNI_VERSION_1_6;
}

VIDEO_PLAYER_FUNC(void, initJni) {
    jclass ffmpegDecoder = env->GetObjectClass(thiz);
    if (!ffmpegDecoder) {
        LOGE("找不到类:%s", decoderClassName);
        return;
    }
    libDefine->playStatusCallback = env->GetMethodID(ffmpegDecoder,
                                                     "jniPlayStatusCallback", "(I)V");
    if (!libDefine->playStatusCallback) {
        LOGE("找不到方法:jniPlayStatusCallback(int)");
        return;
    }
    libDefine->errorCallback = env->GetMethodID(ffmpegDecoder, "jniErrorCallback",
                                                "(ILjava/lang/String;)V");
    if (!libDefine->errorCallback) {
        LOGE("找不到方法:jniErrorCallback(int,String)");
        return;
    }
    libDefine->createAudioTrack = env->GetMethodID(ffmpegDecoder, "createAudioTrack", "(II)V");
    if (!libDefine->createAudioTrack) {
        LOGE("找不到方法:createAudioTrack(int,int)");
        return;
    }
    libDefine->playAudioMethod = env->GetMethodID(ffmpegDecoder, "playAudio", "()V");
    if (!libDefine->playAudioMethod) {
        LOGE("找不到方法:playAudio()");
        return;
    }
    libDefine->stopAudioMethod = env->GetMethodID(ffmpegDecoder, "stopAudio", "()V");
    if (!libDefine->stopAudioMethod) {
        LOGE("找不到方法:stopAudio()");
        return;
    }
    libDefine->releaseJniMethod = env->GetMethodID(ffmpegDecoder, "releaseJni", "()V");
    if (!libDefine->releaseJniMethod) {
        LOGE("找不到方法:releaseJni()");
        return;
    }
    libDefine->writeAudioDataMethod = env->GetMethodID(ffmpegDecoder, "writeAudioData", "([BII)I");
    if (!libDefine->writeAudioDataMethod) {
        LOGE("找不到方法:releaseJni()");
        return;
    }

    libDefine->isRelease = false;
    libDefine->g_obj = env->NewGlobalRef(thiz);
    //执行消息回调线程
    pthread_create(&libDefine->pt[0], nullptr, &onCallbackThread, nullptr);
}

VIDEO_PLAYER_FUNC(void, setDataSource, jstring vPath, jobject surface) {
    if (libDefine->isRelease)return;
    nativePlayer.file_name = env->GetStringUTFChars(vPath, nullptr);
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
    if (nativeWindow == nullptr) {
        LOGE("Could not get native window from surface");
        libDefine->jniErrorCallback(INIT_FAIL, "播放异常，surface无效");
        return;
    }
    nativePlayer.setPlayInfo(nativeWindow);
}

VIDEO_PLAYER_FUNC(long long, getCurrentPosition) {
    if (libDefine->isRelease)return 0;
    return nativePlayer.getPlayProgress(0);
}

VIDEO_PLAYER_FUNC(long long, getDuration) {
    if (libDefine->isRelease)return 0;
    return nativePlayer.getPlayProgress(1);
}

VIDEO_PLAYER_FUNC(void, goSelectedTime, jint t) {
    if (libDefine->isRelease)return;
    return nativePlayer.seekTo(t);
}

VIDEO_PLAYER_FUNC(bool, mIsPlaying) {
    if (libDefine->isRelease)return false;
    return nativePlayer.getPlayStatus() == 1;
}

VIDEO_PLAYER_FUNC(void, setPlayState, jint status) {
    if (libDefine->isRelease)return;
    nativePlayer.setPlayStatus(status);
    libDefine->isRelease = status == 5;
    if (status != 5)return;
    libDefine->onRelease();
}

VIDEO_PLAYER_FUNC(void, setFilter, jstring value) {
    nativePlayer.filter_descr = env->GetStringUTFChars(value, nullptr);
    nativePlayer.setPlayStatus(2);
    usleep(50 * 1000);
    nativePlayer.setPlayStatus(1);
}
}
