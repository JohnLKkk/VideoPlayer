package com.example.testdemo.testModel.ffmpeg

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import com.example.testdemo.R
import java.io.File

/**
 * Created by Void on 2020/7/27 17:14
 *
 */
class FFMPEGUiControl(private val mActivity: FFMPEGActivity) : View.OnClickListener, SurfaceHolder.Callback {
    private lateinit var mPresenter: FFMPEGPresenter
    private val mediaPlayer = MediaPlayer()
    private var playIv: ImageView = mActivity.findViewById(R.id.playIV)
    private val videoView: SurfaceView = mActivity.findViewById(R.id.videoView)
    private val surfaceHolder = videoView.holder

    init {
        playIv.setOnClickListener(this)
        surfaceHolder.addCallback(this)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder().apply {
            this.setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
        }.build())
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.playIV -> playVideo(mPresenter.videoPath)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mediaPlayer.reset()
        mediaPlayer.setDisplay(holder)
    }

    fun setPresenter(presenter: FFMPEGPresenter) {
        this.mPresenter = presenter
    }

    fun playVideo(path: String) {
        try {
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}