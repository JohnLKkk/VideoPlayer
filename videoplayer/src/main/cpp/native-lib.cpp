
#include "native_lib_define.h"

extern "C" {
JNIEXPORT jstring
Java_com_yoy_videoplayer_processing_decoder_VideoFFMPEGDecoder_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
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

VIDEO_PLAYER_FUNC(void, mDestroy) {
    NativePlayer nativePlayer;
    nativePlayer.mDestroy();
}

}
