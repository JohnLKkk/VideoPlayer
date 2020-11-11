package com.example.testdemo.testModel.barrierFree

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.example.testdemo.R
import com.yoy.v_Base.ui.BaseDefaultActivity
import kotlinx.android.synthetic.main.activity_barrier_free.*

class BarrierFreeActivity : BaseDefaultActivity() {
    private var str = "依次按下:\n " +
            "中间的按键;\n " +
            "侧边前面的按键;\n " +
            "侧边后面的按键;\n " +
            "注：为防止误触，两次按键低于一秒将忽略该操作。可以上下滑动\n"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        outputText.text = str

        val intentFilter = IntentFilter()
        intentFilter.addAction("MouseKeyEvent")
        registerReceiver(mBroadcastReceiver, intentFilter)
        setActionBar("辅助服务测试",true)
    }

    override fun getLayoutID(): Int = R.layout.activity_barrier_free

    override fun isFullScreenWindow(): Boolean = true

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBroadcastReceiver)
    }

    fun updateOutputText(message: String) {
        str = str + message + "\n"
        outputText.text = str
    }


    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null || intent.action == "") return
            when (intent.action) {
                "MouseKeyEvent" -> {
                    val scanCode = intent.getStringExtra("scanCode") ?: ""
                    updateOutputText(scanCode)
                }
            }
        }
    }
}