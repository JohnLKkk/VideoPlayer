package com.yoy.videoPlayer.utils.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.yoy.v_Base.utils.LogUtils
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoPlayer.VideoApplication

/**
 * Created by Void on 2021/2/4 16:39
 *
 */
class MyCameraManager private constructor() : CameraDevice.StateCallback() {
    private val TAG = MyCameraManager::class.java.simpleName
    private val mCameraManager: CameraManager by lazy {
        VideoApplication.context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    private val enumerateCameras: List<CameraDevicesInfo> by lazy {
        getCameraDevicesList()
    }
    private val cameraStatus = CameraStatus()

    init {
        mCameraManager.registerAvailabilityCallback(cameraStatus, Handler())

    }

    override fun onOpened(camera: CameraDevice) {
//        camera.createCaptureSession()
    }

    override fun onDisconnected(camera: CameraDevice) {
    }

    override fun onError(camera: CameraDevice, error: Int) {
        LogUtils.e(TAG, "摄像头打开失败:$error")
    }

    /**
     * @see CameraCharacteristics.LENS_FACING_BACK
     * 0后置 1前置
     */
    fun openCamera(cameraType: Int) {
        for (type in mCameraManager.cameraIdList) {
            when (type.toInt()) {
                CameraCharacteristics.LENS_FACING_BACK -> {
                    if (cameraType != 0) continue
                    if (ActivityCompat.checkSelfPermission(VideoApplication.context,
                                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                    mCameraManager.openCamera(type, this, null)
                }
                CameraCharacteristics.LENS_FACING_FRONT -> {
                    if (cameraType != 1) continue
                }
            }
        }
        LogUtils.e(TAG, "打开摄像头失败")
        ToastUtils.showShortInMainThread(VideoApplication.context, "打开摄像头失败")
    }

    /**
     * 返回所有兼容的相机列表
     */
    fun getCameraDevicesList(): List<CameraDevicesInfo> {
        val availableCameras = mutableListOf<CameraDevicesInfo>()
        //获取所有兼容相机的列表
        val cameraIds = mCameraManager.cameraIdList.filter {
            val capabilities = mCameraManager.getCameraCharacteristics(it).get(
                    CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
            capabilities?.contains(
                    CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE) ?: false
        }
        //遍历列表并返回兼容的相机
        cameraIds.forEach {
            val characteristics = mCameraManager.getCameraCharacteristics(it)
            val orientation = characteristics.get(CameraCharacteristics.LENS_FACING) ?: -1

            //查询可用的功能和输出格式
            val capabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
            val outputFormats = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.outputFormats

            if (orientation != -1) {
                availableCameras.add(CameraDevicesInfo(orientation, it, ImageFormat.JPEG))
            }

            //添加支持RAW、JPEG DEPTH功能的相机
            if (capabilities != null && outputFormats != null) {
                if (capabilities.contains(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW)
                        && outputFormats.contains(ImageFormat.RAW_SENSOR)) {
                    availableCameras.add(CameraDevicesInfo(orientation, it, ImageFormat.RAW_SENSOR))
                }
                if (capabilities.contains(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_DEPTH_OUTPUT)
                        && outputFormats.contains(ImageFormat.DEPTH_JPEG)) {
                    availableCameras.add(CameraDevicesInfo(orientation, it, ImageFormat.DEPTH_JPEG))
                }
            }
        }
        return availableCameras
    }

    companion object {
        val instant: MyCameraManager by lazy {
            MyCameraManager()
        }
    }

    inner class CameraStatus : CameraManager.AvailabilityCallback() {
        override fun onCameraAvailable(cameraId: String) {
            super.onCameraAvailable(cameraId)
        }

        override fun onCameraUnavailable(cameraId: String) {
            super.onCameraUnavailable(cameraId)
        }

        override fun onCameraAccessPrioritiesChanged() {
            super.onCameraAccessPrioritiesChanged()
        }
    }
}