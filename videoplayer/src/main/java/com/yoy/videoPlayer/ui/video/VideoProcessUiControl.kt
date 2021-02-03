package com.yoy.videoPlayer.ui.video

import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
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
class VideoProcessUiControl(private val mActivityMain: MainVideoActivity) :
        FragmentCallback,
        VideoPreviewBar.ProgressCallback {
    private lateinit var mPresenter: VideoProcessPresenter
    private val filterFragment = FilterFragment(mActivityMain)
    val videoControlFragment = VideoControlFragment()

    private val videoView: SurfaceView = mActivityMain.findViewById(R.id.videoView)
    private val functionLayout1: FrameLayout = mActivityMain.findViewById(R.id.functionLayout1)
    var videoProgressBar: VideoPreviewBar = mActivityMain.findViewById(R.id.videoProgressBar)
    private val playTime: TextView = mActivityMain.findViewById(R.id.playTime)
    private val fileInfo: TextView = mActivityMain.findViewById(R.id.fileInfo)

    private val fragmentManager = mActivityMain.supportFragmentManager
    private val function1FragmentItems = LinkedList<Fragment>()
    private val function2FragmentItems = LinkedList<Fragment>()


    private var doubleSpeedArray = mActivityMain.resources.getStringArray(R.array.DoubleSpeed)
    private var functionArray = mActivityMain.resources.getStringArray(R.array.FunctionList)
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
        mPresenter.goSelectedTime(index)
        videoProgressBar.setProgress(index)
    }

    override fun onSelectFunction(type: Int, position: Int) {
        when (type) {
//            0 -> playSpeed = doubleSpeedArray[position]
            0 -> ToastUtils.showShort(mActivityMain, "该功能还在开发")
            1 -> {
                if (position > 1) ToastUtils.showShort(mActivityMain, "该功能还在开发")
                selectFunction = functionArray[position]
                setFunctionFragment(position - 1)
            }
            2 -> mPresenter.setDecoderType(position)
        }
    }

    override fun onPlayControl(action: Int) {
        when (action) {
            0 -> getPlayHandler().plStart()
            1 -> getPlayHandler().plPause()
            2, 3 -> ToastUtils.showShort(mActivityMain, "该功能还在开发")
        }
    }

    override fun onItemClick(info: VideoFileInfo) {
        mPresenter.selectFileResult(info.vPath)
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

    fun getPlayHandler(): PlayVideoHandler = mActivityMain.playHandler

    fun setPresenter(mPresenter: VideoProcessPresenter) {
        this.mPresenter = mPresenter
        videoControlFragment.setCallback(this)
        videoView.holder.addCallback(getPlayHandler())
    }

    fun setFileInfo(name: String, path: String) {
        fileInfo.text = mActivityMain.getString(R.string.fileInfo, name, path)
    }

    fun onRelease() {
//        KLog.e("UI---------onRelease")
        videoView.holder.removeCallback(getPlayHandler())
    }

    fun setPlayTime(cT: Long, mT: Long) {
        playTime.text = mActivityMain.getString(R.string.playTime,
                TimeUtils.formatTimeS(cT),
                TimeUtils.formatTimeS(mT)
        )
    }
}