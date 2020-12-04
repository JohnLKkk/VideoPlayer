package com.yoy.videoPlayer.ui

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.yoy.v_Base.utils.KLog
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.processing.DecodeType
import com.yoy.videoPlayer.processing.PlayStateListener
import com.yoy.videoPlayer.processing.PlayVideoHandler

/**
 * Created by Void on 2020/12/4 14:46
 *
 */
class VideoProcessPresenter(private val mActivity: VideoProcessActivity,
                            private val uiControl: VideoProcessUiControl) :
        FragmentCallback,
        PlayStateListener {
    private var doubleSpeedArray = mActivity.resources.getStringArray(R.array.DoubleSpeed)
    private var functionArray = mActivity.resources.getStringArray(R.array.FunctionList)
    private var decoderTypeArray = mActivity.resources.getStringArray(R.array.DecoderType)

    private var playSpeed = doubleSpeedArray[0]
    private var selectFunction = functionArray[0]
    private var decoderType = decoderTypeArray[0]

    val playHandler = PlayVideoHandler(this)

    //    var videoPath = ""
    var videoPath = "/storage/emulated/0/Download/smallfoot.mp4"

    init {
        playHandler.setDecoderType(DecodeType.HARDDecoder)
    }

    override fun onSelectFunction(type: Int, position: Int) {
        when (type) {
            0 -> playSpeed = doubleSpeedArray[position]
            1 -> selectFunction = functionArray[position]
            2 -> decoderType = decoderTypeArray[position]
        }
    }

    override fun onPlayControl(action: Int) {
        when (action) {
            0 -> playHandler.plStart()
            1 -> playHandler.plPause()
            2 -> {
            }
            3 -> {
            }
        }
    }

    fun selectFileResult(path: String?) {
        if (path == null || path.isEmpty()) {
            KLog.e("视频文件路径为null！")
            return
        }
        videoPath = path
        KLog.d("选中的文件路径是：$videoPath")
        playHandler.setDataPath(path)
        onPlayControl(0)
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
        val max = playHandler.getMaxTime()
        val tmp = max * time / 100
        playHandler.seekTo(tmp.toInt())
        uiControl.setPlayProgress(tmp)
        KLog.d("当前时间索引：$time")
    }

    //region
    override fun onPlayStart() {
        KLog.d("即将播放的文件路径：$videoPath")
        uiControl.setFileInfo(playHandler.fileInfo.name, videoPath)
        uiControl.setPlayProgress(playHandler.getCurrentTime())
    }

    override fun onPlayPaused() {
    }

    override fun onPlayStop() {
    }

    override fun onPlayEnd() {
    }

    override fun onPlayRelease() {
    }

    override fun onPlayTime(time: Long) {
        uiControl.setPlayProgress(time)
    }
    //endregion

}