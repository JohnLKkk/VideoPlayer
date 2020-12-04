package com.yoy.videoPlayer.ui

/**
 * Created by Void on 2020/12/3 17:23
 *
 */
interface FragmentCallback {
    /**
     * 选择功能回调
     * @param type 功能类型 0倍速 1功能列表 2解码方式
     * @param position 功能列表索引
     */
    fun onSelectFunction(type: Int, position: Int)

    /**
     * 播放控制
     * @param action 操作类型 0播放 1暂停 2快退 3快进
     */
    fun onPlayControl(action: Int)

}