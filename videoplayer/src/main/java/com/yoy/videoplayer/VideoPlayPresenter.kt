package com.yoy.videoplayer

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.yoy.v_Base.utils.KLog

/**
 * Created by Void on 2020/7/27 17:27
 *
 */
class VideoPlayPresenter(private val mActivity: VideoPlayActivity,
                         private val uiControl: VideoPlayUiControl) :
        VideoPlayActivity.SelectFile,
        PlayStateListener ,
        DecodeOptionTextView.ClickItemCallback{
    private val mainHandler = Handler(Looper.getMainLooper())
    val playHandler = PlayVideoHandler(this)

        var videoPath = ""
//    var videoPath = "/storage/emulated/0/Download/smallfoot.mp4"

    override fun selectCallback(path: String?) {
        if (path == null || path.isEmpty()) {
            KLog.e("视频文件路径为null！")
            return
        }
        videoPath = path
        KLog.d("选中的文件路径是：$videoPath")
        mainHandler.post { onClickPlay() }
    }

    override fun onClickCallback(type: DecodeType) {
        playHandler.setDecoderType(type)
    }

    override fun onPlayStart() {
        KLog.d("即将播放的文件路径：$videoPath")
        uiControl.onStartPlay()
        uiControl.setVideoName(playHandler.fileInfo.name)
        mainHandler.postDelayed({
            uiControl.setPlayTime(2, playHandler.getCurrentTime())
            uiControl.setPlayTime(3, playHandler.getMaxTime())
        }, 1000)
    }

    override fun onPlayPaused() {
    }

    override fun onPlayStop() {
    }

    override fun onPlayEnd() {
    }

    override fun onPlayRelease() {
    }

    override fun onPlayTime(time: Int) {
        mainHandler.post {
            uiControl.setPlayTime(2, time)
        }
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

    /**
     * 跳转到选中的时间
     * @param time 进度百分比  0-100
     * */
    fun goSelectedTime(time: Int) {
        if (time < 0 || time > 100) {
            KLog.e("目标进度百分比无效")
            return
        }
        val tmp = playHandler.getMaxTime() * time / 100
        playHandler.seekTo(tmp)
        uiControl.setPlayTime(2, tmp)
        KLog.d("当前时间索引：$time")
    }
}