package com.example.testdemo.testModel.videoProcess

import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import com.example.testdemo.R

/**
 * Created by Void on 2020/7/27 17:14
 *
 */
class VideoPlayUiControl(private val mActivity: VideoPlayActivity) : View.OnClickListener {
    private lateinit var mPresenter: VideoPlayPresenter
    private var playIv: ImageView = mActivity.findViewById(R.id.playIV)
    private val videoView: SurfaceView = mActivity.findViewById(R.id.videoView)

    init {
        playIv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.playIV -> mPresenter.onClickPlay()
        }
    }

    fun setPresenter(presenter: VideoPlayPresenter) {
        this.mPresenter = presenter
        mPresenter.setSurfaceHolder(videoView.holder)
    }
}