package com.yoy.videoPlayer.ui.video

import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yoy.v_Base.ui.BaseUiControl
import com.yoy.v_Base.utils.TimeUtils
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.beans.VideoFileInfo
import com.yoy.videoPlayer.processing.PlayVideoHandler
import com.yoy.videoPlayer.processing.VideoPreviewBar
import com.yoy.videoPlayer.ui.video.fragment.FilterFragment
import com.yoy.videoPlayer.ui.video.fragment.VideoControlFragment
import java.util.*

/**
 * Created by Void on 2020/12/3 17:08
 *
 */
class VideoProcessUiControl(mActivity: MainVideoActivity) : BaseUiControl(mActivity),
        FragmentCallback,
        VideoPreviewBar.ProgressCallback {
    private val filterFragment = FilterFragment(mActivity)
    private lateinit var videoControl: VideoControlFragment

    private val videoView: SurfaceView = mActivity.findViewById(R.id.videoView)
    private val functionLayout1: FrameLayout = mActivity.findViewById(R.id.functionLayout1)
    var videoProgressBar: VideoPreviewBar = mActivity.findViewById(R.id.videoProgressBar)
    private val playTime: TextView = mActivity.findViewById(R.id.playTime)
    private val fileInfo: TextView = mActivity.findViewById(R.id.fileInfo)

    private val function1FragmentItems = LinkedList<Fragment>()
    private val function2FragmentItems = LinkedList<Fragment>()

    private var doubleSpeedArray = mActivity.resources.getStringArray(R.array.DoubleSpeed)
    private var functionArray = mActivity.resources.getStringArray(R.array.FunctionList)
    var isJumpProgress = false //正在更改进度

    private var playSpeed = doubleSpeedArray[0]
    private var selectFunction = functionArray[0]

    init {
        initView()
        initListener()
        initData()
    }

    override fun getPresenter(): VideoProcessPresenter = getActivityObj().mPresenter as VideoProcessPresenter

    override fun initView() {
        videoControl = (fragmentManager.findFragmentByTag("VideoControlFragment") as VideoControlFragment)
        videoControl.setCallback(this)
        function1FragmentItems.add(filterFragment)
        videoView.holder.addCallback(getPlayHandler())
    }

    override fun initListener() {
        videoProgressBar.callback = this
    }

    override fun initData() {
        setFileInfo("-", "-")
    }

    override fun onChangeProgress(index: Int, fromUser: Boolean) {
        setPlayTime(
                getPlayHandler().progressToTimestamp(index),
                getPlayHandler().getMaxTime()
        )
    }

    override fun onTouchCallback(type: Int, index: Int) {
        if (type == 0) {
            isJumpProgress = true
            return
        }
        isJumpProgress = false
        //没有播放时不进行进度条改变
        if (!getPlayHandler().isReadyPlay()) {
            videoProgressBar.setProgress(0)
            return
        }
        getPresenter().goSelectedTime(index)
        videoProgressBar.setProgress(index)
    }

    override fun onSelectFunction(type: Int, position: Int) {
        when (type) {
//            0 -> playSpeed = doubleSpeedArray[position]
            0 -> ToastUtils.showShort(getActivityObj(), "该功能还在开发")
            1 -> {
                if (position > 1) ToastUtils.showShort(getActivityObj(), "该功能还在开发")
                selectFunction = functionArray[position]
                setFunctionFragment(position - 1)
            }
            2 -> getPresenter().setDecoderType(position)
        }
    }

    override fun getActivityObj(): MainVideoActivity = super.getActivityObj() as MainVideoActivity

    override fun onPlayControl(action: Int) {
        when (action) {
            0 -> getPlayHandler().plStart()
            1 -> getPlayHandler().plPause()
            2, 3 -> ToastUtils.showShort(getActivityObj(), "该功能还在开发")
        }
    }

    override fun onItemClick(info: VideoFileInfo) {
        getPresenter().selectFileResult(info.vPath)
    }

    override fun onRelease() {
//        KLog.e("UI---------onRelease")
        videoView.holder.removeCallback(getPlayHandler())
    }

    private fun setFunctionFragment(index: Int) {
        if (index < 0 || index >= function1FragmentItems.size) {
            functionLayout1.visibility = View.GONE
            return
        }
        functionLayout1.visibility = View.VISIBLE
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.functionLayout1, function1FragmentItems[index])
        fragmentTransaction.commit()
    }

    fun setSelectFunctionUI(type: Int, position: Int) {
        videoControl.setSelectFunctionUI(type, position)
    }

    fun getPlayHandler(): PlayVideoHandler = getActivityObj().playHandler

    fun setFileInfo(name: String, path: String) {
        fileInfo.text = getActivityObj().getString(R.string.fileInfo, name, path)
    }

    fun setPlayTime(cT: Long, mT: Long) {
        playTime.text = getActivityObj().getString(R.string.playTime,
                TimeUtils.formatTimeS(cT),
                TimeUtils.formatTimeS(mT)
        )
    }
}