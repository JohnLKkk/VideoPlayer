package com.yoy.v_Base.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Void on 2020/8/17 10:30
 *
 */
abstract class BaseDefaultActivity : AppCompatActivity() {
    lateinit var uiControl: BaseUiControl
    lateinit var mPresenter: BasePresenter

    abstract fun getLayoutID(): Int

    abstract fun onInit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutID())
        onInit()
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
}