package com.example.testdemo.testModel.popupWindow

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.example.testdemo.R

class PopupWindowActivity : AppCompatActivity() {
    private lateinit var mPopupWindow: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_window)
        mPopupWindow = PopupWindow(
                layoutInflater.inflate(R.layout.layout_skill_template_dialog, null),
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        )
        mPopupWindow.isOutsideTouchable=true
//        mPopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0)
    }

    fun onClickBtn(view: View) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
//        SimpleDialogHelper.showSimpleDialog("提示", "----", true) { dialog, which ->
//            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
//            startActivityForResult(intent, 1010)
//        }.show()
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }
}