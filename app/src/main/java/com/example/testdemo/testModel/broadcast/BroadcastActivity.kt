package com.example.testdemo.testModel.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.testdemo.R
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.v_Base.utils.KLog
import kotlinx.android.synthetic.main.activity_broadcast.*

class BroadcastActivity : BaseDefaultActivity() {

    override fun getLayoutID(): Int = R.layout.activity_broadcast

    override fun onInit() {
        setActionBar("广播测试",true)
        try {
            val mssage = intent.getStringExtra("userSaid") ?: "-1"
            Log.e("BroadcastActivity", "mssage:$mssage")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction("test_msg")
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        registerReceiver(broadcastReceiver, intentFilter)
        KLog.e("-----------111-")
    }

    override fun isFullScreenWindow(): Boolean = true

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context == null || intent == null || intent.action == null) return
            intent.run {
                when (action) {
                    Intent.ACTION_SCREEN_OFF -> KLog.e("锁屏！")
                    Intent.ACTION_SCREEN_ON -> KLog.e("亮屏！")
                }
                outputMessage(action!!, getStringExtra("msg") ?: "")
                Log.e("BroadcastActivity", "action:$action;extraMessage:${getStringExtra("msg")}")
            }
        }
    }

    private fun outputMessage(action: String, extraMessage: String) {
        tv2.text = "action:$action; extraMessage:$extraMessage"
    }
}