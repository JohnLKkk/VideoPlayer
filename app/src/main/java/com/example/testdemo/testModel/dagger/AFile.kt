package com.example.testdemo.testModel.dagger

import android.util.Log

class AFile {
    fun run() {
        Log.d("dagger learn", "AFile——run")
    }

    init {
        Log.d("dagger learn", "AFile 构造方法")
    }
}