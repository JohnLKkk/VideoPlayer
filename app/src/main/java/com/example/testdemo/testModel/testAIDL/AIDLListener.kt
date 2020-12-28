package com.example.testdemo.testModel.testAIDL

import com.example.library_test_aidl.AidlAiListener
import com.yoy.v_Base.utils.LogUtils

/**
 * Created by Void on 2020/12/28 15:35
 *
 */
class AIDLListener : AidlAiListener.Stub() {
    override fun logCallback(msg: String?) {
        LogUtils.e("testModel-客户端", "--------msg:$msg")
    }
}