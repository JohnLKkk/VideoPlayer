package com.example.testdemo.testModel.scanDevicesIP

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.testdemo.R
import kotlin.concurrent.thread

class ScanIPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_ip)
        thread {
            ScanDeviceUtils.getInstant().scan()
        }
    }

    fun onAgain(view: View) {
        thread {
            ScanDeviceUtils.getInstant().scan()
        }
    }
}