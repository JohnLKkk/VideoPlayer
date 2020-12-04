package com.yoy.v_Base.ui

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yoy.v_Base.utils.AppCode
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.ToastUtils

/**
 * Created by Void on 2020/8/17 10:30
 *
 */
abstract class BaseDefaultActivity : AppCompatActivity() {

    private var permissionDialog: AlertDialog? = null

    abstract fun getLayoutID(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutID())
        checkPermission()
    }

    override fun onPause() {
        super.onPause()
        permissionDialog?.dismiss()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus || !isFullScreenWindow()) return
        Log.d("###", "全屏设置")
        window.apply {
            val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.apply {
                setPadding(0, 0, 0, 0)
                systemUiVisibility = uiOptions
                setOnSystemUiVisibilityChangeListener {
                    if (it and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        systemUiVisibility = uiOptions
                    }
                }
            }
            attributes = attributes.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
                horizontalMargin = 0.0f
                verticalMargin = 0.0f
                dimAmount = 0.0f
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            for (i in permissions.indices) {
                var resultStr = ""
                when (permissions[i]) {
                    Manifest.permission.READ_EXTERNAL_STORAGE ->
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

    open fun isFullScreenWindow(): Boolean = false

    /**
     * 设置 ActionBar的标题和返回键
     */
    open fun setActionBar(mTitle: String, hasBackBtn: Boolean = false) {
        supportActionBar?.run {
            setHomeButtonEnabled(hasBackBtn)
            setDisplayHomeAsUpEnabled(hasBackBtn)
            title = mTitle
        }
    }

    open fun checkPermission(): Boolean {
        for (a in AppCode.basePermissions) {
            if (ContextCompat.checkSelfPermission(this, a) == PackageManager.PERMISSION_GRANTED) break
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
        Log.i("checkPermission", "AuthActivity#checkPermission-拿到所有权限")
        return true
    }
}