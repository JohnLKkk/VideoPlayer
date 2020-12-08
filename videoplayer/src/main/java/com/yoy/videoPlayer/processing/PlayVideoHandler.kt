package com.yoy.videoPlayer.processing

import android.os.Handler
import android.os.Looper
import android.view.SurfaceHolder
import com.yoy.v_Base.utils.AppCode
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.LogUtils
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
    private val mHandler = Handler(Looper.getMainLooper())
    private var surfaceHolder: SurfaceHolder? = null
    private var listenerThread: ListenerPlayTimeThread? = null
    private var sDecoder: VideoFFMPEGDecoder? = null
    private var hDecoder: VideoHardDecoder? = null
    private var decoderType = DecodeType.HARDDecoder
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
        if (listenerThread == null) {
            listenerThread = ListenerPlayTimeThread()
            listenerThread?.start()
        }
        listenerThread?.mStart()
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
        else -> {
            LogUtils.e(msg = "未知的解码类型")
            null
        }
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
        listenerThread?.mStop()
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
    fun getCurrentTime(): Long {
        var currentTime = getDecoderHandler()?.getPlayTimeIndex(1) ?: 0L
//        if (BuildConfig.DEBUG && currentTime == 0L) {
//            currentTime = 50000L
//        }
        return currentTime
    }

    @Synchronized
    fun getMaxTime(): Long {
        var maxTime = getDecoderHandler()?.getPlayTimeIndex(2) ?: 0L
//        if (BuildConfig.DEBUG && maxTime == 0L) {
//            maxTime = 100000L
//        }
        return maxTime
    }
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
        listenerThread?.release()
        listenerThread = null
    }

    /**
     * 播放时间更新线程
     */
    inner class ListenerPlayTimeThread : Thread() {
        private var isReady = true
        private var isStop = false
        override fun run() {
            super.run()
            while (isReady) {
                if (isStop) {
                    sleep(500)
                    continue
                }
                mHandler.post { playStateListener?.onPlayTime(getCurrentTime()) }
                sleep(500)
            }
        }

        fun mStart() {
            isStop = false
        }

        fun mStop() {
            isStop = true
        }

        fun release() {
            isStop = true
            isReady = false
        }
    }
    //endregion
}