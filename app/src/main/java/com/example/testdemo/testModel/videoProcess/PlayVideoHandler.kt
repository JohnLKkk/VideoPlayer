package com.example.testdemo.testModel.videoProcess

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SurfaceHolder
import java.io.File

/**
 * Created by Void on 2020/8/17 18:02
 * 播放助手
 */
class PlayVideoHandler : SurfaceHolder.Callback {
    private val mediaPlayer = MediaPlayer()
    private var surfaceHolder: SurfaceHolder? = null
    private val fileInfo=FileAttributes()
    var playFile: File? = null

    init {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder().apply {
            this.setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        }.build())
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mediaPlayer.setDisplay(holder)
    }

    fun setSurfaceHolder(holder: SurfaceHolder) {
        this.surfaceHolder = holder
        holder.addCallback(this)
    }

    fun prepare(path: String) {
        playFile = File(path)
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun start() {
        if (isPlaying()) return
        try {
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        if (!isPlaying()) return
        try {
            mediaPlayer.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isPlaying(): Boolean = mediaPlayer.isPlaying

    fun release() {
        mediaPlayer.release()
        surfaceHolder?.removeCallback(this)
    }
}