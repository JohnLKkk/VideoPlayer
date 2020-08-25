package com.example.testdemo.testModel.videoProcess

import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.testdemo.R

/**
 * Created by Void on 2020/7/27 17:14
 */
class VideoPlayUiControl(private val mActivity: VideoPlayActivity) : View.OnClickListener {
    private lateinit var mPresenter: VideoPlayPresenter

    //top_layout
    private var backIv: ImageView = mActivity.findViewById(R.id.backIv)
    private var videoNameTv: TextView = mActivity.findViewById(R.id.videoNameTv)
    private var settingIv: ImageView = mActivity.findViewById(R.id.settingIv)

    private var playIv: ImageView = mActivity.findViewById(R.id.playIV)
    private var jumpTime: TextView = mActivity.findViewById(R.id.jumpTime)
    private val videoView: SurfaceView = mActivity.findViewById(R.id.videoView)

    //bottom_layout
    private val currentTime: TextView = mActivity.findViewById(R.id.currentTime)
    private val endTime: TextView = mActivity.findViewById(R.id.endTime)
    private val decodeType: TextView = mActivity.findViewById(R.id.decodeType)
    private val videoProgressView: SeekBar = mActivity.findViewById(R.id.videoScheduleView)
    private val playBtn: ImageView = mActivity.findViewById(R.id.playBtn)
    private val preBtn: ImageView = mActivity.findViewById(R.id.preBtn)
    private val nextBtn: ImageView = mActivity.findViewById(R.id.nextBtn)

    init {
        backIv.setOnClickListener(this)
        settingIv.setOnClickListener(this)

        playIv.setOnClickListener(this)

        decodeType.setOnClickListener(this)
        playBtn.setOnClickListener(this)
        preBtn.setOnClickListener(this)
        nextBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIv -> mActivity.finish()
            R.id.settingIv,
            R.id.playIV -> mPresenter.onClickPlay()
            R.id.decodeType -> {
            }
            R.id.playBtn -> changePlayState()
            R.id.preBtn -> mPresenter.preVideo()
            R.id.nextBtn -> mPresenter.nextVideo()
        }
    }

    fun setPresenter(presenter: VideoPlayPresenter) {
        this.mPresenter = presenter
        mPresenter.playHandler.setSurfaceHolder(videoView.holder)
    }

    fun setVideoName(name: String) {
        videoNameTv.text = name
    }

    /**
     * @param type 类型:
     * 1设置跳转的显示时间 jumpTime
     * 2设置当前播放的时间进度 currentTime
     * 3设置结束播放的时间进度 endTime
     * @param position 时间，单位秒
     */
    fun setPlayTime(type: Int, position: Int) {
        when (type) {
            1 -> jumpTime.text = position.toString()
            2 -> currentTime.text = position.toString()
            3 -> endTime.text = position.toString()
            else -> return
        }
    }

    fun onRelease() {
    }

    private fun changePlayState() {
        if (mPresenter.playHandler.isPlaying()) {
            playBtn.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_stop_play)
            mPresenter.playHandler.stop()
        } else {
            playBtn.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_start_play)
            mPresenter.playHandler.start()
        }
    }
}