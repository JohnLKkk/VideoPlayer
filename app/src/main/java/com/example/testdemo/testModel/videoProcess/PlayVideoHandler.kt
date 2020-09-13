package com.example.testdemo.testModel.videoProcess

import android.view.SurfaceHolder
import com.example.testdemo.testModel.videoProcess.decoder.PlayStateCallback
import com.example.testdemo.testModel.videoProcess.decoder.VideoDecoder
import com.example.testdemo.testModel.videoProcess.decoder.VideoHardHandler
import com.example.testdemo.testModel.videoProcess.decoder.VideoSoftHandler
import com.example.testdemo.utlis.KLog

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
        hDecoder
    } else {
        sDecoder
    }

    /**
     * 播放准备
     */
    fun setDataPath(path: String) {
        if (!fileInfo.initData(path)){
            KLog.e("文件信息初始化失败！")
            return
        }
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
    fun isPlaying(): Boolean = getDecoderHandler().isPlaying()

    @Synchronized
    fun getCurrentTime(): Int =  getDecoderHandler().getPlayTimeIndex(1)

    @Synchronized
    fun getMaxTime(): Int =  getDecoderHandler().getPlayTimeIndex(2)

    fun release() {
        isReady = false
        getDecoderHandler().release()
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