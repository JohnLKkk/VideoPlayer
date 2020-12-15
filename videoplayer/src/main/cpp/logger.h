//
// Created by Void on 2020/11/30.
//

#ifndef TESTEXAMPLE_LOGGER_H
#define TESTEXAMPLE_LOGGER_H

#define println(A,...) printf(join1(A, "\n"),__VA_ARGS__)

#ifdef ANDROID

#include <android/log.h>
#include <libavutil/time.h>

#define LOG_TAG    "NativePlayer"
#define LOGE(format, ...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, format, ##__VA_ARGS__)
#define LOGI(format, ...)  __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, format, ##__VA_ARGS__)
#else
#define LOGE(format, ...)  println2(LOG_TAG format, ##__VA_ARGS__)
#define LOGI(format, ...)  println2(LOG_TAG format, ##__VA_ARGS__)
#endif

#endif //TESTEXAMPLE_LOGGER_H
