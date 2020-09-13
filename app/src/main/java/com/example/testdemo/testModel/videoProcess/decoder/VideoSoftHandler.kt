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
    companion object {
        init {
            System.loadLibrary("media-handle")
        }
    }
//    external fun play(filePath: String, surface: Any): Int

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

    override fun isPlaying(): Boolean {
        return false
    }

    override fun getPlayTimeIndex(type: Int) :Int{
        return 0
    }

    override fun release() {
        TODO("Not yet implemented")
    }
}