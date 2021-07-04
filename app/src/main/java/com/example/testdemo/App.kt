package com.example.testdemo

import android.app.Application
import com.yoy.v_Base.utils.SPUtils

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        SPUtils.init(this)
    }
}