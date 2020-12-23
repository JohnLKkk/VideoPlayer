package com.example.testdemo.testModel.jniTest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.testdemo.R
import com.yoy.v_Base.utils.LogUtils
import kotlin.concurrent.thread

class JniActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jni)
//        initThreadJni()
    }

    fun onDj(view: View) {
//        setCallback()
//        createAndRunThread()
        mRelease()
    }

    private external fun setCallback()

    private external fun initThreadJni()

    private external fun mRelease()

    private external fun postMsg(bean: JniBean)

    companion object {
        init {
            System.loadLibrary("my-handle")
        }
    }

    fun onCallback(status: Int, msg: String) {
        LogUtils.e(msg = "onCallback:$status----------$msg")
    }
}