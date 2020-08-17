package com.example.testdemo.testModel.videoProcess

import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.testdemo.R

/**
 * Created by Void on 2020/7/27 17:14
 *
 */
class VideoPlayUiControl(private val mActivity: VideoPlayActivity) : View.OnClickListener {
    private lateinit var mPresenter: VideoPlayPresenter
    private var backIv: ImageView = mActivity.findViewById(R.id.backIv)
    private var videoNameTv: TextView = mActivity.findViewById(R.id.videoNameTv)
    private var settingIv: ImageView = mActivity.findViewById(R.id.settingIv)
    private var playIv: ImageView = mActivity.findViewById(R.id.playIV)
    private val videoView: SurfaceView = mActivity.findViewById(R.id.videoView)

    init {
        backIv.setOnClickListener(this)
        playIv.setOnClickListener(this)
        settingIv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backIv -> mActivity.finish()
            R.id.playIV -> mPresenter.onClickPlay()
            R.id.settingIv -> {
            }
        }
    }

    fun setPresenter(presenter: VideoPlayPresenter) {
        this.mPresenter = presenter
        mPresenter.playHandler.setSurfaceHolder(videoView.holder)
    }

    fun setVideoName(name: String) {
        videoNameTv.text = name
    }

    fun onRelease() {

    }
}