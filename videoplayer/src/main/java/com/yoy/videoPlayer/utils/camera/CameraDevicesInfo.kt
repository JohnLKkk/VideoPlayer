package com.yoy.videoPlayer.utils.camera

import android.graphics.ImageFormat

/**
 * Created by Void on 2021/2/6 15:01
 *
 */
class CameraDevicesInfo(orientation: Int, val cameraID: String, val format: Int) {
    val title: String = "$orientation ${getFormatType()} ($cameraID)"

    fun getFormatType(): String {
        return when (format) {
            ImageFormat.JPEG -> "JPEG"
            ImageFormat.RAW_SENSOR -> "RAW"
            ImageFormat.DEPTH_JPEG -> "DEPTH"
            else -> "Unknown"
        }
    }
}