package com.yoy.videoplayer

import android.content.Intent
import android.view.View
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.videoplayer.processing.VideoPlayActivity

class MainActivity : BaseDefaultActivity() {
    override fun getLayoutID(): Int = R.layout.activity_main

    fun selectFile(view: View) {
        startActivity(Intent(this, VideoPlayActivity::class.java))
    }
}