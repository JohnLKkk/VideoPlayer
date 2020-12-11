package com.example.testdemo.testModel.jniTest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.testdemo.R
import com.yoy.v_Base.utils.LogUtils

class JniActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jni)
        initThreadJni()
    }

    fun onDj(view: View) {
//        setCallback()
        createAndRunThread()
    }

    private external fun setCallback()

    private external fun initThreadJni()

    private external fun createAndRunThread()

    companion object {
        init {
            System.loadLibrary("my-handle")
        }
    }

    fun onCallback(status: Int, msg: String) {
        LogUtils.e(msg = "$status----------$msg")
    }

    fun fromJNI(i: Int) {
        LogUtils.e(msg = "线程：$i")
    }
}