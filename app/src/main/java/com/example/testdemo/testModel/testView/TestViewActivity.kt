package com.example.testdemo.testModel.testView

import android.os.Bundle
import com.example.testdemo.R
import com.yoy.v_Base.ui.BaseDefaultActivity

class TestViewActivity : BaseDefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar("视图测试",true)
    }

    override fun getLayoutID(): Int = R.layout.activity_test_view

    override fun isFullScreenWindow(): Boolean = true
}