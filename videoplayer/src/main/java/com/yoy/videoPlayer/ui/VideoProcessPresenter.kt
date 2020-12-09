package com.yoy.videoPlayer.ui

import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.LogUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.beans.VideoFileInfo
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

    private var playSpeed = doubleSpeedArray[0]
    private var selectFunction = functionArray[0]

    val playHandler = PlayVideoHandler(this)

    var videoPath = ""

    init {
        setDecoderType(1)
    }

    override fun onSelectFunction(type: Int, position: Int) {
        when (type) {
            0 -> playSpeed = doubleSpeedArray[position]
            1 -> selectFunction = functionArray[position]
            2 -> setDecoderType(position)
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

    override fun onItemClick(info: VideoFileInfo) {
        selectFileResult(info.vPath)
    }

    fun selectFileResult(path: String?) {
        if (path == null || path.isEmpty()) {
            KLog.e("视频文件路径为null！")
            return
        }
        videoPath = path
        KLog.d("选中的文件路径是：$videoPath")
        playHandler.setDataPath(path)
//        onPlayControl(0)
    }

    fun onRelease() {
        playHandler.release()
    }

    /**
     * 跳转到选中的时间
     * @param index 进度百分比  0-100
     * */
    fun goSelectedTime(index: Int) {
        if (index < 0 || index > 100) {
            KLog.e("目标进度百分比无效")
            return
        }
        playHandler.seekTo(playHandler.progressToTimestamp(index).toInt())
//        LogUtils.d(msg = "当前播放进度：$index")
    }

    /**
     * 设置解码类型
     * 0=硬解码 1=FFMPEG
     * @see DecodeType
     * @see R.array.DecoderType
     */
    fun setDecoderType(decodeType: Int) {
        when (decodeType) {
            0 -> playHandler.setDecoderType(DecodeType.HARDDecoder)
            1 -> playHandler.setDecoderType(DecodeType.FFMPEGDecoder)
            else -> {
                playHandler.setDecoderType(DecodeType.OTHER)
                return
            }
        }
        uiControl.videoControlFragment.setSelectFuctionUI(2, decodeType)
    }

    //region ----- 播放状态回调 -----
    override fun onPlayStart() {
        LogUtils.d(msg = "即将播放的文件路径：$videoPath")
        uiControl.setFileInfo(playHandler.fileInfo.name, videoPath)
        uiControl.setPlayProgress(playHandler.timestampToProgress())
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
        uiControl.setPlayProgress(playHandler.timestampToProgress(time))
    }
    //endregion
}