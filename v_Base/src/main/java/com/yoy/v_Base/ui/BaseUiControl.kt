package com.yoy.v_Base.ui

import android.view.View

/**
 * Created by Void on 2020/12/25 10:07
 *
 */
abstract class BaseUiControl(val mActivity: BaseDefaultActivity) : View.OnClickListener {
    val fragmentManager = mActivity.supportFragmentManager

    abstract fun getPresenter(): BasePresenter

    abstract fun initView()

    abstract fun initListener()

    abstract fun initData()

    abstract fun onRelease()

    override fun onClick(v: View?) {
    }

    open fun getActivityObj(): BaseDefaultActivity = mActivity

    /**
     * 初始化运行顺序，有需要子类可以重写该方法
     */
    fun runSeq(){
        initView()
        initListener()
        initData()
    }
}

