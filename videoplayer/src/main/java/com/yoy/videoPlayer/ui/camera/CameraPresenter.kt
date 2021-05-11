package com.yoy.videoPlayer.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.Camera
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.core.app.ActivityCompat
import com.yoy.v_Base.ui.BasePresenter
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoPlayer.utils.camera.CameraDevicesInfo
import com.yoy.videoPlayer.utils.camera.MyCameraManager
import kotlinx.coroutines.*
import kotlin.coroutines.resume

/**
 * Created by Void on 2021/2/3 17:58
 *
 */
class CameraPresenter(mActivity: CameraActivity) : BasePresenter(mActivity) {
    private val TAG = CameraPresenter::class.java.simpleName

    override fun getUiControl(): CameraUiControl = getActivityObj().uiControl as CameraUiControl

    override fun onRelease() {
        MyCameraManager.instant.closeCamera()
    }

    fun initCamera() {
        if (ActivityCompat.checkSelfPermission(getActivityObj(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            KLog.e(TAG, "请赋予摄像头权限")
            return
        }
        MyCameraManager.instant.openCamera(0,mActivity,getUiControl().surfaceView.holder.surface)
        MyCameraManager.instant.setAutoFocus(CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
    }
}