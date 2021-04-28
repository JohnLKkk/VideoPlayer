package com.yoy.v_Base.ui

/**
 * Created by Void on 2020/12/25 10:48
 *
 */
abstract class BasePresenter(val mActivity: BaseDefaultActivity) {

    abstract fun getUiControl(): BaseUiControl

    abstract fun onRelease()

    open fun getActivityObj(): BaseDefaultActivity = mActivity

    /**
     * 该方法在
     * @see BaseUiControl.runSeq 中调用
     */
    open fun initData()=Unit
}