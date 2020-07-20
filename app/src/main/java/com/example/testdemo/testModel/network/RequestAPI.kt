package com.example.testdemo.testModel.network

/**
 * Created by Void on 2019/7/11 17:25
 * 中山视频资源平台接口信息
 */
object RequestAPI {
    /*服务器授权的key等*/
    const val APP_Key = "APIdb54d7b7da5cd8396182acf4de05ea5d"
    const val APP_Account = "ACCab6c7a37192f15ba916a118c379a44a0"
    /*没有查询到您要的信息*/
    const val messageStateCode_8 = 8
    /*禁止非法访问*/
    const val messageStateCode_1000 = 1000
    /*接口请求超时,请重新请求*/
    const val messageStateCode_1001 = 1001
    /*签名匹配失败,请校正*/
    const val messageStateCode_1002 = 1002
    /*必填参数不能为空*/
    const val messageStateCode_1003 = 1003

    /*域名*/
    private const val baseUrl = "http://yunstudy.koo6.cn/"
    /*获取服务器时间戳*/
    const val getServerTime = "${baseUrl}Apis/Attribute/getServerTime"
    /*获取学科列表*/
    const val getSubjectList = "${baseUrl}Apis/Attrbute/getSubjectList"
    /*获取学段列表*/
    const val getPhaseList = "${baseUrl}Apis/Attrbute/getPhaseList"
    /*获取年级列表*/
    const val getClassList = "${baseUrl}Apis/Attrbute/getClassList"
    /*获取年份列表*/
    const val getYearLis = "${baseUrl}Apis/Attrbute/getYearLis"
    /*获取版本列表*/
    const val getVersionList = "${baseUrl}Apis/Attrbute/getVersionList"
    /*获取学期列表*/
    const val getSemesterList = "${baseUrl}Apis/Attrbute/getSemesterList"
    /*获取同步书本列表*/
    const val getSynchroBookList = "${baseUrl}Apis/Ai/getSynchroBookList"
    /*获取同步书本的所有播放视频列表*/
    const val getSynchroVideoList = "${baseUrl}Apis/Ai/getSynchroVideoList"
    /*获取知识点视频播放列表*/
    const val getKnowledgeVideoLis = "${baseUrl}Apis/Ai/getKnowledgeVideoLis"
    /*获取作文、奥数专题视频播放列表*/
    const val getSpecialVideoList = "${baseUrl}Apis/Ai/getSpecialVideoList"
    /*获取视频播放信息*/
    const val getVideoUrl = "${baseUrl}Apis/Ai/getVideoUrl"
}