package com.example.testdemo.testModel.ffmpeg

import android.os.Bundle
import com.example.testdemo.R
import com.example.testdemo.base.BaseDefaultActivity

class FFMPEGActivity : BaseDefaultActivity() {
    private lateinit var uiControl: FFMPEGUiControl
    private lateinit var mPresenter: FFMPEGPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiControl = FFMPEGUiControl(this)
        mPresenter = FFMPEGPresenter(uiControl)
        uiControl.setPresenter(mPresenter)
        setActionBar("视频处理",true)
    }

    override fun getLayoutID(): Int = R.layout.activity_ffmpeg

    override fun isFullScreenWindow(): Boolean = true

}