package com.yoy.videoPlayer.ui.video.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.yoy.v_Base.ui.BaseDefaultFragment
import com.yoy.v_Base.utils.ToastUtils
import com.yoy.videoPlayer.R
import com.yoy.videoPlayer.processing.PlayVideoHandler
import com.yoy.videoPlayer.ui.video.MainVideoActivity

/**
 * Created by Void on 2020/12/18 14:52
 *
 */
class FilterFragment(private val mActivityMain: MainVideoActivity) : BaseDefaultFragment(),
        AdapterView.OnItemSelectedListener {

    private lateinit var filterTypeSelect: Spinner
    private lateinit var filterTv: TextView
    private lateinit var inputEt: EditText

    private lateinit var txtArray: Array<String>

    //vflip is up and down, hflip is left and right
    private val filtersHint = arrayOf(
            "lutyuv='u=128:v=128'",//素描
            "hue='h=60:s=-3'",//鲜明
            "transpose=2",//旋转90
            "drawtext=\"fontsize=100:fontcolor=white:text='hello world':x=(w-text_w)/2:y=(h-text_h)/2\"",//文字
            "drawgrid=w=iw/3:h=ih/3:t=2:c=white@0.5",//九宫格
            "drawbox=x=100:y=100:w=100:h=100:color=red@0.5'",//矩形
            "vflip",//翻转
            "unsharp"//锐化
    )

    private var filtersIndex = 0

    private var selectFilters: String? = ""
        set(value) {
            if (value == null) return
            filterTv.text = activity?.getString(R.string.currentFilter, txtArray[filtersIndex], value)
            getPlayHandler().setFilterValue(value)
            field = value
        }

    override fun getLayoutID(): Int = R.layout.layout_filter

    override fun initView(view: View) {
        super.initView(view)
        txtArray = getArrayRes(R.array.FilterType)
        filterTypeSelect = view.findViewById(R.id.filterTypeSelect)
        filterTv = view.findViewById(R.id.filterTv)
        inputEt = view.findViewById(R.id.inputEt)
    }

    override fun initListener(view: View) {
        super.initListener(view)
        filterTypeSelect.onItemSelectedListener = this
        inputEt.addTextChangedListener(textChangeListener)
    }

    fun getPlayHandler(): PlayVideoHandler = mActivityMain.playHandler

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (!getPlayHandler().getFilterChangeState()) {
            ToastUtils.showShort(context, "暂时无法修改滤镜……")
            filterTypeSelect.setSelection(filtersIndex)
            return
        }
        this.filtersIndex = position
        selectFilters = filtersHint[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            selectFilters = s.toString()
        }

        override fun afterTextChanged(s: Editable?) {
//            val text = s.toString()
//            if (s == null || TextUtils.isEmpty(text)) return
//            if (text.endsWith(",")) return
//            s.append(",")
        }
    }
}