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
 *
 */
class VideoPreviewBar(context: Context, attributeSet: AttributeSet) :
        RelativeLayout(context, attributeSet),
        SeekBar.OnSeekBarChangeListener{
    private lateinit var textureView: TextureView
    private lateinit var videoProgressView: SeekBar

    init {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_preview_video, this)
        textureView = view.findViewById(R.id.videoPreviewView)
        videoProgressView = view.findViewById(R.id.videoScheduleView)


    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}