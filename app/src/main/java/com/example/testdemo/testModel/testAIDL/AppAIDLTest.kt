package com.example.testdemo.testModel.testAIDL

import android.util.Log
import com.example.testdemo.IAppAIDLTest

/**
 * Created by Void on 2020/6/5 16:58
 *
 */
class AppAIDLTest : IAppAIDLTest.Stub() {
    override fun outPutLog(msg: String?) {
        Log.e("-------", "outPutLog:$msg")
    }
}