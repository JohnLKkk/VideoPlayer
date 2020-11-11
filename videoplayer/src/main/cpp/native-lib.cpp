#include <jni.h>
#include <string>
#include <iostream>
#include <cstdlib>
#include "default_code.h"

extern "C" JNIEXPORT jstring
Java_com_example_testdemo_testModel_onClickTest_ClickActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    throw hello;
    return env->NewStringUTF(hello.c_str());
}
extern "C" JNIEXPORT jstring
Java_com_yoy_videoplayer_processing_decoder_VideoFFMPEGDecoder_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
