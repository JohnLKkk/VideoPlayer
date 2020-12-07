package com.yoy.videoPlayer

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yoy.videoPlayer.utils.db.SQLInfo

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val items = HashMap<String, String>()
        items["a"] = "a"
        items["b"] = "b"
        items["c"] = "c"
        println("---------------"+ SQLInfo("hellow", items).createTable)
    }
}