package com.yoy.videoPlayer.beans

import java.io.File

/**
 * Created by Void on 2020/12/8 11:41
 * 视频文件信息
 */
class VideoFileInfo(val vName: String, val vPath: String) {
    constructor(vPath: String) : this(File(vPath).name, vPath)
}