package com.yoy.videoPlayer

import com.yoy.v_Base.utils.TimeUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println(TimeUtils.formatTimeS(0))
        println(TimeUtils.formatTimeS(-1))
    }
}