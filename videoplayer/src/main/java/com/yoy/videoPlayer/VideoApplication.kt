package com.yoy.videoPlayer

import android.app.Application
import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.yoy.v_Base.utils.LogUtils
import com.yoy.v_Base.utils.SPUtils
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoPlayer.beans.VideoFileInfo
import com.yoy.videoPlayer.utils.FileUtils
import com.yoy.videoPlayer.utils.PlayHistoryManager
import io.github.prototypez.appjoint.core.AppSpec
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by Void on 2020/11/11 15:22
 *
 */
@AppSpec
class VideoApplication : Application() {
    companion object {
        lateinit var context: Context
        var defaultVideoPath = ""
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        SPUtils.init(this)
        PlayHistoryManager.init(context)
        thread {
            val vPath = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.path ?: ""
            if (TextUtils.isEmpty(vPath)) {
                LogUtils.e(msg = "复制文件失败，路径获取为null")
                ToastUtils.showShortInMainThread(context, "复制文件失败，路径获取为null")
                return@thread
            }
            if (FileUtils.outputAssetsFileToFolder(context, "video.mp4",
                            "video.mp4", vPath)) {
                defaultVideoPath = "$vPath/video.mp4"
                PlayHistoryManager.insertData(LinkedList<VideoFileInfo>().apply {
                    add(VideoFileInfo("defaultVideo", defaultVideoPath))
                })
            }
        }
    }
}