package com.yoy.videoPlayer.utils.camera

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.Display
import android.view.Surface
import android.view.SurfaceHolder
import androidx.core.app.ActivityCompat
import com.yoy.v_Base.utils.KLog
import com.yoy.videoPlayer.VideoApplication
import com.yoy.videoPlayer.ui.camera.getPreviewOutputSize

/**
 * Created by Void on 2021/2/4 16:39
 *
 */
class MyCameraManager private constructor() {
    private val TAG = MyCameraManager::class.java.simpleName
    private val mCameraManager: CameraManager by lazy {
        VideoApplication.context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    private val previewSurfaces = arrayListOf<Surface>()

    private val handler = Handler(Looper.getMainLooper())

    //一条专门处理摄像头事务的线程
    private val cameraThread = HandlerThread("cameraThread").apply { start() }

    private val cameraHandler = Handler(cameraThread.looper)

    private var cameraDevice: CameraDevice? = null
    private var mSession: CameraCaptureSession? = null
    private var previewRequest: CaptureRequest.Builder? = null
    private var characteristics: CameraCharacteristics? = null

    /**
     * @return 打开是否成功
     * @see CameraCharacteristics.LENS_FACING_BACK
     * 0后置 1前置
     */
    fun openCamera(cameraType: Int, context: Activity, surface: Surface): Boolean {
        val cameraID: String = getCameraId(cameraType)
        if (cameraID.isBlank()) return false
        previewSurfaces.add(surface)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            KLog.e(TAG, "请赋予摄像头权限")
            return false
        }
        mCameraManager.openCamera(cameraID, cameraDeviceCallback, cameraHandler)
        characteristics = mCameraManager.getCameraCharacteristics(cameraID)
        return true
    }

    fun closeCamera() {
        previewSurfaces.clear()
        try {
            cameraDevice?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error closing camera", e)
        }
    }

    fun getPreviewSize(display: Display): Size {
        characteristics?.let {
            return getPreviewOutputSize(display, it, SurfaceHolder::class.java)
        }
        return Size(0, 0)
    }

    /**
     * 设置对焦模式
     * @see CaptureRequest.CONTROL_AF_MODE
     */
    fun setAutoFocus(b: Int) {
        previewRequest?.let {
            it.set(CaptureRequest.CONTROL_AF_MODE, b)
            mSession?.setRepeatingRequest(it.build(), null, cameraHandler)
        }
    }

    /**
     * 切换闪光灯模式
     * 注:前置摄像头没有闪光灯
     * @param type 0
     */
    fun switchLightMode(type: Int) {
        previewRequest?.set(CaptureRequest.FLASH_MODE, when (type) {
            1 -> CameraMetadata.FLASH_MODE_SINGLE
            2 -> CameraMetadata.FLASH_MODE_TORCH
            else -> CameraMetadata.FLASH_MODE_OFF
        })
        previewRequest?.let { pr ->
            mSession?.setRepeatingRequest(pr.build(), null, cameraHandler)
        }
    }

    private fun getCameraId(cameraType: Int): String {
        enumerateCameras().forEach {
            KLog.d("CameraActivity", it.toString())
            if (cameraType == 0 && it.orientation ==
                    CameraCharacteristics.LENS_FACING_BACK) {
                return it.cameraID
            } else if (cameraType == 1 && it.orientation ==
                    CameraCharacteristics.LENS_FACING_FRONT) {
                return it.cameraID
            }
        }
        KLog.e(TAG, "打开摄像头失败，没有找到匹配的摄像头")
        return ""
    }

    /**
     * 返回所有兼容的相机列表
     */
    private fun enumerateCameras(): List<CameraDevicesInfo> {
        val cameraList = mutableListOf<CameraDevicesInfo>()
        // 获取所有兼容的相机列表
        for (cameraId in mCameraManager.cameraIdList) {
            val characteristics = mCameraManager.getCameraCharacteristics(cameraId)
            val capabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
            if (capabilities?.contains(CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE) == false) continue
            val orientation = characteristics.get(CameraCharacteristics.LENS_FACING)

//            val outputFormats = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.outputFormats
            cameraList.add(CameraDevicesInfo(orientation, cameraId, ImageFormat.JPEG))
        }
        return cameraList
    }

    private val cameraDeviceCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            previewRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewSurfaces.forEach { s -> previewRequest?.addTarget(s) }
            camera.createCaptureSession(previewSurfaces,
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            mSession = session
                            previewRequest?.let { session.setRepeatingRequest(it.build(), null, cameraHandler) }
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                        }
                    }, cameraHandler)
        }

        override fun onDisconnected(camera: CameraDevice) {
        }

        override fun onError(camera: CameraDevice, error: Int) {
            val msg = when (error) {
                ERROR_CAMERA_DEVICE -> "Fatal (device)"
                ERROR_CAMERA_DISABLED -> "Device policy"
                ERROR_CAMERA_IN_USE -> "Camera in use"
                ERROR_CAMERA_SERVICE -> "Fatal (service)"
                ERROR_MAX_CAMERAS_IN_USE -> "Maximum cameras in use"
                else -> "Unknown"
            }
            val exc = RuntimeException("Camera ${camera.id} error: ($error) $msg")
            Log.e(TAG, exc.message, exc)
        }
    }

    companion object {
        val instant: MyCameraManager by lazy {
            MyCameraManager()
        }
    }
}