package com.example.library_test_aidl

import android.util.Log

/**
 * Created by Void on 2020/6/5 17:13
 *
 */
class OutputLog : IAppAIDLTest.Stub() {
    override fun outPutLog(msg: String?) {
        Log.e("-------", this.javaClass.simpleName + "outPutLog:$msg")
    }
}