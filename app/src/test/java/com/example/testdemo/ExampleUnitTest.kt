package com.example.testdemo

import org.junit.Test

/**
 * Example local unit testBG, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val baseW = 800
        val baseH = 1280
        GenerateValueFiles(baseW, baseH, "").generate()
    }
}