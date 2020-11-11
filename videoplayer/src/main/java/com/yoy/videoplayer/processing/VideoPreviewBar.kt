package com.yoy.videoplayer.processing

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TextureView
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.yoy.videoplayer.R

/**
 * Created by Void on 2020/9/23 15:50
 * 自定义进度条
 */
class VideoPreviewBar(context: Context, attributeSet: AttributeSet) :
        RelativeLayout(context, attributeSet),
        SeekBar.OnSeekBarChangeListener {
    private var textureView: TextureView
    private var videoProgressView: SeekBar
    var callback: ProgressCallback? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_preview_video, this)
        textureView = view.findViewById(R.id.videoPreviewView)
        videoProgressView = view.findViewById(R.id.videoScheduleView)
        videoProgressView.setOnSeekBarChangeListener(this)
    }

    /**
     * 设置进度条的进度
     * @param index 1-99
     * 注：因为UI的问题，当进度为0、100时，UI不好看，所以最小和最大进度分别是1和99
     */
    fun setProgress(index: Int) {
        when {
            index <= 1 -> videoProgressView.progress = 1
            index >= 99 -> videoProgressView.progress = 99
            else -> videoProgressView.progress = index
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar == null) return
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        if (seekBar == null) return
        callback?.onTouchCallback(0, seekBar.progress)
        videoProgressView.progressDrawable = ContextCompat.getDrawable(context, R.drawable.ic_video_seekbar_pre_bg)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (seekBar == null) return
        callback?.onTouchCallback(1, seekBar.progress)
        videoProgressView.progressDrawable = ContextCompat.getDrawable(context, R.drawable.ic_video_seekbar_bg)
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