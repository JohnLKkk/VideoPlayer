package com.yoy.v_Base.utils

import android.Manifest

/**
 * Created by Void on 2020/7/10 14:22
 *
 */
object AppCode {

    /*
    * 运行app所需的基本权限 todo
    * 目前的逻辑是没有这些权限将无法使用2020-06-04 10:47
    * 将权限声明在这里是因为不仅仅是在权限请求布局需要权限判断，在其他的地方，
    * 如:WakeupService 也会有需要判断权限的情况。这是历史代码遗留的问题，不方便且没时间修改。
    * */
    val basePermissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    )

    //region ------------------ 一些通用的值
    //获取麦克风失败时最大的尝试次数
    const val tryGetRecorderFailMaxCount = 10
    //endregion

    //region ------------------ 用于储存SharedPreferences数据所使用的key
    //当前项目使用的解码方式，这个值必定是最新的，每次变更解码方式都会更新该值，注意：这不是线程安全的
    const val currentDecodeType = "currentDecodeType"

    //endregion
}