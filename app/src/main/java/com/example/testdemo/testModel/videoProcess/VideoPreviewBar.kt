package com.example.testdemo.testModel.videoProcess

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TextureView
import android.widget.RelativeLayout
import android.widget.SeekBar
import com.example.testdemo.R

/**
 * Created by Void on 2020/9/23 15:50
 * 自定义进度条
 */
class VideoPreviewBar(context: Context, attributeSet: AttributeSet) :
        RelativeLayout(context, attributeSet),
        SeekBar.OnSeekBarChangeListener {
    private lateinit var textureView: TextureView
    private lateinit var videoProgressView: SeekBar
    var callback: ProgressCallback? = null

    init {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_preview_video, this)
        textureView = view.findViewById(R.id.videoPreviewView)
        videoProgressView = view.findViewById(R.id.videoScheduleView)
        videoProgressView.setOnSeekBarChangeListener(this)
    }

    fun setProgress(index: Int) {
        if (index < 0 || index > 100) return
        videoProgressView.progress = index
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar == null) return
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        if (seekBar == null) return
        callback?.onTouchCallback(0, seekBar.progress)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (seekBar == null) return
        callback?.onTouchCallback(1, seekBar.progress)
    }

    interface ProgressCallback {
        /**
         * @param index 当前进度
         * @param fromUser 是否来自用户
         */
        fun onChangeProgress(index: Int, fromUser: Boolean)

        /**
         * 触碰回调
         * @param type 0开始触碰 1停止触碰
         * @param index 当前播放进度
         */
        fun onTouchCallback(type: Int, index: Int)
    }
}