package com.yoy.videoPlayer.ui.camera

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
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

    private val cameraManager: CameraManager by lazy {
        mActivity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    /** [CameraCharacteristics] 与摄像头ID一致*/
    private val characteristics: CameraCharacteristics by lazy {
        cameraManager.getCameraCharacteristics(args.cameraId)
    }

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
    }

    private fun enumerateCameras() {
        // 获取所有兼容的相机列表
        val cameraIds = cameraManager.cameraIdList.filter {
            val characteristics = cameraManager.getCameraCharacteristics(it)
            val capabilities = characteristics.get(
                    CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
            capabilities?.contains(CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE)
                    ?: false
        }
        cameraIds.forEach { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val orientation = lensOrientationString(
                    characteristics.get(CameraCharacteristics.LENS_FACING)!!)

            // Query the available capabilities and output formats
            val capabilities = characteristics.get(
                    CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)!!
            val outputFormats = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!.outputFormats

            // All cameras *must* support JPEG output so we don't need to check characteristics
            availableCameras.add(FormatItem(
                    "$orientation JPEG ($id)", id, ImageFormat.JPEG))

            // Return cameras that support RAW capability
            if (capabilities.contains(
                            CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW) &&
                    outputFormats.contains(ImageFormat.RAW_SENSOR)) {
                availableCameras.add(FormatItem(
                        "$orientation RAW ($id)", id, ImageFormat.RAW_SENSOR))
            }

            // Return cameras that support JPEG DEPTH capability
            if (capabilities.contains(
                            CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_DEPTH_OUTPUT) &&
                    outputFormats.contains(ImageFormat.DEPTH_JPEG)) {
                availableCameras.add(FormatItem(
                        "$orientation DEPTH ($id)", id, ImageFormat.DEPTH_JPEG))
            }
        }


    }

}