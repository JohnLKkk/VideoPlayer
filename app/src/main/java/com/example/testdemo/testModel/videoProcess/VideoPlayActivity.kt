package com.example.testdemo.testModel.videoProcess

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import com.example.testdemo.App
import com.example.testdemo.R
import com.example.testdemo.base.BaseDefaultActivity
import com.example.testdemo.utlis.FileTools
import com.example.testdemo.utlis.ToastUtils

class VideoPlayActivity : BaseDefaultActivity() {
    private lateinit var uiControl: VideoPlayUiControl
    private lateinit var mPresenter: VideoPlayPresenter
    private var selectFileCallback: SelectFile? = null
    private val selectFileResultCode = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiControl = VideoPlayUiControl(this)
        mPresenter = VideoPlayPresenter(this, uiControl)
        uiControl.setPresenter(mPresenter)
        setActionBar("视频处理", true)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onRelease()
        uiControl.onRelease()
    }

    override fun getLayoutID(): Int = R.layout.activity_video_play

    override fun isFullScreenWindow(): Boolean = true

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == selectFileResultCode) {
            val uri = data?.data ?: return
            selectFileCallback?.selectCallback(FileTools.getPath(applicationContext, uri))
        }
    }

    fun openFileSelectTools(callback: SelectFile) {
        this.selectFileCallback = callback
        try {
            startActivityForResult(Intent.createChooser(
                    Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "*/*"
                        addCategory(Intent.CATEGORY_OPENABLE)
                    }, "选择视频文件"), selectFileResultCode)
        } catch (ex: ActivityNotFoundException) {
            ToastUtils.showShortInMainThread(App.context, "没有找到安装文件管理器！")
        }
    }

    interface SelectFile {
        fun selectCallback(path: String?)
    }
}