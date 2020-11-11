package com.yoy.videoplayer

import android.app.Application
import android.content.Context
import com.yoy.v_Base.utils.SPUtils
import io.github.prototypez.appjoint.core.AppSpec

/**
 * Created by Void on 2020/11/11 15:22
 *
 */
@AppSpec
class VideoApplication : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        SPUtils.init(this)
    }
}