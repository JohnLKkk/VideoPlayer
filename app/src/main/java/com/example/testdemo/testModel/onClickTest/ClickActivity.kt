package com.example.testdemo.testModel.onClickTest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testdemo.R
import kotlinx.android.synthetic.main.activity_on_click.*

/**
 * Created by Void on 2020/8/28 11:40
 *
 */
class ClickActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_click)
        btn_1.setOnClickListener { onInputTest() }
    }

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
//        startActivity(Intent().apply {
//            setClassName("com.robot.app_ai", "com.robot.app_ai.ui.AuthActivity")
//            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        })
//        val apkFile = File(Environment.getExternalStorageDirectory().path + "/wyt/update/103.apk")
//        val intent = Intent(Intent.ACTION_VIEW)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            val contentUri = FileProvider.getUriForFile(applicationContext, applicationContext.packageName + ".fileProvider", apkFile)
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
//        } else {
//            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        YunStudyVideoManager.getInstance().analyzeText("我想看小学一年级语文知识点字母")
    }

    private fun onInputTest() {
    }

}