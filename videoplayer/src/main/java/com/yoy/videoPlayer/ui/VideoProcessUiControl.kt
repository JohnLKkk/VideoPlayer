package com.yoy.videoPlayer.ui

import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.TimeUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.processing.VideoPreviewBar
import com.yoy.videoPlayer.ui.fragment.VideoControlFragment
import java.util.*

/**
 * Created by Void on 2020/12/3 17:08
 *
 */
class VideoProcessUiControl(private val mActivity: VideoProcessActivity) :
        VideoPreviewBar.ProgressCallback {
    private lateinit var mPresenter: VideoProcessPresenter
    var videoControlFragment = VideoControlFragment()
    private var videoView: SurfaceView = mActivity.findViewById(R.id.videoView)
    private var videoProgressBar: VideoPreviewBar = mActivity.findViewById(R.id.videoProgressBar)
    private var playTime: TextView = mActivity.findViewById(R.id.playTime)
    private var fileInfo: TextView = mActivity.findViewById(R.id.fileInfo)

    private var fragmentManager = mActivity.supportFragmentManager
    private var function1FragmentItems = LinkedList<Fragment>()
    private var function2FragmentItems = LinkedList<Fragment>()
    private val mainHandler = Handler(Looper.getMainLooper())
    private var currentTime = TimeUtils.formatTimeS(0)
    private var endTime = TimeUtils.formatTimeS(0)
    private var isJumpProgress = false //用户正在更改进度

    init {
        videoProgressBar.callback = this
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.defaultControlView,
                videoControlFragment,
                videoControlFragment.javaClass.name
        )
        fragmentTransaction.commit()
        setFileInfo("-", "-")
    }

    override fun onChangeProgress(index: Int, fromUser: Boolean) {
        currentTime = TimeUtils.formatTimeS(mPresenter.playHandler.progressToTimestamp(index))
        playTime.text = mActivity.getString(R.string.playTime, currentTime, endTime)
    }

    override fun onTouchCallback(type: Int, index: Int) {
        if (type == 0) {
            isJumpProgress = true
            return
        }
        isJumpProgress = false
        //没有播放时不进行进度条改变
        if (!mPresenter.playHandler.isReadyPlay()) {
            videoProgressBar.setProgress(0)
            return
        }
        mPresenter.goSelectedTime(index)
        videoProgressBar.setProgress(index)
    }

    fun setPresenter(mPresenter: VideoProcessPresenter) {
        this.mPresenter = mPresenter
        videoControlFragment.setCallback(mPresenter)
        videoView.holder.addCallback(mPresenter.playHandler)
    }

    fun setFileInfo(name: String, path: String) {
        fileInfo.text = mActivity.getString(R.string.fileInfo, name, path)
    }

    /**
     * 设置播放进度
     * @param index 目标播放进度
     *
     */
    fun setPlayProgress(index: Int) {
        //当用户正在更改播放进度，忽略设置播放进度请求
        if (isJumpProgress || !mPresenter.playHandler.isReadyPlay()) return
        currentTime = TimeUtils.formatTimeS(mPresenter.playHandler.progressToTimestamp(index))
        endTime = TimeUtils.formatTimeS(mPresenter.playHandler.getMaxTime())
        videoProgressBar.setProgress(index)
        playTime.text = mActivity.getString(R.string.playTime, currentTime, endTime)
    }

    fun onRelease() {
//        KLog.e("UI---------onRelease")
        videoView.holder.removeCallback(mPresenter.playHandler)
    }
}