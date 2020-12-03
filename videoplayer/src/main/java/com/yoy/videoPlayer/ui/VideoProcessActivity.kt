package com.yoy.videoPlayer.ui

import android.os.Bundle
import android.view.View
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.videoPlayer.R

/**
 * Created by Void on 2020/12/3 14:41
 * 视频处理界面
 */
class VideoProcessActivity : BaseDefaultActivity() {
    private lateinit var uiControl: VideoProcessUiControl

    override fun getLayoutID(): Int = R.layout.activity_video_process

    override fun isFullScreenWindow(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiControl = VideoProcessUiControl(this)
    }
}