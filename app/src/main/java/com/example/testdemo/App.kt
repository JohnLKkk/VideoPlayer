package com.example.testdemo

import android.app.Application
import android.content.Context
import com.yoy.v_Base.utils.SPUtils

class App : Application(){
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        SPUtils.init(this)
    }
}