package com.example.testdemo.testModel.videoProcessing.decoder

/**
 * Created by Void on 2020/9/10 16:32
 *
 */
interface PlayStateCallback {
    /**
     * 播放准备
     */
    fun onPrepared()

    /**
     * 播放结束
     */
    fun onCompletion()
}