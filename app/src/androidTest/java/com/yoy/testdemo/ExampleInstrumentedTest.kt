package com.yoy.testdemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testdemo.gg.AdManager
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
        AdManager.onOpenActionAdd()
    }
}