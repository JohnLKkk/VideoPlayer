package com.example.testdemo

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit testBG, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        GenerateValueFiles.main()
        assertEquals(4, 2 + 2)
    }
}
