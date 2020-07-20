package com.example.library_test_aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by Void on 2020/6/5 17:20
 */
class AIDLService : Service() {
    private val outputLog = OutputLog()

    override fun onBind(intent: Intent?): IBinder {
        return outputLog
    }
}