package com.yoy.videoPlayer.processing.decoder

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.text.TextUtils
import android.view.SurfaceHolder
import com.yoy.videoPlayer.processing.FileAttributes

/**
 * Created by Void on 2020/9/9 20:20
 *
 */
class VideoFFMPEGDecoder(private val callback: PlayStateCallback) : VideoDecoder() {

    private var holder: SurfaceHolder? = null

    private var vPath = ""

    override fun setDisPlay(holder: SurfaceHolder?, fileInfo: FileAttributes) {
        if (!fileInfo.isValid) return
        this.holder = holder
        if (!TextUtils.isEmpty(vPath)) setDataSource(vPath)
    }

    override fun setDataSource(path: String) {
        vPath = path
        if (holder == null) return
        Thread {
            try {
                playVideo(path, holder!!.surface)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun start() {

    }

    override fun pause() {
    }

    override fun seekTo(time: Int) {
    }

    override fun isPlaying(): Boolean {
        return false
    }

    override fun getPlayTimeIndex(type: Int): Long {
        return 0L
    }

    override fun release() {
        holder = null
        mDestroy()
        callback.onCompletion()
    }

    /**
     * 创建音轨
     *
     * @param sampleRate 采样率
     * @param channels   频道
     */
    fun createAudioTrack(sampleRate: Int, channels: Int): AudioTrack? {
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val channelConfig: Int = when (channels) {
            1 -> AudioFormat.CHANNEL_OUT_MONO
            2 -> AudioFormat.CHANNEL_OUT_STEREO
            else -> AudioFormat.CHANNEL_OUT_STEREO
        }
        val bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return AudioTrack.Builder()
                    .setAudioAttributes(AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build())
                    .setAudioFormat(AudioFormat.Builder()
                            .setEncoding(audioFormat)
                            .setSampleRate(sampleRate)
                            .setChannelMask(channelConfig)
                            .build())
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .setBufferSizeInBytes(bufferSizeInBytes)
                    .build()
        } else {
            @Suppress("DEPRECATION")
            return AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, audioFormat,
                    bufferSizeInBytes, AudioTrack.MODE_STREAM)
        }
    }

    //region  ------------ffmpeg decoder
    private external fun stringFromJNI(): String

    private external fun playVideo(vPath: String, surface: Any): Int

    private external fun mDestroy()
    //endregion

    companion object {
        init {
            System.loadLibrary("media-handle")
        }
    }
}