package com.example.testdemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

//        for (i in 0 until 50) {
//            goFun(i, "currentTimeMillis:" + System.currentTimeMillis())
//        }
//        mRelease()
        val array=LinkedList<String>()
    }

    private external fun goFun(code: Int, msg: String): Int
    private external fun mRelease()

    companion object {
        init {
            System.loadLibrary("my-handle")
        }
    }
}