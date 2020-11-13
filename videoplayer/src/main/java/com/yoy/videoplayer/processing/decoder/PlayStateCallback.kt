package com.yoy.videoplayer.processing.decoder

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

    /**
     * 播放取消、终止
     */
    fun onPlayCancel()
}