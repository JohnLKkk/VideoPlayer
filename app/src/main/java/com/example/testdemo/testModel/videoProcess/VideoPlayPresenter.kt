package com.example.testdemo.testModel.videoProcess

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.text.TextUtils
import android.view.SurfaceHolder
import com.example.testdemo.utlis.KLog

/**
 * Created by Void on 2020/7/27 17:27
 *
 */
class VideoPlayPresenter(private val mActivity: VideoPlayActivity,
                         private val uiControl: VideoPlayUiControl) :
        VideoPlayActivity.SelectFile {
    private val mainHandler = Handler()
    val playHandler = PlayVideoHandler()

    private var videoPath = ""


    override fun selectCallback(path: String?) {
        if (path == null || path.isEmpty()) {
            KLog.e("视频文件路径为null！")
            return
        }
        videoPath = path
        KLog.d("选中的文件路径是：$videoPath")
        mainHandler.post { onClickPlay() }
    }

    fun onClickPlay() {
        if (TextUtils.isEmpty(videoPath)) {//视频地址为空，进入选择路径流程
            mActivity.openFileSelectTools(this)
            return
        }
        playHandler.prepare(videoPath)
        playVideo()
    }

    private fun playVideo() {
        KLog.d("即将播放的文件路径：$videoPath")
        uiControl.setVideoName(playHandler.playFile?.name ?: "---")
        playHandler.start()
    }
    fun onRelease(){
        playHandler.release()
    }
}