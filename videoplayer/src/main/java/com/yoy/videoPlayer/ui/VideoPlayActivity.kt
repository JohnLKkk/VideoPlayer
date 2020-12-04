package com.yoy.videoPlayer.ui

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.v_Base.utils.AppCode
import com.yoy.v_Base.utils.FileTools
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.VideoApplication

class VideoPlayActivity : BaseDefaultActivity() {
    private lateinit var uiControl: VideoPlayUiControl
    private lateinit var mPresenter: VideoPlayPresenter
    private val selectFileResultCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiControl = VideoPlayUiControl(this)
        mPresenter = VideoPlayPresenter(this, uiControl)
        uiControl.setPresenter(mPresenter)
        setActionBar("视频处理", true)
        if (!TextUtils.isEmpty(mPresenter.videoPath)&&checkPermission()) mPresenter.onClickPlay()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onRelease()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            uiControl.onRelease()
        }catch (e:Exception){
            //Ignore
        }
    }

    override fun getLayoutID(): Int = R.layout.activity_video_play

    override fun isFullScreenWindow(): Boolean = true

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == selectFileResultCode) {
            val uri = data?.data ?: return
            mPresenter.selectFileResult(FileTools.getFilePathByUri(applicationContext, uri))
        }
    }

    fun openFileSelectTools() {
        try {
            startActivityForResult(Intent.createChooser(
                    Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "*/*"
                        addCategory(Intent.CATEGORY_OPENABLE)
                    }, "选择视频文件"), selectFileResultCode)
        } catch (ex: ActivityNotFoundException) {
            ToastUtils.showShortInMainThread(VideoApplication.context, "没有找到安装文件管理器！")
        }
    }
}