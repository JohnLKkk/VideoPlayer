package com.example.testdemo.testModel.coroutine

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import com.yoy.v_Base.utils.KLog
import kotlinx.coroutines.*
import retrofit2.Call
import kotlin.coroutines.resume

/**
 * Created by Void on 2021/4/29 11:28
 *
 */
class CoroutineTest {
    private val TAG = "协程学习"
    fun test() {
        KLog.d(TAG, "主线程id：${Looper.getMainLooper().thread.id}")
//        onRunBlockIng()
//        onGlobalScope()
//        retrofit {
//            onSuccess {
//                Log.e(TAG, "onSuccess:$it")
//            }
//            onFail { msg, errorCode ->
//                Log.e(TAG, "msg:$msg; errorCode:$errorCode")
//            }
//            onComplete {
//                Log.e(TAG, "onComplete:$it")
//                "艹"
//            }
//        }
        GlobalScope.launch {
            KLog.e(TAG,t1())
        }
        KLog.d(TAG, "主线程执行结束")
    }

    fun onRunBlockIng() = runBlocking {
        KLog.e(TAG, "repeat-----------")
        repeat(8) {
            KLog.e(TAG, "time:${System.currentTimeMillis()}")
            delay(1000)
        }
        this.coroutineContext
    }

    fun onGlobalScope() {
        GlobalScope.launch(Dispatchers.Main) {
            KLog.d(TAG, "协程onGlobalScope-Main:${Thread.currentThread().id}")
        }
        GlobalScope.launch(Dispatchers.IO) {
            KLog.d(TAG, "协程onGlobalScope-IO:${Thread.currentThread().id}")
        }
        GlobalScope.launch(Dispatchers.Default) {
            KLog.d(TAG, "协程onGlobalScope-Default:${Thread.currentThread().id}")
        }
        GlobalScope.launch(Dispatchers.Unconfined) {
            KLog.d(TAG, "协程onGlobalScope-Unconfined:${Thread.currentThread().id}")
        }
    }

    private fun retrofit(dsl: RetrofitCoroutineDSL.() -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val ct = RetrofitCoroutineDSL().apply(dsl)
            ct.let {
                val a1 = async(Dispatchers.IO) {
                    it.onSuccess?.invoke(100)
                }
                val a2 = async(Dispatchers.Default) {
                    it.onFail?.invoke("我是谁", -1)
                }
                val a3 = async(Dispatchers.Unconfined) {
                    it.onComplete?.invoke(3.7)
                }
                a1.await()
                a2.await()
                Log.e(TAG, "await:${a3.await()}")
            }
        }
    }

    private suspend fun t1(): String = suspendCancellableCoroutine {
        callbackTest(object : TestI {
            override fun onOU(s: String) {
                it.resume(s)
            }
        })
    }

    private fun callbackTest(t: TestI) {
        Thread {
            t.onOU("-1-2-")
        }.start()
    }

    interface TestI {
        fun onOU(s: String)
    }

    inner class RetrofitCoroutineDSL {
        internal var onSuccess: ((code: Int) -> Unit)? = null
            private set
        internal var onFail: ((msg: String, errorCode: Int) -> Unit)? = null
            private set
        internal var onComplete: ((t: Double) -> String)? = null
            private set

        fun onSuccess(back: (code: Int) -> Unit) {
            this.onSuccess = back
        }

        fun onFail(back: (msg: String, errorCode: Int) -> Unit) {
            this.onFail = back
        }

        fun onComplete(back: (t: Double) -> String) {
            this.onComplete = back
        }

        internal fun clean() {
            onSuccess = null
            onFail = null
            onComplete = null
        }
    }

}