package com.example.library_test_aidl

import android.util.Log

/**
 * Created by Void on 2020/6/5 17:13
 *
 */
class OutputLog : IAppAIDLTest.Stub() {
    var mListener: AidlAiListener? = null

    override fun setListener(obj: AidlAiListener?) {
        mListener = obj
    }

    override fun outPutLog(msg: String?) {
        Log.e("--library_test_aidl--", this.javaClass.simpleName + "$msg")
    }

}