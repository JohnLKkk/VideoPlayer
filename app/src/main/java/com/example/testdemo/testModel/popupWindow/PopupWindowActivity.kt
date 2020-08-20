package com.example.testdemo.testModel.popupWindow

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.example.testdemo.R
import com.example.testdemo.base.BaseDefaultActivity


class PopupWindowActivity : BaseDefaultActivity() {
    private lateinit var mPopupWindow: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar("弹窗测试")
        mPopupWindow = PopupWindow(
                layoutInflater.inflate(R.layout.layout_skill_template_dialog, null),
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        )
        mPopupWindow.isOutsideTouchable=true
        mPopupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mPopupWindow.elevation = 10f
    }

    override fun getLayoutID(): Int = R.layout.activity_popup_window

    fun onClickBtn(view: View) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
//        SimpleDialogHelper.showSimpleDialog("提示", "----", true) { dialog, which ->
//            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
//            startActivityForResult(intent, 1010)
//        }.show()
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }
}