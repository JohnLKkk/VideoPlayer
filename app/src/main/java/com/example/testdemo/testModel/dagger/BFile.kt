package com.example.testdemo.testModel.dagger

import android.util.Log
import javax.inject.Inject

class BFile @Inject constructor() {
    fun bRun() {
        Log.d("dagger learn", "BFile——run")
    }

    init {
        Log.d("dagger learn", "BFile 构造方法")
    }
}