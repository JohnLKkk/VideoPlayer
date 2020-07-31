package com.example.testdemo.testModel.ffmpeg

import android.os.Environment
import com.example.testdemo.utlis.KLog
import java.lang.Exception

/**
 * Created by Void on 2020/7/27 17:27
 *
 */
class FFMPEGPresenter(private val uiControl: FFMPEGUiControl) {
   var videoPath = "${Environment.getExternalStorageDirectory()}/123.mp4"
    init {
        KLog.w("videoPath:$videoPath")
    }
}