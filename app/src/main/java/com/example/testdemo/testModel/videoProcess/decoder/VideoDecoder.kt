package com.example.testdemo.testModel.videoProcess.decoder

import android.view.SurfaceHolder
import com.example.testdemo.testModel.videoProcess.FileAttributes

/**
 * Created by Void on 2020/9/10 14:34
 *
 */
abstract class VideoDecoder {
    abstract fun setDisPlay(holder: SurfaceHolder?, fileInfo: FileAttributes)

    abstract fun setDataSource(path: String)
    abstract fun start()
    abstract fun pause()
    abstract fun seekTo(time: Int)
    abstract fun release()

    /**
     * 播放状态
     * @return true播放中
     */
    abstract fun isPlaying(): Boolean

    /**
     * 获取播放时间数据
     * @param type 1当前播放进度 2最大播放长度
     */
    abstract fun getPlayTimeIndex(type: Int):Int

}