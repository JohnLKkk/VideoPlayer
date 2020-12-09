
#include "native_lib_define.h"

extern "C" {
VIDEO_PLAYER_FUNC(jstring, stringFromJNI) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
VIDEO_PLAYER_FUNC(int, playVideo, jstring vPath, jobject surface) {
    const char *videoPath = env->GetStringUTFChars(vPath, nullptr);
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env, surface);
    if (nativeWindow == nullptr) {
        LOGE("Could not get native window from surface");
        return -1;
    }
    NativePlayer nativePlayer;
    nativePlayer.playVideo(videoPath, nativeWindow);
    env->ReleaseStringUTFChars(vPath, videoPath);
    return 0;
}

VIDEO_PLAYER_FUNC(long long, getCurrentPosition) {
    NativePlayer nativePlayer;
    return nativePlayer.getPlayProgress(0);
}
VIDEO_PLAYER_FUNC(long long, getDuration) {
    NativePlayer nativePlayer;
    return nativePlayer.getPlayProgress(1);
}

VIDEO_PLAYER_FUNC(void, goSelectedTime, jint t) {
    NativePlayer nativePlayer;
    return nativePlayer.seekTo(t);
}

VIDEO_PLAYER_FUNC(int, isPlaying) {

}

VIDEO_PLAYER_FUNC(void, setPlayState, jint stater) {

}

VIDEO_PLAYER_FUNC(void, mDestroy) {
    NativePlayer nativePlayer;
    nativePlayer.mDestroy();
}
}