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
class VideoSoftHandler(callback: PlayStateCallback) : VideoDecoder() {
    private val mediaPlayer = MediaPlayer()
    init {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder().apply {
            this.setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        }.build())
        mediaPlayer.setOnPreparedListener { callback.onPrepared() }
        mediaPlayer.setOnCompletionListener { callback.onCompletion() }
    }

    override fun getDecoderHandler(): VideoDecoder = this

    override fun setDisPlay(holder: SurfaceHolder?, fileInfo: FileAttributes) {
        if (fileInfo.isValid) {
            mediaPlayer.setDisplay(holder)
            mediaPlayer.prepare()
        }
    }

    override fun setDataSource(path: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(path)
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

    override fun release() {
        TODO("Not yet implemented")
    }

    override fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlayTimeIndex(type: Int) {
        TODO("Not yet implemented")
    }
}