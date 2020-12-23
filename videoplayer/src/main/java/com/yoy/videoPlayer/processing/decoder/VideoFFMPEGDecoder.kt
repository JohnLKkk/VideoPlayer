package com.yoy.videoPlayer.processing.decoder

import android.text.TextUtils
import android.view.SurfaceHolder
import com.yoy.v_Base.utils.LogUtils
import com.yoy.videoPlayer.processing.FileAttributes

/**
 * Created by Void on 2020/9/9 20:20
 *
 */
class VideoFFMPEGDecoder(private val callback: PlayStateCallback) : VideoDecoder() {
    private val TAG = "FFMPEGDecoder"
    private var holder: SurfaceHolder? = null
    private val decoderJni = FFMPEGDecoderJni(this)
    private var vPath = ""

    init {
        decoderJni.initJni()
    }

    override fun setDisPlay(holder: SurfaceHolder?, fileInfo: FileAttributes) {
        this.holder = holder
        if (!fileInfo.isValid || holder == null) return
        if (!TextUtils.isEmpty(vPath)) setDataSource(vPath)
    }

    override fun setDataSource(path: String) {
        vPath = path
        if (holder == null) return
        Thread {
            decoderJni.playVideo(path, holder!!.surface)
        }.start()
    }

    override fun start() {
        decoderJni.setPlayState(1)
    }

    override fun pause() {
        decoderJni.setPlayState(2)
    }

    override fun seekTo(time: Int) {
        decoderJni.goSelectedTime(time)
    }

    override fun isPlaying(): Boolean = decoderJni.mIsPlaying()

    override fun getPlayTimeIndex(type: Int): Long = if (type == 1) {
        decoderJni.getCurrentPosition()
    } else {
        decoderJni.getDuration()
    }

    override fun release() {
        decoderJni.setPlayState(5)
        holder = null
        callback.onCompletion()
    }

    fun setFilter(value: String) {
        if (TextUtils.isEmpty(value)) return
        LogUtils.w(msg = "设置滤镜：$value")
        decoderJni.setFilter(value)
    }

    /**
     * c层播放状态回调
     * 注，不要更改方法名和参数类型！
     * @param status 0=Prepared
     */
    fun onPlayStatusCallback(status: Int) {
        LogUtils.i(TAG, "jniPlayStatusCallback; status:$status")
        when (status) {
            0 -> callback.onPrepared()
        }
    }

    /**
     * c层错误回调
     * 注，不要更改方法名和参数类型！
     * 详细请查看错误定义文件：src/main/cpp/ErrorCodeDefine.h
     * @param errorCode
     */
    fun onErrorCallback(errorCode: Int, msg: String) {
        LogUtils.i(TAG, "jniErrorCallback$msg")
        when (errorCode) {

        }
    }
}