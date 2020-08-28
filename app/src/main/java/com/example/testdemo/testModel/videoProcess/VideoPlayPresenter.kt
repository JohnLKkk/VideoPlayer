package com.example.testdemo.testModel.videoProcess

import android.os.Handler
import android.text.TextUtils
import com.example.testdemo.utlis.KLog

/**
 * Created by Void on 2020/7/27 17:27
 *
 */
class VideoPlayPresenter(private val mActivity: VideoPlayActivity,
                         private val uiControl: VideoPlayUiControl) :
        VideoPlayActivity.SelectFile,
        PlayStateListener {
    private val mainHandler = Handler()
    val playHandler = PlayVideoHandler(this)
    var videoPath = ""
//    var videoPath = "/storage/emulated/0/smallfoot.mp4"

    override fun selectCallback(path: String?) {
        if (path == null || path.isEmpty()) {
            KLog.e("视频文件路径为null！")
            return
        }
        videoPath = path
        KLog.d("选中的文件路径是：$videoPath")
        mainHandler.post { onClickPlay() }
    }

    override fun onPlayStart() {
        KLog.d("即将播放的文件路径：$videoPath")
        uiControl.onStartPlay()
        uiControl.setVideoName(playHandler.fileInfo.name)
        mainHandler.postDelayed({
            uiControl.setPlayTime(2, playHandler.getCurrentTime())
            uiControl.setPlayTime(3, playHandler.getMaxTime())
        },1000)
    }

    override fun onPlayPaused() {
    }

    override fun onPlayStop() {
    }

    override fun onPlayEnd() {
    }

    override fun onPlayRelease() {
    }

    fun onClickPlay() {
        if (TextUtils.isEmpty(videoPath)) {//视频地址为空，进入选择路径流程
            mActivity.openFileSelectTools(this)
            return
        }
        playHandler.setDataPath(videoPath)
    }

    fun preVideo() {

    }

    fun nextVideo() {

    }

    fun onRelease() {
        playHandler.release()
    }
}