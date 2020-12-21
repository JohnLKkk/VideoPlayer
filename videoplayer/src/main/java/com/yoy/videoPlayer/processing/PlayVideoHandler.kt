package com.yoy.videoPlayer.processing

import android.os.Handler
import android.os.Looper
import android.view.SurfaceHolder
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.LogUtils
import com.yoy.videoPlayer.processing.decoder.PlayStateCallback
import com.yoy.videoPlayer.processing.decoder.VideoDecoder
import com.yoy.videoPlayer.processing.decoder.VideoFFMPEGDecoder
import com.yoy.videoPlayer.processing.decoder.VideoHardDecoder

/**
 * Created by Void on 2020/8/17 18:02
 * 播放助手
 */
class PlayVideoHandler : PlayStateCallback, SurfaceHolder.Callback {
    private val mHandler = Handler(Looper.getMainLooper())
    private var surfaceHolder: SurfaceHolder? = null
    private var listenerThread: ListenerPlayTimeThread? = null
    private var sDecoder = VideoFFMPEGDecoder(this)
    private var hDecoder = VideoHardDecoder(this)
    private var playStateListener: PlayStateListener? = null
    private var decoderType = DecodeType.HARDDecoder
    private var isReadyPlay = false
    val fileInfo = FileAttributes()

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        LogUtils.e(msg = "PlayVideoHandler--surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        LogUtils.e(msg = "PlayVideoHandler--surfaceDestroyed")
        getDecoderHandler()?.setDisPlay(null, fileInfo)
        stopTimeUpdateThread()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        LogUtils.e(msg = "PlayVideoHandler--surfaceCreated")
        getDecoderHandler()?.setDisPlay(holder, fileInfo)
    }

    override fun onPrepared() {
        LogUtils.i(msg = "播放状态：onPrepared")
        isReadyPlay = true
        plStart()
        if (listenerThread == null) {
            listenerThread = ListenerPlayTimeThread()
            listenerThread?.start()
        }
        listenerThread?.isStop = false
        playStateListener?.onPlayStart()
    }

    override fun onCompletion() {
        playStateListener?.onPlayEnd()
    }

    override fun onPlayCancel() {
    }

    private fun getDecoderHandler(): VideoDecoder? = when (decoderType) {
        DecodeType.FFMPEGDecoder -> sDecoder
        DecodeType.HARDDecoder -> hDecoder
        else -> {
            LogUtils.e("PlayVideoHandler", "未知的解码类型")
            null
        }
    }

    fun setPlayStateListener(listener: PlayStateListener) {
        this.playStateListener = listener
    }

    /**
     * 是否准备好播放
     */
    fun isReadyPlay() = isReadyPlay

    fun setDecoderType(decoder: DecodeType) {
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
        listenerThread?.isStop = true
        getDecoderHandler()?.setDataSource(path)
    }

    fun release() {
        getDecoderHandler()?.release()
        surfaceHolder?.removeCallback(this)
        stopTimeUpdateThread()
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
    fun isPlaying(): Boolean = getDecoderHandler()?.isPlaying() ?: false

    @Synchronized
    fun getCurrentTime(): Long = getDecoderHandler()?.getPlayTimeIndex(1) ?: 0L

    @Synchronized
    fun getMaxTime(): Long = getDecoderHandler()?.getPlayTimeIndex(2) ?: 0L
    //endregion

    /**
     * 时间戳转换为当前播放进度(百分比)
     */
    fun timestampToProgress(): Int = timestampToProgress(getCurrentTime())

    fun timestampToProgress(time: Long): Int {
        val max = getMaxTime()
        if (max <= 0 || time <= 0) return 0
        return (time * 100 / max).toInt()
    }

    /**
     * 播放进度(百分比)转换为具体的时间戳
     * @param bfb 当前进度，如 50%=播放的一半
     */
    fun progressToTimestamp(bfb: Int): Long {
        val max = getMaxTime()
        if (max <= 0 || bfb <= 0) return 0
        return (bfb / 100.0 * max).toLong()
    }

    //region -----播放时间更新线程控制-----
    fun stopTimeUpdateThread() {
        isReadyPlay = false
        listenerThread?.isStop = true
        listenerThread = null
    }
    fun setFilterValue(str:String){

    }

    /**
     * 播放时间更新线程
     */
    inner class ListenerPlayTimeThread : Thread() {
        var isStop = false
        override fun run() {
            super.run()
            while (isReadyPlay) {
                if (isStop || !isPlaying()) {
                    sleep(500)
                    continue
                }
                mHandler.post { playStateListener?.onPlayTime(getCurrentTime()) }
                sleep(500)
            }
        }
    }
    //endregion
}