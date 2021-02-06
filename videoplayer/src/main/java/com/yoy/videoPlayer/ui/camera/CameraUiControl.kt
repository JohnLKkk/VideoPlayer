package com.yoy.videoPlayer.ui.camera

import android.view.SurfaceHolder
import android.view.SurfaceView
import com.yoy.v_Base.ui.BaseUiControl
import com.yoy.v_Base.ui.BasePresenter
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.utils.camera.MyCameraManager

/**
 * Created by Void on 2021/2/3 17:58
 *
 */
class CameraUiControl(mActivity: CameraActivity) : BaseUiControl(mActivity), SurfaceHolder.Callback {
    private val surfaceView: SurfaceView = mActivity.findViewById(R.id.surfaceView)
    override fun getPresenter(): BasePresenter = getActivityObj().mPresenter

    override fun initView() {
        val holder = surfaceView.holder
        holder.addCallback(this)
        MyCameraManager.instant.getCameraDevicesList()
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun onRelease() {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        TODO("Not yet implemented")
    }
}