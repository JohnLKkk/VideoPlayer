package com.example.testdemo.testModel.videoProcess

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.text.TextUtils
import android.view.SurfaceHolder
import com.example.testdemo.utlis.KLog

/**
 * Created by Void on 2020/7/27 17:27
 *
 */
class VideoPlayPresenter(private val mActivity: VideoPlayActivity,
                         private val uiControl: VideoPlayUiControl) :
        VideoPlayActivity.SelectFile, SurfaceHolder.Callback {
    private val mediaPlayer = MediaPlayer()
    private var surfaceHolder: SurfaceHolder? = null
    private val mainHandler = Handler()

    private var videoPath = ""

    init {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder().apply {
            this.setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        }.build())
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mediaPlayer.reset()
        mediaPlayer.setDisplay(holder)
    }

    override fun selectCallback(path: String?) {
        if (path == null || path.isEmpty()) {
            KLog.e("视频文件路径为null！")
            return
        }
        videoPath = path
        KLog.d("选中的文件路径是：$videoPath")
        mainHandler.post { onClickPlay() }
    }

    fun onClickPlay() {
        if (TextUtils.isEmpty(videoPath)) {//视频地址为空，进入选择路径流程
            mActivity.openFileSelectTools(this)
            return
        }
        playVideo(videoPath)
    }

    fun setSurfaceHolder(holder: SurfaceHolder) {
        this.surfaceHolder = holder
        holder.addCallback(this)
    }

    private fun playVideo(path: String) {
        KLog.d("即将播放的文件路径：$videoPath")
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}