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
<<<<<<< HEAD:app/src/main/java/com/example/testdemo/testModel/videoProcess/decoder/VideoHardDecoder.kt
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
=======

class VideoSoftHandler(callback: PlayStateCallback) : VideoDecoder() {
    companion object {
        init {
            System.loadLibrary("media-handle")
        }
    }
//    external fun play(filePath: String, surface: Any): Int
>>>>>>> bbb9dddb01896cb9d07394470f57fe532c3c48aa:app/src/main/java/com/example/testdemo/testModel/videoProcess/decoder/VideoSoftHandler.kt

    override fun setDisPlay(holder: SurfaceHolder?, fileInfo: FileAttributes) {
    }

    override fun setDataSource(path: String) {
    }

    override fun start() {
    }

    override fun pause() {
    }

    override fun seekTo(time: Int) {
    }

<<<<<<< HEAD:app/src/main/java/com/example/testdemo/testModel/videoProcess/decoder/VideoHardDecoder.kt
    override fun release() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean = mediaPlayer.isPlaying

    override fun getPlayTimeIndex(type: Int): Int = when (type) {
        1 -> mediaPlayer.currentPosition
        2 -> mediaPlayer.duration
        else -> -1
=======
    override fun isPlaying(): Boolean {
        return false
    }

    override fun getPlayTimeIndex(type: Int) :Int{
        return 0
    }

    override fun release() {
        TODO("Not yet implemented")
>>>>>>> bbb9dddb01896cb9d07394470f57fe532c3c48aa:app/src/main/java/com/example/testdemo/testModel/videoProcess/decoder/VideoSoftHandler.kt
    }
}