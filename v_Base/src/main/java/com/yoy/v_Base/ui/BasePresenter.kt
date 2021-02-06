package com.yoy.v_Base.ui

/**
 * Created by Void on 2020/12/25 10:48
 *
 */
abstract class BasePresenter(private val mActivity: BaseDefaultActivity) {

    abstract fun getUiControl(): BaseUiControl

    abstract fun onRelease()

    open fun getActivityObj(): BaseDefaultActivity = mActivity
}