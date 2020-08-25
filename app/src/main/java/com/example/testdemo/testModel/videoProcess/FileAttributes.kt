package com.example.testdemo.testModel.videoProcess

import java.io.File

/**
 * Created by Void on 2020/8/17 18:51
 * 文件属性
 * name 我是说
 * path 路径
 * size 文件大小 单位：bit
 * fileType 文件类型，如：.mp4  .mkv
 */
class FileAttributes {
    var name: String = "-error-"
    var path: String = ""
    var size: Long = 0L
    var fileType: String = ""
    var isValid=false
    lateinit var playFile: File
    fun initData(path: String) {
        playFile = File(path)
        this.path = path
        size = playFile.length()
        playFile.name.run {
            val index = lastIndexOf(".")
            name = substring(0, index)
            fileType = substring(index, length)
        }
        isValid=true
    }

}