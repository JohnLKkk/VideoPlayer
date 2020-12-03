package com.yoy.videoPlayer.processing

import android.view.SurfaceHolder
import com.yoy.v_Base.utils.AppCode
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.SPUtils
import com.yoy.videoPlayer.processing.decoder.PlayStateCallback
import com.yoy.videoPlayer.processing.decoder.VideoDecoder
import com.yoy.videoPlayer.processing.decoder.VideoFFMPEGDecoder
import com.yoy.videoPlayer.processing.decoder.VideoHardDecoder

/**
 * Created by Void on 2020/8/17 18:02
 * 播放助手
 */
class PlayVideoHandler(private val playStateListener: PlayStateListener?) :
        PlayStateCallback,
        SurfaceHolder.Callback {
    private var surfaceHolder: SurfaceHolder? = null
    private var listenerThread: ListenerPlayTime? = null
    private var sDecoder: VideoFFMPEGDecoder? = null
    private var hDecoder: VideoHardDecoder? = null
    private var decoderType = DecodeType.HARDDecoder
    private var isReady = false
    val fileInfo = FileAttributes()

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        getDecoderHandler()?.setDisPlay(holder, fileInfo)
    }

    override fun onPrepared() {
        plStart()
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

    override fun onPlayCancel() {
    }

    private fun getDecoderHandler(): VideoDecoder? = when (decoderType) {
        DecodeType.FFMPEGDecoder -> {
            if (sDecoder == null) {
                sDecoder = VideoFFMPEGDecoder(this)
            }
            sDecoder
        }
        DecodeType.HARDDecoder -> {
            if (hDecoder == null) {
                hDecoder = VideoHardDecoder(this)
            }
            hDecoder
        }
        else -> null
    }

    fun setDecoderType(decoder: DecodeType) {
        SPUtils.saveString(AppCode.currentDecodeType, decoder.toString())
        this.decoderType = decoder
    }

    /**
     * 播放准备
     */
    fun setDataPath(path: String) {
        if (!fileInfo.initData(path)) {
            KLog.e("文件信息初始化失败,请检查文件是否有效！path:$path")
            return
        }
        getDecoderHandler()?.setDataSource(path)
    }

    fun release() {
        isReady = false
        getDecoderHandler()?.release()
        surfaceHolder?.removeCallback(this)
        if (listenerThread?.isInterrupted == true) {
            listenerThread?.interrupt()
        }
    }

    //region 播放控制或播放信息获取
    @Synchronized
    fun plStart() {
        getDecoderHandler()?.start()
    }

    @Synchronized
    fun plPause() {
        playStateListener?.onPlayPaused()
        getDecoderHandler()?.pause()
    }

    @Synchronized
    fun seekTo(time: Int) {
        getDecoderHandler()?.seekTo(time)
    }

    @Synchronized
    fun isPlaying(): Boolean = getDecoderHandler()?.isPlaying()?:false

    @Synchronized
    fun getCurrentTime(): Int = getDecoderHandler()?.getPlayTimeIndex(1)?:0

    @Synchronized
    fun getMaxTime(): Int = getDecoderHandler()?.getPlayTimeIndex(2)?:0
    //endregion

    /**
     * 播放时间更新线程
     */
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