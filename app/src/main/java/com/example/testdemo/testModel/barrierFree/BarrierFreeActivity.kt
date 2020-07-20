package com.example.testdemo.testModel.barrierFree

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testdemo.R
import kotlinx.android.synthetic.main.activity_barrier_free.*

class BarrierFreeActivity : AppCompatActivity() {
    private var str="依次按下:\n " +
            "中间的按键;\n " +
            "侧边前面的按键;\n " +
            "侧边后面的按键;\n "+
            "注：为防止误触，两次按键低于一秒将忽略该操作。可以上下滑动\n"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barrier_free)
        outputText.text=str

        val intentFilter = IntentFilter()
        intentFilter.addAction("MouseKeyEvent")
        registerReceiver(mBroadcastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBroadcastReceiver)
    }

    fun updateOutputText(message:String){
        str=str+message+"\n"
        outputText.text=str
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