package com.example.testdemo.testModel.videoProcess

/**
 * Created by Void on 2020/9/3 14:43
 * 解码类型
 */
enum class DecodeType {
    HARDDecoder,//硬解码
    FFMPEGDecoder, //ffmpeg解码
    OTHER //不存在的解码类型
}