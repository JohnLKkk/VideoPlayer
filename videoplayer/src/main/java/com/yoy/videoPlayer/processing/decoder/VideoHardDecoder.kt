package com.yoy.videoPlayer.processing.decoder

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import android.view.SurfaceHolder
import com.yoy.videoPlayer.processing.FileAttributes
import java.util.*

/**
 * Created by Void on 2020/9/9 20:20
 *
 */
class VideoHardDecoder(callback: PlayStateCallback) : VideoDecoder(), MediaPlayer.OnErrorListener {
    private val mediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder().apply {
            this.setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        }.build())
        mediaPlayer.setOnPreparedListener { callback.onPrepared() }
        mediaPlayer.setOnCompletionListener { callback.onCompletion() }
        mediaPlayer.setOnErrorListener(this)
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        val msg = when (extra) {
            MediaPlayer.MEDIA_ERROR_IO -> "网络异常/没有文件读写权限"
            MediaPlayer.MEDIA_ERROR_MALFORMED -> "比特流不符合相关的编码标准或文件规范"
            MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> "比特流符合相关的编码标准或文件规范，但媒体框架不支持该功能"
            MediaPlayer.MEDIA_ERROR_TIMED_OUT -> "操作超时"
            else -> String.format(Locale.CHINA, "未知错误(%d,%d)", what, extra)
        }
        Log.e("VideoHardDecoder", msg)
        return false
    }

    override fun setDisPlay(holder: SurfaceHolder?, fileInfo: FileAttributes) {
        try {
//            KLog.e("----setDisPlay")
            mediaPlayer.setDisplay(holder)
            if (fileInfo.isValid) {
                mediaPlayer.prepare()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setDataSource(path: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun start() {
        if (isPlaying() || isRelease) return
        try {
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun pause() {
        if (!isPlaying() || isRelease) return
        try {
            mediaPlayer.pause()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun seekTo(time: Int) {
        if (isRelease) return
        mediaPlayer.seekTo(time)
    }

    override fun release() {
        if (isPlaying()) mediaPlayer.stop()
        mediaPlayer.release()
        isRelease = true
    }

    override fun isPlaying(): Boolean {
        if (isRelease) return false
        return mediaPlayer.isPlaying
    }

    override fun getPlayTimeIndex(type: Int): Long {
        if (isRelease) return 0L
        return when (type) {
            1 -> mediaPlayer.currentPosition.toLong()
            2 -> mediaPlayer.duration.toLong()
            else -> -1L
        }
    }
}