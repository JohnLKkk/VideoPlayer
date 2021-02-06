package com.example.testdemo.testModel.testAIDL

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.view.View
import com.example.library_test_aidl.IAppAIDLTest
import com.example.testdemo.R
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.v_Base.utils.LogUtils

class AIDLActivity : BaseDefaultActivity() {
    private var iAppAIDLTest: IAppAIDLTest? = null
    private var aidlIntent: Intent? = null

    override fun getLayoutID(): Int = R.layout.activity_aidl

    override fun onInit() {
        aidlIntent = Intent("com.example.library_test_aidl.hello").apply {
            setPackage("com.example.library_test_aidl")
        }
        bindService(aidlIntent, aidlConnection, Context.BIND_AUTO_CREATE)
        setActionBar("AIDL测试", true)
    }

    override fun isFullScreenWindow(): Boolean = true

    fun onBindServer(view: View) {
        if (iAppAIDLTest == null) {
            bindService(aidlIntent, aidlConnection, Context.BIND_AUTO_CREATE)
        }
        Log.e("Boot_Void", "onClick跑起来")
        iAppAIDLTest?.outPutLog("你好:" + System.currentTimeMillis())
    }

    private val aidlConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            LogUtils.w("AppAIDLTest", "onServiceConnected")
            iAppAIDLTest = IAppAIDLTest.Stub.asInterface(service)
            iAppAIDLTest?.setListener(AIDLListener())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            LogUtils.w("AppAIDLTest", "onServiceDisconnected")
            iAppAIDLTest = null
        }
    }
}
