package com.example.testdemo

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.testdemo.testModel.audioRecorder.AudioRecorderActivity
import com.example.testdemo.testModel.barrierFree.BarrierFreeActivity
import com.example.testdemo.testModel.broadcast.BroadcastActivity
import com.example.testdemo.testModel.coroutine.CoroutineTest
import com.example.testdemo.testModel.dagger.DaggerLearnActivity
import com.example.testdemo.testModel.jniTest.JniActivity
import com.example.testdemo.testModel.notification.NotificationActivity
import com.example.testdemo.testModel.onClickTest.ClickActivity
import com.example.testdemo.testModel.permission.PermissionActivity
import com.example.testdemo.testModel.popupWindow.PopupWindowActivity
import com.example.testdemo.testModel.scanDevicesIP.ScanIPActivity
import com.example.testdemo.testModel.startPhoneApp.StartPhoneAppActivity
import com.example.testdemo.testModel.testView.TestViewActivity
import com.example.testdemo.testModel.viewpager.ViewPagerActivity
import com.yoy.v_Base.ui.BaseDefaultActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 该项目主要用于测试模块，这个项目不应该依赖于任何项目;
 * 当开发新的模块时，使用这个项目编译时间会快于app_ai项目，
 * 任何新的模块都应该在这个项目上测试通过功能没问题后，才应用于主项目。
 *
 * 建议需要测试新的模块时，新建一个activity，不要删除旧的。
 */
class MainActivity : BaseDefaultActivity(), AdapterView.OnItemClickListener {
    private val functionList = ArrayList<String>().apply {
        add("点击测试")
        add("弹窗测试")
        add("广播测试")
        add("视图测试")
        add("权限测试")
        add("JNI测试")
        add("协程学习")
        add("扫描设备IP")
        add("监听通知测试")
        add("无障碍服务测试")
        add("打开系统任意APP")
        add("Android原生录音测试")
        add("ViewpagerFragment内容测试")
        add("Dagger 2 学习")
    }
    private var autoTestActivity = true

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun onInit() {
        val lvAdapter = LvAdapter()
        mainListView.adapter = lvAdapter
        mainListView.onItemClickListener = this
        lvAdapter.notifyDataSetChanged()
        if (autoTestActivity) startActivity(Intent(this, DaggerLearnActivity::class.java))
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> startActivity(Intent(this, ClickActivity::class.java))
            1 -> startActivity(Intent(this, PopupWindowActivity::class.java))
            2 -> startActivity(Intent(this, BroadcastActivity::class.java))
            3 -> startActivity(Intent(this, TestViewActivity::class.java))
            4 -> startActivity(Intent(this, PermissionActivity::class.java))
            5 -> startActivity(Intent(this, JniActivity::class.java))
            6 -> CoroutineTest().test()
            7 -> startActivity(Intent(this, ScanIPActivity::class.java))
            8 -> startActivity(Intent(this, NotificationActivity::class.java))
            9 -> startActivity(Intent(this, BarrierFreeActivity::class.java))
            10 -> startActivity(Intent(this, StartPhoneAppActivity::class.java))
            11 -> startActivity(Intent(this, AudioRecorderActivity::class.java))
            12 -> startActivity(Intent(this, ViewPagerActivity::class.java))
            13 -> startActivity(Intent(this, DaggerLearnActivity::class.java))
        }
    }

    inner class LvAdapter : BaseAdapter() {

        override fun getCount(): Int = functionList.size

        override fun getItem(position: Int): String = functionList[position]

        override fun getItemId(position: Int): Long = 0
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View =
            TextView(this@MainActivity).apply {
                setTextColor(Color.BLACK)
                setPadding(20, 20, 0, 20)
                text = getItem(position)
                textSize = 22f
            }
    }
}
