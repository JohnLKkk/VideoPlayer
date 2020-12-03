package com.yoy.videoPlayer.processing.decoder

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SurfaceHolder
import com.yoy.videoPlayer.processing.FileAttributes

/**
 * Created by Void on 2020/9/9 20:20
 *
 */
class VideoHardDecoder(callback: PlayStateCallback) : VideoDecoder() {
    private val mediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder().apply {
            this.setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        }.build())
        mediaPlayer.setOnPreparedListener { callback.onPrepared() }
        mediaPlayer.setOnCompletionListener { callback.onCompletion() }
    }

    override fun setDisPlay(holder: SurfaceHolder?, fileInfo: FileAttributes) {
        try {
            if (fileInfo.isValid) {
                mediaPlayer.setDisplay(holder)
                mediaPlayer.prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setDataSource(path: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepareAsync()
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

    override fun getPlayTimeIndex(type: Int): Int {
        if (isRelease) return 0
        return when (type) {
            1 -> mediaPlayer.currentPosition
            2 -> mediaPlayer.duration
            else -> -1
        }
    }
}