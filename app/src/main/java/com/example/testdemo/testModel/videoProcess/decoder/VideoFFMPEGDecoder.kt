package com.example.testdemo.testModel.videoProcess.decoder

import android.view.SurfaceHolder
import com.example.testdemo.testModel.videoProcess.FileAttributes

/**
 * Created by Void on 2020/9/9 20:20
 *
 */
class VideoFFMPEGDecoder(callback: PlayStateCallback) : VideoDecoder() {
    companion object {
        init {
            System.loadLibrary("media-handle")
        }
    }

    external fun play(filePath: String, surface: Any): Int

    external fun stringFromJNI(): String

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

    override fun getPlayTimeIndex(type: Int): Int {
        return 0
    }

    override fun release() {
    }
}