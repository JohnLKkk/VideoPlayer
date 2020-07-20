package com.example.testdemo

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.testdemo.testModel.audioRecorder.AudioRecorderActivity
import com.example.testdemo.testModel.barrierFree.BarrierFreeActivity
import com.example.testdemo.testModel.broadcast.BroadcastActivity
import com.example.testdemo.testModel.notification.NotificationActivity
import com.example.testdemo.testModel.permission.PermissionActivity
import com.example.testdemo.testModel.popupWindow.PopupWindowActivity
import com.example.testdemo.testModel.startPhoneApp.StartPhoneAppActivity
import com.example.testdemo.testModel.viewpager.ViewPagerActivity
import com.example.testdemo.utlis.KLog
import com.example.testdemo.testModel.testView.TestViewActivity
import kotlinx.android.synthetic.main.activity_main.*


/**
 * 该项目主要用于测试模块，这个项目不应该依赖于任何项目;
 * 当开发新的模块时，使用这个项目编译时间会快于app_ai项目，
 * 任何新的模块都应该在这个项目上测试通过功能没问题后，才应用于主项目。
 *
 * 建议需要测试新的模块时，新建一个activity，不要删除旧的。
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        startActivity(Intent(this, StartPhoneAppActivity::class.java))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) return
        KLog.d("全屏设置")
        window.apply {
            val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.apply {
                setPadding(0, 0, 0, 0)
                systemUiVisibility = uiOptions
                setOnSystemUiVisibilityChangeListener {
                    if (it and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        systemUiVisibility = uiOptions
                    }
                }
            }
            attributes = attributes.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
                horizontalMargin = 0.0f
                verticalMargin = 0.0f
                dimAmount = 0.0f
            }
        }
    }

    fun onClickTest(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent().apply {
                action = "com.robot.app_ai.action.bootStartUp"
                setPackage("com.robot.app_ai")
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } else {
            startService(Intent().apply {
                action = "com.robot.app_ai.action.bootStartUp"
                setPackage("com.robot.app_ai")
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
    }

    fun viewpagerFragment(view: View) {
        startActivity(Intent(this, ViewPagerActivity::class.java))
    }

    fun onDialogClick(view: View) {
        startActivity(Intent(this, PopupWindowActivity::class.java))
    }

    fun notificationClick(view: View) {
        startActivity(Intent(this, NotificationActivity::class.java))
    }

    fun onAudioRecorder(view: View) {
        startActivity(Intent(this, AudioRecorderActivity::class.java))
    }

    fun onBroadcastTest(view: View) {
        startActivity(Intent(this, BroadcastActivity::class.java))
    }

    fun onViewTest(view: View) {
        startActivity(Intent(this, TestViewActivity::class.java))
    }

    fun onBarrierFree(view: View) {
        startActivity(Intent(this, BarrierFreeActivity::class.java))
    }

    private var state = true

    fun onImageView(view: View) {
        if (state) {
            testImageView.background = ContextCompat.getDrawable(this, R.drawable.ic_play_audio_anim)
            (testImageView.background as AnimationDrawable).start()
        } else {
            testImageView.background = ContextCompat.getDrawable(this, R.drawable.ic_play_audio)
        }
        state = !state
    }

    fun onPermissionTest(view: View) {
        startActivity(Intent(this, PermissionActivity::class.java))
    }

    fun onOpenApp(view: View) {
        startActivity(Intent(this, StartPhoneAppActivity::class.java))
    }
}
