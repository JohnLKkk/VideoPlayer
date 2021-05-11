package com.yoy.videoPlayer.ui.camera

import android.os.Build
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import com.yoy.v_Base.ui.BaseUiControl
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.utils.camera.MyCameraManager

/**
 * Created by Void on 2021/2/3 17:58
 * 摄像头ui控制
 */
class CameraUiControl(mActivity: CameraActivity) : BaseUiControl(mActivity), SurfaceHolder.Callback {
    private val TAG = CameraUiControl::class.java.simpleName
    private val lightBtn: Button = mActivity.findViewById(R.id.lightBtn)
    private val photographBtn: Button = mActivity.findViewById(R.id.photographBtn)
    private val switchCameraBtn: Button = mActivity.findViewById(R.id.switchCameraBtn)
    private var lightMode = 0
    val surfaceView: AutoFitSurfaceView = mActivity.findViewById(R.id.surfaceView)

    init {
        runSeq()
    }

    override fun getPresenter(): CameraPresenter = getActivityObj().mPresenter as CameraPresenter

    override fun initView() {
        surfaceView.holder.addCallback(this)
    }

    override fun initListener() {
        lightBtn.setOnClickListener(this)
        photographBtn.setOnClickListener(this)
        switchCameraBtn.setOnClickListener(this)
    }

    override fun initData() {
    }

    override fun onRelease() {
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.lightBtn -> {
                lightMode = if (lightMode >= 2) 0 else lightMode + 1
                MyCameraManager.instant.switchLightMode(lightMode)
                lightBtn.text = when (lightMode) {
                    1 -> "闪光:SINGLE"
                    2 -> "闪光:TORCH"
                    else -> "闪光:off"
                }
            }
            R.id.photographBtn -> {
            }
            R.id.switchCameraBtn -> {
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun surfaceCreated(holder: SurfaceHolder?) {
        getPresenter().initCamera()
        val previewSize = MyCameraManager.instant.getPreviewSize(surfaceView.display)
        Log.d(TAG, "View finder size: ${surfaceView.width} x ${surfaceView.height}")
        Log.d(TAG, "Selected preview size: $previewSize")
        surfaceView.setAspectRatio(previewSize.width, previewSize.height)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) = Unit

    override fun surfaceDestroyed(holder: SurfaceHolder?) = Unit
}