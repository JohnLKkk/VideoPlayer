package com.example.testdemo.testModel.permission

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testdemo.R
import com.yoy.v_Base.utils.AppCode
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.v_Base.utils.ToastUtils

class PermissionActivity : BaseDefaultActivity() {
    private var permissionDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar("权限处理",true)
        if (checkPermission()) {
            if (checkFloatWindowPermission()) {
                KLog.e("拿到全部权限！")
            }
        }
    }

    override fun getLayoutID(): Int = R.layout.activity_permission

    override fun isFullScreenWindow(): Boolean = true

    override fun onDestroy() {
        super.onDestroy()
        permissionDialog?.dismiss()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            for (i in permissions.indices) {
                var resultStr = ""
                when (permissions[i]) {
                    Manifest.permission.READ_EXTERNAL_STORAGE -> resultStr = if (grantResults[i] == PackageManager.PERMISSION_GRANTED) "" else "读写权限获取失败"
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS -> resultStr = if (grantResults[i] == PackageManager.PERMISSION_GRANTED) "" else "录音权限获取失败，没有基础权限无法继续！"
                    Manifest.permission.READ_PHONE_STATE -> resultStr = if (grantResults[i] == PackageManager.PERMISSION_GRANTED) "" else "请求设备识别码权限失败，无法通过授权！"
                    Manifest.permission.ACCESS_COARSE_LOCATION -> resultStr = if (grantResults[i] == PackageManager.PERMISSION_GRANTED) "" else "获取定位权限失败！"
                }
                if (TextUtils.isEmpty(resultStr)) continue
                ToastUtils.showShort(this, resultStr)
            }
            if (checkPermission()) checkFloatWindowPermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        KLog.e("onActivityResult", "SDK_INT:${Build.VERSION.SDK_INT}")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        KLog.e("onActivityResult", "requestCode:$requestCode," +
                "canDrawOverlays:${Settings.canDrawOverlays(this)}," +
                "canWrite:${Settings.System.canWrite(this)}")
        if (requestCode == 1001) {
            if (Settings.canDrawOverlays(this)) {
                checkFloatWindowPermission()
                return
            }
        } else if (requestCode == 1002) {
            if (Settings.System.canWrite(this)) {
                checkFloatWindowPermission()
                return
            }
        }
        KLog.e("没有权限无法继续，初始化失败！")
        ToastUtils.showShort(this, "没有权限无法继续，初始化失败！")
        finish()
    }

    fun onRequestPermission(view: View) {
        if (checkPermission()) {
            if (checkFloatWindowPermission()) {
                KLog.e("拿到全部权限！")
            }
        }
    }

    /**
     * 检查权限录音，读写等权限,没有将请求请求权限
     *
     * @return true 取得全部权限
     */
    private fun checkPermission(): Boolean {
        for (a in AppCode.basePermissions) {
            if (ContextCompat.checkSelfPermission(this, a) !=
                    PackageManager.PERMISSION_GRANTED) {
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
        }
        KLog.d("AuthActivity#checkPermission", "已具备基本运行所需权限")
        return true
    }

    /**
     * 检查悬浮窗、写入系统设置权限
     */
    private fun checkFloatWindowPermission(): Boolean {
        //6.0以下不进行权限请求
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
        if (!Settings.canDrawOverlays(this)) {
            KLog.d("没有悬浮窗权限，开始获取！")
            AlertDialog.Builder(this)
                    .setTitle("请求权限")
                    .setMessage("该APP基于悬浮窗运行，请授予权限，以继续执行。")
                    .setCancelable(false)
                    .setPositiveButton("设置权限") { _, _ ->
                        startActivityForResult(Intent().apply {
                            action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                            data = Uri.parse("package:$packageName")
                        }, 1001)
                    }
                    .create()
                    .show()
            return false
        } else if (!Settings.System.canWrite(this)) {
            KLog.d("没有写入设置权限，开始获取！")
            AlertDialog.Builder(this)
                    .setTitle("请求权限")
                    .setMessage("语音交互需要写入设置权限，没有权限无法提供完整使用体验，请允许权限")
                    .setCancelable(false)
                    .setPositiveButton("设置权限") { _, _ ->
                        startActivityForResult(Intent().apply {
                            action = Settings.ACTION_MANAGE_WRITE_SETTINGS
                            data = Uri.parse("package:$packageName")
                        }, 1002)
                    }
                    .create()
                    .show()
            return false
        }
        return true
    }
}