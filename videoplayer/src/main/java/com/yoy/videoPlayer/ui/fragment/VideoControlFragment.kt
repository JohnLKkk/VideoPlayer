package com.yoy.videoPlayer.ui.fragment

import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.core.view.size
import com.yoy.v_Base.ui.BaseDefaultFragment
import com.yoy.v_Base.utils.KLog
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.ui.FragmentCallback
import com.yoy.videoPlayer.ui.VideoProcessActivity
import com.yoy.videoPlayer.ui.view.PlayHistoryPopupWindow

/**
 * Created by Void on 2020/12/3 14:47
 *
 */
class VideoControlFragment : BaseDefaultFragment(),
        AdapterView.OnItemSelectedListener {
    private val TAG = VideoControlFragment::class.java.simpleName
    private var historyWindow: PlayHistoryPopupWindow? = null
    private var callback: FragmentCallback? = null
    private lateinit var selectFileBtn: Button
    private lateinit var videoHistory: Button
    private lateinit var playBtn: Button
    private lateinit var stopBtn: Button
    private lateinit var goBackBtn: Button
    private lateinit var forwardBtn: Button
    private lateinit var doubleSpeedList: Spinner
    private lateinit var functionList: Spinner
    private lateinit var decoderTypeList: Spinner

    private var isInitFinish = false

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
        historyWindow = PlayHistoryPopupWindow(view.context)
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
        isInitFinish = true
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.doubleSpeedList -> callback?.onSelectFunction(0, position)
            R.id.functionList -> callback?.onSelectFunction(1, position)
            R.id.decoderTypeList -> callback?.onSelectFunction(2, position)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        KLog.e(TAG, "onNothingSelected")
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.selectFileBtn ->
                if (activity is VideoProcessActivity)
                    (activity as VideoProcessActivity).openSelectFileView()
            R.id.videoHistory -> {
                historyWindow?.setCallback(callback ?: return)
                historyWindow?.show(v)
            }
            R.id.playBtn -> callback?.onPlayControl(0)
            R.id.stopBtn -> callback?.onPlayControl(1)
            R.id.goBackBtn -> callback?.onPlayControl(2)
            R.id.forwardBtn -> callback?.onPlayControl(3)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.callback = null
    }

    fun setCallback(callback: FragmentCallback) {
        this.callback = callback
    }

    /**
     * 选择功能回调
     * @param type 功能类型 0倍速 1功能列表 2解码方式
     * @param position 功能列表索引
     */
    fun setSelectFuctionUI(type: Int, position: Int) {
        if (!isInitFinish) return
        when (type) {
            0 -> {
                if (position >= doubleSpeedList.size) return
                doubleSpeedList.setSelection(position)
            }
            1 -> {
                if (position >= functionList.size) return
                functionList.setSelection(position)
            }
            2 -> {
                if (position >= decoderTypeList.size) return
                decoderTypeList.setSelection(position)
            }
        }
    }
}