package com.yoy.videoPlayer.ui

import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.TimeUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.processing.DecodeType
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
    private lateinit var videoControlFragment: VideoControlFragment
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

    init {
        videoProgressBar.callback = this
    }

    override fun onChangeProgress(index: Int, fromUser: Boolean) {
        setPlayProgress(mPresenter.playHandler.getMaxTime() * index / 100)
    }

    override fun onTouchCallback(type: Int, index: Int) {
        if (type == 0) return
        mPresenter.goSelectedTime(index)
    }

    fun setPresenter(mPresenter: VideoProcessPresenter) {
        videoControlFragment = VideoControlFragment(mPresenter)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.defaultControlView, videoControlFragment, videoControlFragment.javaClass.name)
        fragmentTransaction.commit()
        videoView.holder.addCallback(mPresenter.playHandler)
    }

    fun setFileInfo(name: String, path: String) {
        fileInfo.text = mActivity.getString(R.string.fileInfo, name, path)
    }

    fun setPlayProgress(time: Long) {
        val tmp = time / 1000L
        currentTime = TimeUtils.formatTimeS(tmp)
        val maxTime = mPresenter.playHandler.getMaxTime()
        when {
            time == 0L -> videoProgressBar.setProgress(0)
            time > maxTime -> videoProgressBar.setProgress(100)
            else -> {
                videoProgressBar.setProgress((time / maxTime * 100).toInt())
                KLog.e("maxTime:$maxTime;position:$time;pro:${time / maxTime * 100}")
            }
        }
        playTime.text = mActivity.getString(R.string.playTime, currentTime, endTime)
    }

    fun onRelease() {
        videoView.holder.removeCallback(mPresenter.playHandler)
    }
}