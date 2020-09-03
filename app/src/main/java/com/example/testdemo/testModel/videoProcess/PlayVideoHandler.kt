package com.example.testdemo.testModel.videoProcess

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SurfaceHolder
import com.example.testdemo.utlis.KLog

/**
 * Created by Void on 2020/8/17 18:02
 * 播放助手
 */
class PlayVideoHandler(private val playStateListener: PlayStateListener?) : SurfaceHolder.Callback {
    private val mediaPlayer = MediaPlayer()
    private var surfaceHolder: SurfaceHolder? = null
    private var listenerThread: ListenerPlayTime? = null
    private var isReady = false
    val fileInfo = FileAttributes()

    init {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder().apply {
            this.setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        }.build())
        mediaPlayer.setOnPreparedListener {
            start()
            isReady = true
            if (listenerThread == null) {
                listenerThread = ListenerPlayTime()
                listenerThread?.start()
            }
            playStateListener?.onPlayStart()
        }
        mediaPlayer.setOnCompletionListener {
            playStateListener?.onPlayEnd()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (fileInfo.isValid) {
            mediaPlayer.setDisplay(holder)
            mediaPlayer.prepare()
        }
    }

    fun setSurfaceHolder(holder: SurfaceHolder) {
        holder.addCallback(this)
    }

    /**
     * 播放准备
     */
    fun setDataPath(path: String) {
        fileInfo.initData(path)
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun start() {
        if (isPlaying()) return
        try {
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun pause() {
        playStateListener?.onPlayPaused()
        if (!isPlaying()) return
        try {
            mediaPlayer.pause()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun isPlaying(): Boolean = mediaPlayer.isPlaying

    @Synchronized
    fun getCurrentTime(): Int = mediaPlayer.currentPosition

    @Synchronized
    fun getMaxTime(): Int = mediaPlayer.duration

    fun release() {
        isReady = false
        mediaPlayer.stop()
        mediaPlayer.release()
        surfaceHolder?.removeCallback(this)
    }

    inner class ListenerPlayTime : Thread() {
        override fun run() {
            super.run()
            while (isReady) {
                if (playStateListener == null) return
                playStateListener.onPlayTime(getCurrentTime())
                sleep(500)
            }
        }
    }

//    interface PlayStateListener {
//
//        fun onPlayTimeListener()
//    }

}