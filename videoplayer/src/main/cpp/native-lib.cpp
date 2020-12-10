
#include "native_lib_define.h"

extern "C" {
NativeLibDefine *libDefine;
NativePlayer nativePlayer;

NativeLibDefine::NativeLibDefine() {
    libDefine = this;
    nativePlayer = NativePlayer();
}
VIDEO_PLAYER_FUNC(void, initJni) {
    libDefine=new NativeLibDefine();
    libDefine->env = env;
    libDefine->ffmpegDecoder = env->GetObjectClass(thiz);
    if (!libDefine->ffmpegDecoder) {
        LOGE("找不到类:%s", decoderClassName);
        return;
    }
    libDefine->playStatusCallback = env->GetMethodID(libDefine->ffmpegDecoder,
                                                     "jniPlayStatusCallback", "(I)V");
    if (!libDefine->playStatusCallback) {
        LOGE("找不到方法:jniPlayStatusCallback(int)");
        return;
    }
    libDefine->errorCallback = env->GetMethodID(libDefine->ffmpegDecoder, "jniErrorCallback",
                                                "(ILjava/lang/String;)V");
    if (!libDefine->errorCallback) {
        LOGE("找不到方法:jniErrorCallback(int,String)");
        return;
    }
}

VIDEO_PLAYER_FUNC(int, playVideo, jstring vPath, jobject surface) {
    const char *videoPath = env->GetStringUTFChars(vPath, nullptr);
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
    if (nativeWindow == nullptr) {
        LOGE("Could not get native window from surface");
        return -1;
    }
    nativePlayer.playVideo(videoPath, nativeWindow);
    env->ReleaseStringUTFChars(vPath, videoPath);
    return 0;
}

VIDEO_PLAYER_FUNC(long long, getCurrentPosition) {
    return nativePlayer.getPlayProgress(0);
}
VIDEO_PLAYER_FUNC(long long, getDuration) {
    return nativePlayer.getPlayProgress(1);
}

VIDEO_PLAYER_FUNC(void, goSelectedTime, jint t) {
    return nativePlayer.seekTo(t);
}

VIDEO_PLAYER_FUNC(bool , mIsPlaying) {
    return nativePlayer.getPlayStatus() == 1;
}

VIDEO_PLAYER_FUNC(void, setPlayState, jint status) {
    nativePlayer.setPlayStatus(status);
}
}