package com.yoy.videoplayer.processing

import android.view.SurfaceHolder
import com.yoy.v_Base.utils.AppCode
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.SPUtils
import com.yoy.videoplayer.processing.decoder.PlayStateCallback
import com.yoy.videoplayer.processing.decoder.VideoDecoder
import com.yoy.videoplayer.processing.decoder.VideoFFMPEGDecoder
import com.yoy.videoplayer.processing.decoder.VideoHardDecoder

/**
 * Created by Void on 2020/8/17 18:02
 * 播放助手
 */
class PlayVideoHandler(private val playStateListener: PlayStateListener?) :
        PlayStateCallback,
        SurfaceHolder.Callback {
    private var surfaceHolder: SurfaceHolder? = null
    private var listenerThread: ListenerPlayTime? = null
    private var sDecoder = VideoFFMPEGDecoder(this)
    private var hDecoder = VideoHardDecoder(this)
    private var decoderType = DecodeType.HARDDecoder
    private var isReady = false
    val fileInfo = FileAttributes()

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        getDecoderHandler().setDisPlay(holder, fileInfo)
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

    private fun getDecoderHandler(): VideoDecoder = when (decoderType) {
        DecodeType.FFMPEGDecoder -> sDecoder
        DecodeType.HARDDecoder -> hDecoder
        else -> hDecoder
    }

    fun setDecoderType(decoder: DecodeType) {
        SPUtils.saveString(AppCode.currentDecodeType,decoder.toString())
        this.decoderType = decoder
    }

    /**
     * 播放准备
     */
    fun setDataPath(path: String) {
        if (!fileInfo.initData(path)) {
            KLog.e("文件信息初始化失败！path:$path")
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
    fun getCurrentTime(): Int = getDecoderHandler().getPlayTimeIndex(1)

    @Synchronized
    fun getMaxTime(): Int = getDecoderHandler().getPlayTimeIndex(2)

    fun release() {
        isReady = false
        getDecoderHandler().release()
        surfaceHolder?.removeCallback(this)
        if (listenerThread?.isInterrupted == true) {
            listenerThread?.interrupt()
        }
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
}