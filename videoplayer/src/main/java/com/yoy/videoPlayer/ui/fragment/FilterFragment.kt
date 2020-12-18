package com.yoy.videoPlayer.ui.fragment

import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.yoy.v_Base.ui.BaseDefaultFragment
import com.yoy.videoPlayer.R

/**
 * Created by Void on 2020/12/18 14:52
 *
 */
class FilterFragment : BaseDefaultFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var filterTypeSelect: Spinner
    private lateinit var filterTv: TextView
    private lateinit var inputEt: EditText


    //vflip is up and down, hflip is left and right
    private lateinit var txtArray: Array<String>

    private val filters = arrayOf(
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

    override fun getLayoutID(): Int = R.layout.layout_filter

    override fun initView(view: View) {
        super.initView(view)
        getArrayRes(R.array.FilterType)
        filterTypeSelect = view.findViewById(R.id.filterTypeSelect)
        filterTv = view.findViewById(R.id.filterTv)
        inputEt = view.findViewById(R.id.inputEt)
    }

    override fun initListener(view: View) {
        super.initListener(view)
        filterTypeSelect.onItemSelectedListener = this
    }

    fun getFilterFormat(index: Int, itemStr: String) {
        val itemArray = itemStr.split(",")
        val value = String.format(filters[index],
                itemArray[0],
                itemArray[1],
                itemArray[2],
                itemArray[3],
                itemArray[4]
        )
        filterTv.text = activity?.getString(R.string.filterHint, txtArray[index], value)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        getFilterFormat(position, "0,0,0,0,0")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}