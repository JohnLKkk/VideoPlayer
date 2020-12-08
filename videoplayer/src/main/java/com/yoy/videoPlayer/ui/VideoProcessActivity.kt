package com.yoy.videoPlayer.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.v_Base.utils.FileTools
import com.yoy.v_Base.utils.LogUtils
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.VideoApplication
import com.yoy.videoPlayer.beans.VideoFileInfo
import com.yoy.videoPlayer.utils.PlayHistoryManager

/**
 * Created by Void on 2020/12/3 14:41
 * 视频处理界面
 */
class VideoProcessActivity : BaseDefaultActivity() {
    private val TAG = VideoProcessActivity::class.java.simpleName
    private lateinit var uiControl: VideoProcessUiControl
    private lateinit var mPresenter: VideoProcessPresenter
    private var lastClickBackTime = System.currentTimeMillis()
    private val selectFileResultCode = 1001

    override fun getLayoutID(): Int = R.layout.activity_video_process

    override fun isFullScreenWindow(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiControl = VideoProcessUiControl(this)
        mPresenter = VideoProcessPresenter(this, uiControl)
        uiControl.setPresenter(mPresenter)
        if (!TextUtils.isEmpty(mPresenter.videoPath)) mPresenter.selectFileResult(mPresenter.videoPath)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onRelease()
        uiControl.onRelease()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == selectFileResultCode) {
            val uri = data?.data
            if (data == null || uri == null) {
                LogUtils.d(TAG, "选择文件路径结果为空！")
                return
            }
            val path = FileTools.getFilePathByUri(applicationContext, uri) ?: return
            mPresenter.selectFileResult(path)
            PlayHistoryManager.insertData(VideoFileInfo(path))
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (System.currentTimeMillis() - lastClickBackTime < 1500) {
                finish()
                true
            } else {
                lastClickBackTime = System.currentTimeMillis()
                ToastUtils.showLong(applicationContext, "请再次点击返回键退出")
                false
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    fun openSelectFileView() {
        try {
            startActivityForResult(Intent.createChooser(
                    Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "*/*"
                        addCategory(Intent.CATEGORY_OPENABLE)
                    }, "选择视频文件"), selectFileResultCode)
        } catch (ex: ActivityNotFoundException) {
            ToastUtils.showShort(VideoApplication.context, "没有找到文件管理器！")
        }
    }
}