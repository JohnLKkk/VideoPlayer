package com.yoy.videoPlayer

import android.app.Application
import android.content.Context
import com.yoy.v_Base.utils.SPUtils
import com.yoy.videoPlayer.utils.PlayHistoryManager
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
        PlayHistoryManager.init(context)
    }
    /*
素描  "lutyuv='u=128:v=128'"
鲜明  "hue='h=60:s=-3'"
暖蓝  "lutrgb='r=0:g=0'"
边缘  "edgedetect=low=0.1:high=0.4"
九宫格 "drawgrid=w=iw/3:h=ih/3:t=2:c=white@0.5"
均衡  "colorbalance=bs=0.3"
矩形  "drawbox=x=100:y=100:w=100:h=100:color=red@0.5'"
翻转  "vflip"
锐化  "unsharp"
    *
    *
    * */
}