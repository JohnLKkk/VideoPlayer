package com.example.testdemo.testModel.videoProcess

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SurfaceHolder
import com.example.testdemo.testModel.videoProcess.decoder.PlayStateCallback
import com.example.testdemo.testModel.videoProcess.decoder.VideoDecoder
import com.example.testdemo.testModel.videoProcess.decoder.VideoHardHandler
import com.example.testdemo.testModel.videoProcess.decoder.VideoSoftHandler

/**
 * Created by Void on 2020/8/17 18:02
 * 播放助手
 */
class PlayVideoHandler(private val playStateListener: PlayStateListener?) : PlayStateCallback,SurfaceHolder.Callback {
    private var surfaceHolder: SurfaceHolder? = null
    private var listenerThread: ListenerPlayTime? = null
    private var sDecoder = VideoSoftHandler(this)
    private var hDecoder = VideoHardHandler(this)
    private var isReady = false
    val fileInfo = FileAttributes()

    init {
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        getDecoderHandler().setDisPlay(holder,fileInfo)
    }

    fun setSurfaceHolder(holder: SurfaceHolder) {
        holder.addCallback(this)
    }

    fun getDecoderHandler(): VideoDecoder = if (true) {
        sDecoder
    } else {
        hDecoder
    }

    /**
     * 播放准备
     */
    fun setDataPath(path: String) {
        fileInfo.initData(path)
        getDecoderHandler().setDataSource(path)
    }

    @Synchronized
    fun start() {
        getDecoderHandler().start()
    }

    @Synchronized
    fun pause() {
        playStateListener?.onPlayPaused()
        getDecoderHandler().pause()
    }

    @Synchronized
    fun seekTo(time: Int) {
        getDecoderHandler().seekTo(time)
    }

    @Synchronized
    fun isPlaying(): Boolean = mediaPlayer.isPlaying

    @Synchronized
    fun getCurrentTime(): Int = mediaPlayer.currentPosition

    @Synchronized
    fun getMaxTime(): Int = mediaPlayer.duration

    fun release() {
        isReady = false
        mediaPlayer.stop()
        mediaPlayer.release()
        surfaceHolder?.removeCallback(this)

    }

    inner class ListenerPlayTime : Thread() {
        override fun run() {
            super.run()
            while (isReady) {
                if (playStateListener == null) return
                playStateListener.onPlayTime(getCurrentTime())
                sleep(500)
            }
        }
    }

    override fun onPrepared() {
        start()
        isReady = true
        if (listenerThread == null) {
            listenerThread = ListenerPlayTime()
            listenerThread?.start()
        }
        playStateListener?.onPlayStart()
    }

    override fun onCompletion() {
        playStateListener?.onPlayEnd()
    }
}