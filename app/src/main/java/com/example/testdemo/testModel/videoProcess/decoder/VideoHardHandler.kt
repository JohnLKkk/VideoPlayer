package com.example.testdemo.testModel.videoProcess.decoder

import android.view.SurfaceHolder
import com.example.testdemo.testModel.videoProcess.FileAttributes

/**
 * Created by Void on 2020/9/9 20:20
 *
 */
class VideoHardHandler(callback: PlayStateCallback) : VideoDecoder() {
    companion object {
        init {
            System.loadLibrary("media-handle")
        }
    }
//    external fun play(filePath: String, surface: Any): Int

    override fun getDecoderHandler(): VideoDecoder {
        TODO("Not yet implemented")
    }

    override fun setDisPlay(holder: SurfaceHolder?, fileInfo: FileAttributes) {
        TODO("Not yet implemented")
    }

    override fun setDataSource(path: String) {
        TODO("Not yet implemented")
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun seekTo(time: Int) {
        TODO("Not yet implemented")
    }

    override fun isPlaying(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlayTimeIndex(type: Int) {
    }

    override fun release() {
        TODO("Not yet implemented")
    }
}