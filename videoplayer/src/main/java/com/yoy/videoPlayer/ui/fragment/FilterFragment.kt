package com.yoy.videoPlayer.ui.fragment

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.yoy.v_Base.ui.BaseDefaultFragment
import com.yoy.v_Base.utils.LogUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.processing.PlayVideoHandler
import com.yoy.videoPlayer.ui.VideoProcessActivity
import java.util.*

/**
 * Created by Void on 2020/12/18 14:52
 *
 */
class FilterFragment(private val mActivity: VideoProcessActivity) : BaseDefaultFragment(),
        AdapterView.OnItemSelectedListener {

    private lateinit var filterTypeSelect: Spinner
    private lateinit var filterTv: TextView
    private lateinit var inputEt: EditText
    //vflip is up and down, hflip is left and right
    private lateinit var txtArray: Array<String>

    /*
素描  "lutyuv='u=128:v=128'"
鲜明  "hue='h=60:s=-3'"
暖蓝  "lutrgb='r=0:g=0'"
边缘  "edgedetect=low=0.1:high=0.4"
九宫格 "drawgrid=w=iw/3:h=ih/3:t=2:c=white@0.5"
均衡  "colorbalance=bs=0.3"
矩形  "drawbox=x=100:y=100:w=100:h=100:color=red@0.5'"
翻转  "vflip"
锐化  "unsharp"
    *
    *
    * */
    private val filters = arrayOf(
            "——",
            "lutyuv='u=128:v=128'",//素描
            "hue='h=60:s=-3'",//鲜明
            "lutrgb='r=0:g=0'",//暖蓝
            "edgedetect=low=0.1:high=0.4",//边缘
            "drawgrid=w=iw/3:h=ih/3:t=2:c=white@0.5",//九宫格
            "colorbalance=bs=0.3",//均衡
            "drawbox=x=100:y=100:w=100:h=100:color=red@0.5'",//矩形
            "vflip",//翻转
            "unsharp"//锐化
    )

    //    private val filters = arrayOf(
//            "——",
//            "lutyuv='u=%s:v=%s'",//素描
//            "hue='h=%s:s=%s'",//鲜明
//            "lutrgb='r=%s:g=%s'",//暖蓝
//            "edgedetect=low=%s:high=%s",//边缘
//            "drawgrid=w=%s/3:h=%s/3:t=2:c=white@0.5",//九宫格
//            "colorbalance=bs=%s",//均衡
//            "drawbox=x=%s:y=%s:w=%s:h=%s:color=red@0.5'",//矩形
//            "vflip",//翻转
//            "unsharp"//锐化
//    )
    private var selectFilters: String? = ""
        set(value) {
            if (value == null) return
            filterTv.text = value
            getPlayHandler().setFilterValue(value)

            field = value
        }
    private var filtersIndex = 0

    override fun getLayoutID(): Int = R.layout.layout_filter

    override fun initView(view: View) {
        super.initView(view)
        txtArray = getArrayRes(R.array.FilterType) ?: arrayOf(String())
        filterTypeSelect = view.findViewById(R.id.filterTypeSelect)
        filterTv = view.findViewById(R.id.filterTv)
        inputEt = view.findViewById(R.id.inputEt)
    }

    override fun initListener(view: View) {
        super.initListener(view)
        filterTypeSelect.onItemSelectedListener = this
        inputEt.addTextChangedListener(textChangeListener)
    }

    fun getPlayHandler(): PlayVideoHandler = mActivity.playHandler

    /**
     * 格式化参数
     * @param itemStr 输入的参数
     */
    fun getFilterFormat(itemStr: String) {
        if (filtersIndex == 0) return
        arrayListOf("0", "0", "0", "0", "0").run {
            this.addAll(0, itemStr.split(","))
            this.removeAll(Collections.singleton(""))
            val stringBuilder = StringBuilder()
            for (a in this)
                stringBuilder.append(a).append(";")
//            LogUtils.i(msg = stringBuilder.toString())
//            val value = String.format(filters[filtersIndex], *this.toTypedArray())
            val value = filters[filtersIndex]
            selectFilters = activity?.getString(R.string.filterHint, txtArray[filtersIndex], value)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        this.filtersIndex = position
        getFilterFormat("0,0,0,0,0")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            getFilterFormat(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
//            val text = s.toString()
//            if (s == null || TextUtils.isEmpty(text)) return
//            if (text.endsWith(",")) return
//            s.append(",")
        }
    }
}