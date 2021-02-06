package com.yoy.videoPlayer.ui.camera

import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.videoPlayer.R

/**
 * Created by Void on 2021/2/3 17:36
 * 摄像头界面
 */
class CameraActivity : BaseDefaultActivity() {

    override fun getLayoutID(): Int = R.layout.activity_camera

    override fun onInit() {
        mPresenter = CameraPresenter(this)
        uiControl = CameraUiControl(this)
    }


}