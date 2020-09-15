package com.example.testdemo.testModel.onClickTest

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.DialogCompat
import com.example.testdemo.R
import com.example.testdemo.utlis.FileTools
import com.example.testdemo.utlis.KLog
import kotlinx.android.synthetic.main.activity_on_click.*

/**
 * Created by Void on 2020/8/28 11:40
 *
 */
class ClickActivity : AppCompatActivity() {
    //    val a1 = Uri.parse("content://com.android.providers.media.documents/document/video%3A61884")
//    val a2 = Uri.parse("content://com.android.providers.media.documents/document/video%3A49480")
    lateinit var a3: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_click)
        onClickTest.setOnClickListener { onTestClick() }
        startActivityForResult(Intent.createChooser(
                Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "*/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }, "选择视频文件"), 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            val uri = data?.data ?: return
            KLog.e("选择视频的路径:$uri")
            a3 = uri
//            selectFileCallback?.selectCallback(FileTools.getPath(applicationContext, uri))
//            selectFileCallback?.selectCallback(uri.toString())
        }
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
//        val bb=FileTools.getFilePathByUri(a1)
        val bb = FileTools.getFilePathByUri(applicationContext,a3)
        KLog.e("aa:$a3; bb:$bb")
    }

    private fun onInputTest() {
//        KLog.d(stringFromJNI())
    }

//    private external fun stringFromJNI():String

//    companion object{
//        init {
//            System.loadLibrary("native-lib")
//        }
//    }
}