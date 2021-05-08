package com.yoy.videoPlayer.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.core.app.ActivityCompat
import com.yoy.v_Base.ui.BasePresenter
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoPlayer.utils.camera.CameraDevicesInfo
import kotlinx.coroutines.*
import kotlin.coroutines.resume

/**
 * Created by Void on 2021/2/3 17:58
 *
 */
class CameraPresenter(mActivity: CameraActivity) : BasePresenter(mActivity) {
    private val TAG = CameraPresenter::class.java.simpleName

    private val cameraManager: CameraManager by lazy {
        mActivity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    private val previewRequest: CaptureRequest by lazy {
        mSession.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
            addTarget(getUiControl().surfaceView.holder.surface)
        }.build()
    }

    private lateinit var mSession: CameraCaptureSession

    private val cameraThread = HandlerThread("cameraThread").apply { start() }

    private val cameraHandler = Handler(cameraThread.looper)
    private var cameraDevice: CameraDevice? = null
    private var useBackCamera = true

    private var openCameraID = ""
        get() {
            if (field.isBlank()) field = getCameraId(useBackCamera)
            return field
        }
    val characteristics: CameraCharacteristics by lazy {
        cameraManager.getCameraCharacteristics(openCameraID)
    }

    override fun getUiControl(): CameraUiControl = getActivityObj().uiControl as CameraUiControl

    override fun onRelease() {
    }

    fun initCamera() {
        if (openCameraID.isBlank()) {
            ToastUtils.showShort(getActivityObj(), "打开摄像头失败，没有找到匹配的摄像头")
            KLog.e(TAG, "打开摄像头失败，没有找到匹配的摄像头")
            return
        }
        if (ActivityCompat.checkSelfPermission(getActivityObj(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            KLog.e(TAG, "请赋予权限")
            return
        }
        GlobalScope.launch(Dispatchers.Main) {
            cameraDevice = openCameras()
            cameraDevice?.createCaptureSession(listOf(getUiControl().surfaceView.holder.surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            mSession = session
                            session.setRepeatingRequest(previewRequest, null, cameraHandler)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                        }

                    }, cameraHandler)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun openCameras() = suspendCancellableCoroutine<CameraDevice> {
        cameraManager.openCamera(openCameraID, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) = it.resume(camera)

            override fun onDisconnected(camera: CameraDevice) {
            }

            override fun onError(camera: CameraDevice, error: Int) {
            }
        }, cameraHandler)
    }

    private fun enumerateCameras(): List<CameraDevicesInfo> {
        val cameraList = mutableListOf<CameraDevicesInfo>()
        // 获取所有兼容的相机列表
        for (cameraId in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val capabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
            if (capabilities?.contains(CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE) == false) continue
            val orientation = characteristics.get(CameraCharacteristics.LENS_FACING)

//            val outputFormats = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.outputFormats
            cameraList.add(CameraDevicesInfo(orientation, cameraId, ImageFormat.JPEG))
        }
        return cameraList
    }

    private fun getCameraId(useBack: Boolean): String {
        enumerateCameras().forEach {
            KLog.d("CameraActivity", it.toString())
            if (useBack && it.orientation ==
                    CameraCharacteristics.LENS_FACING_BACK) {
                return it.cameraID
            } else if (!useBack && it.orientation ==
                    CameraCharacteristics.LENS_FACING_FRONT) {
                return it.cameraID
            }
        }
        return ""
    }

    private val cameraStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            camera.createCaptureSession(listOf(getUiControl().surfaceView.holder.surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            mSession = session
                            session.setRepeatingRequest(previewRequest, null, cameraHandler)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                        }

                    }, cameraHandler)
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice = null
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
}