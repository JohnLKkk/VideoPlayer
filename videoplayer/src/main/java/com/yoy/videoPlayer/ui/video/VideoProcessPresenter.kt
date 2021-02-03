package com.yoy.videoPlayer.ui.video

import android.os.Handler
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.LogUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.processing.DecodeType
import com.yoy.videoPlayer.processing.PlayStateListener
import com.yoy.videoPlayer.processing.PlayVideoHandler

/**
 * Created by Void on 2020/12/4 14:46
 *
 */
class VideoProcessPresenter(private val mActivity: MainVideoActivity,
                            private val uiControl: VideoProcessUiControl) :
        PlayStateListener {


    var videoPath = ""

    init {
        getPlayHandler().setPlayStateListener(this)
        Handler().postDelayed({
            setDecoderType(1)
        }, 1000)
    }

    //region ----- 播放状态回调 -----
    override fun onPlayStart() {
        LogUtils.d(msg = "即将播放的文件路径：$videoPath")
        uiControl.setFileInfo(getPlayHandler().fileInfo.name, videoPath)
        setPlayProgress(getPlayHandler().timestampToProgress())
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
        setPlayProgress(getPlayHandler().timestampToProgress(time))
    }
    //endregion

    fun getPlayHandler(): PlayVideoHandler = mActivity.playHandler

    fun selectFileResult(path: String?) {
        if (path == null || path.isEmpty()) {
            KLog.e("视频文件路径为null！")
            return
        }
        videoPath = path
        KLog.d("选中的文件路径是：$videoPath")
        getPlayHandler().setDataPath(path)
//        onPlayControl(0)
    }

    fun onRelease() {
        getPlayHandler().release()
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
        getPlayHandler().seekTo(getPlayHandler().progressToTimestamp(index).toInt())
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
            0 -> getPlayHandler().setDecoderType(DecodeType.HARDDecoder)
            1 -> getPlayHandler().setDecoderType(DecodeType.FFMPEGDecoder)
            else -> return
        }
        uiControl.videoControlFragment.setSelectFunctionUI(2, decodeType)
    }


    /**
     * 设置播放进度
     * @param index 目标播放进度
     *
     */
    fun setPlayProgress(index: Int) {
        //当用户正在更改播放进度，忽略设置播放进度请求
        if (uiControl.isJumpProgress || !getPlayHandler().isReadyPlay()) return
        uiControl.videoProgressBar.setProgress(index)
        uiControl.setPlayTime(getPlayHandler().getCurrentTime(), getPlayHandler().getMaxTime())
    }
}