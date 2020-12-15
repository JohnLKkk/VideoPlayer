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
        initThreadJni()
    }

    fun onDj(view: View) {
//        setCallback()
//        createAndRunThread()
        thread {
            var i = 1001
            do {
                postMsg(JniBean(i, "currentTimeMillis:" + System.currentTimeMillis()))
                i++
                if (i == 5) break
                Thread.sleep(1500)
            } while (true)
        }
    }

    private external fun setCallback()

    private external fun initThreadJni()

    private external fun createAndRunThread()

    private external fun postMsg(bean: JniBean)

    private external fun goFun(code:Int,msg:String): Int

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