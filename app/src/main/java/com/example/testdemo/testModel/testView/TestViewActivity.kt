package com.example.testdemo.testModel.testView

import com.example.testdemo.R
import com.yoy.v_Base.ui.BaseDefaultActivity

class TestViewActivity : BaseDefaultActivity() {

    override fun getLayoutID(): Int = R.layout.activity_test_view

    override fun onInit() {
        setActionBar("视图测试",true)
    }

    override fun isFullScreenWindow(): Boolean = true
}