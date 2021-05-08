package com.yoy.videoPlayer.ui.camera

import android.os.Build
import android.util.Log
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi
import com.yoy.v_Base.ui.BaseUiControl
import com.yoy.videoPlayer.R

/**
 * Created by Void on 2021/2/3 17:58
 * 摄像头ui控制
 */
class CameraUiControl(mActivity: CameraActivity) : BaseUiControl(mActivity), SurfaceHolder.Callback {
    private val TAG = CameraUiControl::class.java.simpleName
    val surfaceView: AutoFitSurfaceView = mActivity.findViewById(R.id.surfaceView)
    override fun getPresenter(): CameraPresenter = getActivityObj().mPresenter as CameraPresenter
    init {
        runSeq()
    }
    override fun initView() {
        surfaceView.holder.addCallback(this)
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun onRelease() {
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun surfaceCreated(holder: SurfaceHolder?) {
        val previewSize = getPreviewOutputSize(
                surfaceView.display, getPresenter().characteristics, SurfaceHolder::class.java)
        Log.d(TAG, "View finder size: ${surfaceView.width} x ${surfaceView.height}")
        Log.d(TAG, "Selected preview size: $previewSize")
        surfaceView.setAspectRatio(previewSize.width, previewSize.height)
        getPresenter().initCamera()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) = Unit

    override fun surfaceDestroyed(holder: SurfaceHolder?) = Unit
}