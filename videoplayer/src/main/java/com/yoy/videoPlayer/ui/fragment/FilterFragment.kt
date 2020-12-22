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
    private val mHandler = Handler()

    //vflip is up and down, hflip is left and right
    private lateinit var txtArray: Array<String>

    private val filters = arrayOf(
            "——",
            "lutyuv='u=%s:v=%s'",
            "hue='h=%s:s=%s'",
            "lutrgb='r=%s:g=%s'",
            "edgedetect=low=%s:high=%s",
            "drawgrid=w=%s/3:h=%s/3:t=2:c=white@0.5",
            "colorbalance=bs=%s",
            "drawbox=x=%s:y=%s:w=%s:h=%s:color=red@0.5'",
            "vflip",
            "unsharp"
    )
    private var selectFilters: String? = ""
        set(value) {
            if (value == null) return
            filterTv.text = value
            mHandler.removeCallbacksAndMessages(null)
            mHandler.postDelayed({
                getPlayHandler().setFilterValue(value)
            }, 1000)

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
        arrayListOf("0", "0", "0", "0", "0").run {
            this.addAll(0, itemStr.split(","))
            this.removeAll(Collections.singleton(""))
            val stringBuilder = StringBuilder()
            for (a in this)
                stringBuilder.append(a).append(";")
            LogUtils.i(msg = stringBuilder.toString())
            val value = String.format(filters[filtersIndex], *this.toTypedArray())
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