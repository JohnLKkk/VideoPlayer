package com.example.testdemo.testModel.videoProcess.decoder

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SurfaceHolder
import com.example.testdemo.testModel.videoProcess.FileAttributes

/**
 * Created by Void on 2020/9/9 20:20
 *
 */
class VideoHardHandler(callback: PlayStateCallback) : VideoDecoder() {
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
//        if (fileInfo.isValid) {
            mediaPlayer.setDisplay(holder)
//        mediaPlayer.prepareAsync()
//        ｝
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
        if (isPlaying()) return
        try {
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun pause() {
        if (!isPlaying()) return
        try {
            mediaPlayer.pause()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun seekTo(time: Int) {
        mediaPlayer.seekTo(time)
    }

    override fun isPlaying(): Boolean =mediaPlayer.isPlaying

    override fun getPlayTimeIndex(type: Int): Int = when (type) {
        1 -> mediaPlayer.currentPosition
        2 -> mediaPlayer.duration
        else -> 0
    }

    override fun release() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}