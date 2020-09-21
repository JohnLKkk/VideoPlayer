package com.example.testdemo.testModel.videoProcess.decoder

import android.view.SurfaceHolder
import com.example.testdemo.App
import com.example.testdemo.R
import com.example.testdemo.testModel.videoProcess.FileAttributes

/**
 * Created by Void on 2020/9/9 20:20
 *
 */
class VideoFFMPEGDecoder(callback: PlayStateCallback) : VideoDecoder() {

    private var holder: SurfaceHolder? = null

    override fun setDisPlay(holder: SurfaceHolder?, fileInfo: FileAttributes) {
        if (fileInfo.isValid) {
            this.holder = holder
        }
    }

    override fun setDataSource(path: String) {
        if (holder == null) return
        Thread {
            try {
                filter(path, holder!!.surface, filters[5])
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
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
        destroy()
    }

    //region  ------------ffmpeg decoder
    private external fun play(filePath: String, surface: Any): Int

    private external fun setPlayRate(playRate: Float)

    private external fun filter(filePath: String, surface: Any, filterType: String): Int

    private external fun again()

    private external fun destroy()

    private external fun playAudio(play: Boolean)

    private external fun stringFromJNI(): String
    //endregion

    companion object {
        init {
            System.loadLibrary("media-handle")
        }
    }
}