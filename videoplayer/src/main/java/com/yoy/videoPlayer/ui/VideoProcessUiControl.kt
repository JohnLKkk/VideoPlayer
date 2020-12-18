package com.yoy.videoPlayer.ui

import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yoy.v_Base.utils.TimeUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.beans.VideoFileInfo
import com.yoy.videoPlayer.processing.VideoPreviewBar
import com.yoy.videoPlayer.ui.fragment.FilterFragment
import com.yoy.videoPlayer.ui.fragment.VideoControlFragment
import java.util.*

/**
 * Created by Void on 2020/12/3 17:08
 *
 */
class VideoProcessUiControl(private val mActivity: VideoProcessActivity) :
        FragmentCallback,
        VideoPreviewBar.ProgressCallback {
    private lateinit var mPresenter: VideoProcessPresenter
    val videoControlFragment = VideoControlFragment()
    private val videoView: SurfaceView = mActivity.findViewById(R.id.videoView)
    var videoProgressBar: VideoPreviewBar = mActivity.findViewById(R.id.videoProgressBar)
    private val playTime: TextView = mActivity.findViewById(R.id.playTime)
    private val fileInfo: TextView = mActivity.findViewById(R.id.fileInfo)

    private val fragmentManager = mActivity.supportFragmentManager
    private val function1FragmentItems = LinkedList<Fragment>()
    private val function2FragmentItems = LinkedList<Fragment>()

    private val filterFragment = FilterFragment()

    private var doubleSpeedArray = mActivity.resources.getStringArray(R.array.DoubleSpeed)
    private var functionArray = mActivity.resources.getStringArray(R.array.FunctionList)
    private val mainHandler = Handler(Looper.getMainLooper())
    var isJumpProgress = false //用户正在更改进度

    private var playSpeed = doubleSpeedArray[0]
    private var selectFunction = functionArray[0]

    init {
        videoProgressBar.callback = this
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.defaultControlView,
                videoControlFragment,
                videoControlFragment.javaClass.name
        )
        fragmentTransaction.commit()
        function1FragmentItems.add(filterFragment)
        setFileInfo("-", "-")
    }

    override fun onChangeProgress(index: Int, fromUser: Boolean) {
        setPlayTime(
                mPresenter.playHandler.progressToTimestamp(index),
                mPresenter.playHandler.getMaxTime()
        )
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

    override fun onSelectFunction(type: Int, position: Int) {
        when (type) {
            0 -> playSpeed = doubleSpeedArray[position]
            1 -> selectFunction = functionArray[position]
            2 -> mPresenter.setDecoderType(position)
        }
    }

    override fun onPlayControl(action: Int) {
        when (action) {
            0 -> mPresenter.playHandler.plStart()
            1 -> mPresenter.playHandler.plPause()
            2 -> {
            }
            3 -> {
            }
        }
    }

    override fun onItemClick(info: VideoFileInfo) {
        mPresenter.selectFileResult(info.vPath)
    }

    fun setPresenter(mPresenter: VideoProcessPresenter) {
        this.mPresenter = mPresenter
        videoControlFragment.setCallback(this)
        videoView.holder.addCallback(mPresenter.playHandler)
    }

    fun setFileInfo(name: String, path: String) {
        fileInfo.text = mActivity.getString(R.string.fileInfo, name, path)
    }

    fun onRelease() {
//        KLog.e("UI---------onRelease")
        videoView.holder.removeCallback(mPresenter.playHandler)
    }

    fun setPlayTime(cT: Long, mT: Long) {
        playTime.text = mActivity.getString(R.string.playTime,
                TimeUtils.formatTimeS(cT),
                TimeUtils.formatTimeS(mT)
        )
    }
}