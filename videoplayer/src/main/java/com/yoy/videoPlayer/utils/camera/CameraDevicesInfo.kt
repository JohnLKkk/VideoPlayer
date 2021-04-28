package com.yoy.videoPlayer.utils.camera

import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics

/**
 * Created by Void on 2021/2/6 15:01
 *
 */
class CameraDevicesInfo(val orientation: Int?, val cameraID: String, val format: Int) {

    override fun toString(): String = "${lensOrientationString(orientation)} $cameraID ${getFormatType(format)}"

    private fun getFormatType(format: Int?): String {
        return when (format) {
            ImageFormat.JPEG -> "JPEG"
            ImageFormat.RAW_SENSOR -> "RAW"
            ImageFormat.DEPTH_JPEG -> "DEPTH"
            else -> "Unknown"
        }
    }

    private fun lensOrientationString(value: Int?) = when (value) {
        CameraCharacteristics.LENS_FACING_BACK -> "后置摄像头"
        CameraCharacteristics.LENS_FACING_FRONT -> "前置摄像头"
        CameraCharacteristics.LENS_FACING_EXTERNAL -> "外接摄像头"
        else -> "未知的"
    }
}