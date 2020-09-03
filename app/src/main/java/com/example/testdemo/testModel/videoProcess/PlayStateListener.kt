package com.example.testdemo.testModel.videoProcess

/**
 * Created by Void on 2020/8/25 17:00
 * 视频播放状态监听
 */
interface PlayStateListener {
    fun onPlayStart()
    fun onPlayPaused()
    fun onPlayStop()
    fun onPlayEnd()
    fun onPlayRelease()
    fun onPlayTime(time:Int)
}