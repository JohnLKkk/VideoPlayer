package com.example.testdemo.testModel.scanDevicesIP

import android.os.Bundle
import android.view.View
import com.example.testdemo.R
import com.yoy.v_Base.ui.BaseDefaultActivity
import kotlin.concurrent.thread

class ScanIPActivity : BaseDefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thread {
            ScanDeviceUtils.getInstant().scan()
        }
    }

    override fun getLayoutID(): Int = R.layout.activity_scan_ip

    override fun isFullScreenWindow(): Boolean = true

    fun onAgain(view: View) {
        thread {
            ScanDeviceUtils.getInstant().scan()
        }
    }
}