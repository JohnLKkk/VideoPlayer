package com.yoy.videoPlayer.ui.fragment

import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import com.yoy.v_Base.ui.BaseDefaultFragment
import com.yoy.v_Base.utils.KLog
import com.yoy.v_Base.utils.LogUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.processing.VideoPreviewBar
import com.yoy.videoPlayer.ui.FragmentCallback
import com.yoy.videoPlayer.ui.VideoProcessActivity

/**
 * Created by Void on 2020/12/3 14:47
 *
 */
class VideoControlFragment(private val callback: FragmentCallback) : BaseDefaultFragment(),
        AdapterView.OnItemSelectedListener{
    private val TAG = VideoControlFragment::class.java.simpleName
    private lateinit var selectFileBtn: Button
    private lateinit var videoHistory: Button
    private lateinit var playBtn: Button
    private lateinit var stopBtn: Button
    private lateinit var goBackBtn: Button
    private lateinit var forwardBtn: Button
    private lateinit var doubleSpeedList: Spinner
    private lateinit var functionList: Spinner
    private lateinit var decoderTypeList: Spinner

    override fun getLayoutID(): Int = R.layout.fragment_video_control

    override fun initView(view: View) {
        super.initView(view)
        selectFileBtn = view.findViewById(R.id.selectFileBtn)
        videoHistory = view.findViewById(R.id.videoHistory)
        playBtn = view.findViewById(R.id.playBtn)
        stopBtn = view.findViewById(R.id.stopBtn)
        goBackBtn = view.findViewById(R.id.goBackBtn)
        forwardBtn = view.findViewById(R.id.forwardBtn)
        doubleSpeedList = view.findViewById(R.id.doubleSpeedList)
        functionList = view.findViewById(R.id.functionList)
        decoderTypeList = view.findViewById(R.id.decoderTypeList)
    }

    override fun initListener(view: View) {
        super.initListener(view)
        selectFileBtn.setOnClickListener(this)
        videoHistory.setOnClickListener(this)
        playBtn.setOnClickListener(this)
        stopBtn.setOnClickListener(this)
        goBackBtn.setOnClickListener(this)
        forwardBtn.setOnClickListener(this)
        doubleSpeedList.onItemSelectedListener = this
        functionList.onItemSelectedListener = this
        decoderTypeList.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.doubleSpeedList -> callback.onSelectFunction(0, position)
            R.id.functionList -> callback.onSelectFunction(1, position)
            R.id.decoderTypeList -> callback.onSelectFunction(2, position)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        KLog.e(TAG, "onNothingSelected")
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.selectFileBtn -> try {
                (activity as VideoProcessActivity).openSelectFileView()
            } catch (e: Exception) {
            }

            R.id.videoHistory -> {
                LogUtils.d(msg = "播放历史")
            }
            R.id.playBtn -> {
                LogUtils.d(msg = "播放")
            }
            R.id.stopBtn -> {
                LogUtils.d(msg = "停止")
            }
            R.id.goBackBtn -> {
                LogUtils.d(msg = "快退")
            }
            R.id.forwardBtn -> {
                LogUtils.d(msg = "快进")
            }
        }
    }
}