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
    }

    fun onDj(view: View) {
        setCallback()
    }

    private external fun setCallback()

    companion object {
        init {
            System.loadLibrary("my-handle")
        }
    }

    fun onCallback(status:Int,msg: String) {
        LogUtils.e(msg = "$status----------$msg")
    }
}