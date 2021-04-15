package com.example.testdemo.testModel.onClickTest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.testdemo.R
import com.yoy.v_Base.utils.KLog
import kotlinx.android.synthetic.main.activity_on_click.*
import java.io.File


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
//        startActivityForResult(Intent.createChooser(
//                Intent(Intent.ACTION_GET_CONTENT).apply {
//                    type = "*/*"
//                    addCategory(Intent.CATEGORY_OPENABLE)
//                }, "选择视频文件"), 1001)

        val filePath: String = getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.path + "recorderVideo.mp4" // 保存路径
        val uri = Uri.fromFile(File(filePath)) // 将路径转换为Uri对象
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE) // 表示跳转至相机的录视频界面
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0) // MediaStore.EXTRA_VIDEO_QUALITY 表示录制视频的质量，从 0-1，越大表示质量越好，同时视频也越大
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri) // 表示录制完后保存的录制，如果不写，则会保存到默认的路径，在onActivityResult()的回调，通过intent.getData中返回保存的路径
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10) // 设置视频录制的最长时间
        startActivityForResult(intent, 1001) // 跳转

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
//        val bb = FileTools.getFilePathByUri(applicationContext,a3)
//        KLog.e("aa:$a3; bb:$bb")

    }

    private fun onInputTest() {
    }

}