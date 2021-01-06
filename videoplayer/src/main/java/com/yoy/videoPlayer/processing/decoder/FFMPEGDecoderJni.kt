package com.yoy.videoPlayer.processing.decoder

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import com.yoy.v_Base.utils.LogUtils

/**
 * Created by Void on 2020/12/17 15:56
 *
 */
class FFMPEGDecoderJni(private val decoder: VideoFFMPEGDecoder) {
    private val TAG = "FFMPEG_decoder_jni"

    /**
     * c层播放状态回调
     * 注，不要更改方法名和参数类型！
     * @param status 0=Prepared
     */
    fun jniPlayStatusCallback(status: Int) {
//        LogUtils.i(TAG, "jniPlayStatusCallback; status:$status")
        decoder.onPlayStatusCallback(status)
    }

    /**
     * c层错误回调
     * 注，不要更改方法名和参数类型！
     * 详细请查看错误定义文件：src/main/cpp/ErrorCodeDefine.h
     * @param errorCode
     */
    fun jniErrorCallback(errorCode: Int, msg: String) {
        decoder.onErrorCallback(errorCode, msg)
    }

    /**
     * 创建音轨
     *
     * @param sampleRate 采样率
     * @param channels   频道
     */
    private fun createAudioTrack(sampleRate: Int, channels: Int): AudioTrack? {
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

    external fun initJni()

    external fun setDataSource(vPath: String, surface: Any): Int

    external fun getCurrentPosition(): Long

    external fun getDuration(): Long

    external fun goSelectedTime(t: Int)

    external fun mIsPlaying(): Boolean

    external fun setPlayState(status: Int)

    external fun setFilter(value: String)

    companion object {
        init {
            System.loadLibrary("media-handle")
        }
    }
}