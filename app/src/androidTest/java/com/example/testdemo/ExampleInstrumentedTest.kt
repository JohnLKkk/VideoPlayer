package com.example.testdemo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.testdemo.testModel.coroutine.CoroutineTest
import com.example.testdemo.testModel.dagger.DaggerLearnActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @JvmField
    @Rule
    var mActivityRule = ActivityTestRule(DaggerLearnActivity::class.java)

    @Test
    fun useAppContext() {
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        CoroutineTest().test()
    }

    @Test
    fun testTextViewDisplay() {
        println("----------------单元测试----------------")
        onView(withId(R.id.onBtn)).perform(click())

        onView(withText("你好单元测试")).perform(click())
//        onView(withText("你好单元测试")).check(ViewAssertions.matches(isDisplayed()))
    }
}