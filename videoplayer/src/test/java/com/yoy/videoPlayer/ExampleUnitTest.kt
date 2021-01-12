package com.yoy.videoPlayer

import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        Char[] tmp ={ 79, 112, 101, 110, 105, 110, 103, 32, 39, 37, 115, 39, 32, 102, 111, 114, 32, 37, 115, 10 }
//Opening '`�6�f���*'�`�6�' for (null)
        val tmp = intArrayOf(79, 112, 101, 110, 105, 110, 103, 32, 39, 37, 115, 39, 32, 102, 111, 114, 32, 37, 115, 10)
        var str=""
        for (t in tmp){
            str+=t.toChar()
        }
        println(str)
    }
}