package com.example.testdemo.testModel.ffmpeg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testdemo.R

class FFMPEGActivity : AppCompatActivity() {
    private lateinit var uiControl: FFMPEGUiControl
    private lateinit var mPresenter: FFMPEGPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg)
        uiControl = FFMPEGUiControl(this)
        mPresenter = FFMPEGPresenter(uiControl)
        uiControl.setPresenter(mPresenter)
    }
}