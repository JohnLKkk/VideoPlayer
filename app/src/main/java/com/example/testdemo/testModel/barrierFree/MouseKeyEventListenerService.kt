package com.example.testdemo.testModel.barrierFree

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.yoy.v_Base.utils.KLog

/**
 * Created by Void on 2020/7/2 16:24
 * 无障碍服务-鼠标监听
 */
class MouseKeyEventListenerService : AccessibilityService() {
    private val handler = Handler()
    private var scanCode = ""
    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    }

    override fun onCreate() {
        super.onCreate()
        KLog.e("服务已启动")
    }

    override fun onDestroy() {
        super.onDestroy()
        KLog.e("服务已关闭")
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        when (event?.action) {
            KeyEvent.ACTION_DOWN -> {
                scanCode = event.scanCode.toString()
                handler.postDelayed(runnable, 500)
            }
            KeyEvent.ACTION_UP -> {
                handler.removeCallbacksAndMessages(null)
            }
        }
        when (event?.scanCode) {
            188, 189, 190 -> outputLog("按键码：" + event.scanCode)
            else -> {
                outputLog("非定义功能按键码，不响应  " + event?.scanCode)
                return false
            }
        }
        return super.onKeyEvent(event)
    }

    private val runnable = Runnable {
        KLog.e("按键码：$scanCode")
        sendMessage(scanCode)
    }

    private fun sendMessage(message: String) {
        sendBroadcast(Intent().apply {
            action = "MouseKeyEvent"
            putExtra("scanCode", "按键码：$message")
        })
    }

    private fun outputLog(string: String) {
        KLog.d(string)
    }
}