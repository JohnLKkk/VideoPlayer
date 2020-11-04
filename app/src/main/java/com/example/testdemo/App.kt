package com.example.testdemo

import android.app.Application
import android.content.Context
import com.example.testdemo.utlis.SPUtils

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