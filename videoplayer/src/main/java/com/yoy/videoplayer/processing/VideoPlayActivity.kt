package com.yoy.videoplayer.processing

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yoy.v_Base.utils.AppCode
import com.yoy.v_Base.utils.FileTools
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoplayer.R
import com.yoy.videoplayer.VideoApplication

class VideoPlayActivity : BaseDefaultActivity() {
    private val basePermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    )
    private var permissionDialog: AlertDialog? = null
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
        if (!TextUtils.isEmpty(mPresenter.videoPath)&&checkPermission()) mPresenter.onClickPlay()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            permissionDialog?.dismiss()
            mPresenter.onRelease()
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
            selectFileCallback?.selectCallback(FileTools.getFilePathByUri(applicationContext, uri))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            for (i in permissions.indices) {
                var resultStr = ""
                when (permissions[i]) {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE ->
                        resultStr = if (grantResults[i] == PackageManager.PERMISSION_GRANTED) "" else "读写权限获取失败"
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS ->
                        resultStr = if (grantResults[i] == PackageManager.PERMISSION_GRANTED) "" else "录音权限获取失败，没有基础权限无法继续！"
                }
                if (TextUtils.isEmpty(resultStr)) continue
                ToastUtils.showShort(this, resultStr)
            }
            if (checkPermission()) ToastUtils.showShort(applicationContext,"权限获取完成")
        }
    }

    private fun checkPermission(): Boolean {
        for (a in basePermissions) {
            if (ContextCompat.checkSelfPermission(this, a) == PackageManager.PERMISSION_GRANTED) return true
            KLog.d("AuthActivity#checkPermission", "没有读写或录音权限，请求权限")
            if (permissionDialog == null) {
                permissionDialog = AlertDialog.Builder(this)
                        .setTitle("请求权限")
                        .setMessage("需要一些基础权限以提供完整体验。")
                        .setCancelable(false)
                        .setPositiveButton("设置权限") { _: DialogInterface?, _: Int ->
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, a)) {
                                KLog.e("已设置拒绝授予权限且不在显示，\n请前往设置手动设置权限:$a")
                                ToastUtils.showShort(
                                        applicationContext,
                                        "已设置拒绝授予权限且不在显示，\n请前往设置手动设置权限"
                                )
                                finish()
                            } else {
                                ActivityCompat.requestPermissions(this, AppCode.basePermissions, 100)
                            }
                        }
                        .create()
            }
            if (permissionDialog?.isShowing == true) {
                permissionDialog?.dismiss()
            }
            permissionDialog?.show()
            return false
        }
        return true
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
            ToastUtils.showShortInMainThread(VideoApplication.context, "没有找到安装文件管理器！")
        }
    }

    interface SelectFile {
        fun selectCallback(path: String?)
    }
}