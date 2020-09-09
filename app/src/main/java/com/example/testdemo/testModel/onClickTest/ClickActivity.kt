package com.example.testdemo.testModel.onClickTest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testdemo.R
import com.example.testdemo.utlis.KLog
import kotlinx.android.synthetic.main.activity_on_click.*

/**
 * Created by Void on 2020/8/28 11:40
 *
 */
class ClickActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_click)
        onClickTest.setOnClickListener { onInputTest() }
    }

    private external fun stringFromJNI():String

    fun onTestClick() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(Intent().apply {
//                action = "com.robot.app_ai.action.bootStartUp"
//                setPackage("com.robot.app_ai")
//                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            })
//        } else {
//            startService(Intent().apply {
//                action = "com.robot.app_ai.action.bootStartUp"
//                setPackage("com.robot.app_ai")
//                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            })
//        }
    }

    private fun onInputTest() {
        KLog.d(stringFromJNI())
    }
    companion object{
        init {
            System.loadLibrary("native-lib")
        }
    }
}