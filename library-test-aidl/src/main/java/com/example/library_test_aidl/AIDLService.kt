package com.example.library_test_aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * Created by Void on 2020/6/5 17:20
 */
class AIDLService : Service() {
    private val outputLog = OutputLog()

    override fun onBind(intent: Intent?): IBinder = outputLog

    override fun onCreate() {
        super.onCreate()
        var tmp: String
        Thread {
            while (true) {
                tmp = "time:" + System.currentTimeMillis()
                outputLog.mListener?.logCallback(tmp)
                Log.w("--library_test_aidl--", ":AIDLService-Thread:$tmp")
                Thread.sleep(1500)
            }
        }.start()
    }
}