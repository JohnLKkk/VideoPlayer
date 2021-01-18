package com.yoy.videoPlayer.processing

/**
 * Created by Void on 2020/9/3 14:43
 * 解码类型
 * @param code 该类型的string格式
 * @param cn 改类型用于展示给用户看的所对应的文本
 */
enum class DecodeType(private var code: String, val cn: String) {
    HARDDecoder("HARDDecoder", "硬解码"),//硬解码
    FFMPEGDecoder("FFMPEGDecoder", "FFMPEG"); //ffmpeg解码

    override fun toString(): String = code
}