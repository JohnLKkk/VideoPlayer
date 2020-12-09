package com.example.testdemo.testModel.jniTest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.view.View
import com.example.testdemo.R
import com.yoy.v_Base.utils.LogUtils

class JniActivity : AppCompatActivity(), MCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jni)
        LogUtils.e("-----","string:"+String::class.java.name)
    }

    fun onDj(view: View) {
        setCallback(this)
    }

    private external fun setCallback(callback: MCallback)

    companion object {
        init {
            System.loadLibrary("my-handle")
        }
    }

    override fun onCallback(msg: String) {
        LogUtils.e(msg = "----------$msg")
    }
}