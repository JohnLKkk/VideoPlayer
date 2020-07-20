package com.example.testdemo.testModel.testAIDL

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import com.example.testdemo.IAppAIDLTest
import com.example.testdemo.R

class AIDLActivity : AppCompatActivity() {
    private val str = "com.example.library_test_aidl.hello"
    private var iAppAIDLTest: IAppAIDLTest? = null
    private var aidlIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidl)
        aidlIntent = Intent()
        aidlIntent!!.setPackage("com.example.library_test_aidl")
        aidlIntent!!.action = str
        bindService(aidlIntent, aidlConnection, Context.BIND_AUTO_CREATE)
    }

    fun onBindServer(view: View) {
        if (iAppAIDLTest == null) {
            bindService(aidlIntent, aidlConnection, Context.BIND_AUTO_CREATE)
        }
        Log.e("Boot_Void", "onClick跑起来")
        iAppAIDLTest?.outPutLog("你好啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊")
    }

    private val aidlConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iAppAIDLTest = IAppAIDLTest.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            iAppAIDLTest = null
        }
    }
}
