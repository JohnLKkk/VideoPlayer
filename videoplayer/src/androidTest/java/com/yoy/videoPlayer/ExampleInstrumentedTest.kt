package com.yoy.videoPlayer

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.yoy.videoPlayer.beans.VideoFileInfo
import com.yoy.videoPlayer.utils.PlayHistoryManager
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
        Log.e("SQL Test", "start------------------------")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        PlayHistoryManager.init(appContext)
        //第一次查询
        PlayHistoryManager.queryData().apply {
            for (q in this) {
                Log.e("SQL Test", "第一次查询:${q.vName}-${q.vPath}")
            }
        }
        //插入数据
        PlayHistoryManager.insertData(LinkedList<VideoFileInfo>().apply {
            add(VideoFileInfo("第一个", "第一个.mp4"))
            add(VideoFileInfo("第二个", "第二个.mp4"))
            add(VideoFileInfo("第二z个", "第二个z.mp4"))
        })
        //第二次查询：验证插入数据
        PlayHistoryManager.queryData().apply {
            for (q in this) {
                Log.e("SQL Test", "第二次查询：验证插入数据:${q.vName}-${q.vPath}")
            }
        }
        //删除数据1
        PlayHistoryManager.deleteData(name = "第一个")
        //第三次查询：验证删除数据1
        PlayHistoryManager.queryData().apply {
            for (q in this) {
                Log.e("SQL Test", "第三次查询：验证删除数据1:${q.vName}-${q.vPath}")
            }
        }
        //删除数据2
        PlayHistoryManager.deleteData(path = "第二个.mp4")
        //第三次查询：验证删除数据2
        PlayHistoryManager.queryData().apply {
            for (q in this) {
                Log.e("SQL Test", "第四次查询：验证删除数据2:${q.vName}-${q.vPath}")
            }
        }
        Log.e("SQL Test", "end------------------------")
    }
}