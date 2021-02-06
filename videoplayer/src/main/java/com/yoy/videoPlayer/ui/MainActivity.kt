package com.yoy.videoPlayer.ui

import android.content.Intent
import android.view.View
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.ui.camera.CameraActivity
import com.yoy.videoPlayer.ui.video.MainVideoActivity

/**
 * Created by Void on 2021/2/3 16:36
 * 程序入口
 */
class MainActivity : BaseDefaultActivity() {

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun onInit() {
    }

    fun onBtnClick(view: View) {
        when (view.id) {
            R.id.videoBtn -> startActivity(Intent(applicationContext, MainVideoActivity::class.java))
            R.id.cameraBtn -> startActivity(Intent(applicationContext, CameraActivity::class.java))
        }
    }


}