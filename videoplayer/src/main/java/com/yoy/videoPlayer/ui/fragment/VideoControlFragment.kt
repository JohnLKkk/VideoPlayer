package com.yoy.videoPlayer.ui.fragment

import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import com.yoy.v_Base.ui.BaseDefaultFragment
import com.yoy.v_Base.utils.KLog
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.ui.FragmentCallback
import com.yoy.videoPlayer.ui.MainVideoActivity
import com.yoy.videoPlayer.ui.view.PlayHistoryPopupWindow

/**
 * Created by Void on 2020/12/3 14:47
 *
 */
class VideoControlFragment : BaseDefaultFragment(), AdapterView.OnItemSelectedListener {
    private val TAG = VideoControlFragment::class.java.simpleName
    private var historyWindow: PlayHistoryPopupWindow? = null
    private var callback: FragmentCallback? = null
    private lateinit var doubleSpeedList: Spinner
    private lateinit var functionList: Spinner
    private lateinit var decoderTypeList: Spinner

    private var isInitFinish = false

    override fun getLayoutID(): Int = R.layout.fragment_video_control

    override fun initView(view: View) {
        super.initView(view)
        doubleSpeedList = view.findViewById(R.id.doubleSpeedList)
        functionList = view.findViewById(R.id.functionList)
        decoderTypeList = view.findViewById(R.id.decoderTypeList)
        historyWindow = PlayHistoryPopupWindow(view.context)
    }

    override fun initListener(view: View) {
        super.initListener(view)
        view.findViewById<Button>(R.id.selectFileBtn).setOnClickListener(this)
        view.findViewById<Button>(R.id.videoHistory).setOnClickListener(this)
        view.findViewById<Button>(R.id.playBtn).setOnClickListener(this)
        view.findViewById<Button>(R.id.stopBtn).setOnClickListener(this)
        view.findViewById<Button>(R.id.goBackBtn).setOnClickListener(this)
        view.findViewById<Button>(R.id.forwardBtn).setOnClickListener(this)
        //setSelection需要在注册事件之前
        doubleSpeedList.setSelection(0,true)
        functionList.setSelection(0,true)
        decoderTypeList.setSelection(0,true)
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
                if (activity is MainVideoActivity)
                    (activity as MainVideoActivity).openSelectFileView()
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
    fun setSelectFunctionUI(type: Int, position: Int) {
        if (!isInitFinish) return
        when (type) {
            0 -> if (position < doubleSpeedList.adapter.count)
                doubleSpeedList.setSelection(position)
            1 -> if (position < functionList.adapter.count)
                functionList.setSelection(position)
            2 -> if (position < decoderTypeList.adapter.count)
                decoderTypeList.setSelection(position)
        }
    }
}