package com.yoy.videoPlayer.ui.video

import android.content.Intent
import android.view.KeyEvent
import com.yoy.v_Base.ui.BaseDefaultActivity
import com.yoy.v_Base.utils.AppCode
import com.yoy.v_Base.utils.FileTools
import com.yoy.v_Base.utils.LogUtils
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.beans.VideoFileInfo
import com.yoy.videoPlayer.processing.PlayVideoHandler
import com.yoy.videoPlayer.utils.PlayHistoryManager

/**
 * Created by Void on 2020/12/3 14:41
 * 视频处理界面
 */
class MainVideoActivity : BaseDefaultActivity() {
    private val TAG = MainVideoActivity::class.java.simpleName
    private var lastClickBackTime = System.currentTimeMillis()

    val playHandler = PlayVideoHandler()

    override fun getLayoutID(): Int = R.layout.activity_video_process

    override fun onInit() {
        mPresenter = VideoProcessPresenter(this)
        uiControl = VideoProcessUiControl(this)
    }

    override fun isFullScreenWindow(): Boolean = true

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onRelease()
        uiControl.onRelease()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppCode.selectFileResultCode) {
            val uri = data?.data
            if (uri == null) {
                LogUtils.d(TAG, "选择文件路径结果为空！")
                return
            }
            val path = FileTools.getFilePathByUri(applicationContext, uri) ?: return
            (mPresenter as VideoProcessPresenter).selectFileResult(path)
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
}