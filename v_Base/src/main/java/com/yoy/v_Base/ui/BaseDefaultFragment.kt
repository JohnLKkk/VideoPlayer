package com.yoy.v_Base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by Void on 2020/12/4 13:42
 *
 */
abstract class BaseDefaultFragment : Fragment(), View.OnClickListener {

    abstract fun getLayoutID(): Int
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutID(), null).apply {
            initView(this)
            initListener(this)
            initData(this)
        }
    }

    override fun onClick(v: View?) {}

    open fun initView(view: View) {}

    open fun initListener(view: View) {}

    open fun initData(view: View) {}
}

