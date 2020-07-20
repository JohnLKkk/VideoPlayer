package com.example.testdemo.testModel.network;

import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Response;
import okio.Okio;

/**
 * Created by Void on 2020/3/13 16:30
 * 中山视频资源调用管理
 */
public class YunStudyVideoManager {
    private static YunStudyVideoManager yunStudyVideoManager;
    private String TAG = YunStudyVideoManager.class.getSimpleName();

    public static YunStudyVideoManager getInstance() {
        if (yunStudyVideoManager == null) yunStudyVideoManager = new YunStudyVideoManager();
        return yunStudyVideoManager;
    }

    private YunStudyVideoManager() {
    }

    //region ---------------------- 提供出来的调用入口

    /**
     * 获取服务器时间戳
     *
     * @param callback 结果回调
     */
    public void getServerTimeRequest(ServiceResultCallback callback) {
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getServerTime,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取服务器时间戳 接口请求错误！");
                        callback.requestError(RequestAPI.getServerTime, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取服务器时间戳 接口请求成功！");
                        decodingBody(RequestAPI.getServerTime, response, callback);
                    }
                }));
    }

    /**
     * 访问获取学科列表数据
     *
     * @param xueduan    学段
     * @param deviceCode 约定的机器识别码，如mac、sn、imei等
     * @param callback   结果回调
     */
    public void getSubjectListRequest(String xueduan, String deviceCode, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getSubjectList,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getSubjectList, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getSubjectList, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(deviceCode + xueduan + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("xueduan", xueduan)
                .addBody("jgcode", deviceCode)
        );
    }

    /**
     * 获取学段列表数据
     */
    public void getPhaseListRequest(String deviceCode, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getPhaseList,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getPhaseList, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getPhaseList, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(deviceCode + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("jgcode", deviceCode)
        );
    }

    /**
     * 获取年纪列表数据
     *
     * @param xueduan 学段
     * @param xueke   学科
     */
    public void getClassListRequest(String xueduan, String xueke, String deviceCode, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getClassList,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getClassList, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getClassList, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(deviceCode + xueduan + xueke + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("xueduan", xueduan)
                .addBody("xueke", xueke)
                .addBody("jgcode", deviceCode)
        );
    }

    /**
     * 获取年份列表数据
     */
    public void getYearLisRequest(String deviceCode, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getYearLis,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getYearLis, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getYearLis, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(deviceCode + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("jgcode", deviceCode)
        );
    }

    /**
     * 获取版本列表数据
     *
     * @param nianji 年级
     */
    public void getVersionListRequest(String xueduan, String xueke, String nianji, String deviceCode, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getVersionList,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getVersionList, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getVersionList, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(deviceCode + nianji + xueduan + xueke + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("xueduan", xueduan)
                .addBody("xueke", xueke)
                .addBody("nianji", nianji)
                .addBody("jgcode", deviceCode)
        );
    }

    /**
     * 获取学期列表数据
     */
    public void getSemesterListRequest(String xueduan, String xueke, String deviceCode, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getSemesterList,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getSemesterList, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getSemesterList, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(deviceCode + xueduan + xueke + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("xueduan", xueduan)
                .addBody("xueke", xueke)
                .addBody("jgcode", deviceCode)
        );
    }

    /**
     * 获取同步书本列表数据
     *
     * @param banben 版本
     * @param xueqi  学期
     * @param order  排序:1按年份降序,2按id降序
     */
    public void getSynchroBookListRequest(String xueduan, String xueke, String nianji, String banben, String xueqi, String order, String deviceCode, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getSynchroBookList,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getSynchroBookList, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getSynchroBookList, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(banben + deviceCode + nianji + order + xueduan + xueqi + xueke + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("banben", banben)
                .addBody("jgcode", deviceCode)
                .addBody("nianji", nianji)
                .addBody("order", order)
                .addBody("xueduan", xueduan)
                .addBody("xueqi", xueqi)
                .addBody("xueke", xueke)
        );
    }

    /**
     * 获取同步书本的所有播放视频列表
     *
     * @param muluID 书本ID
     * @param order  排序,1id降序,默认id升序
     * @param num    每页显示条数,默认10条
     * @param page   当前页数,默认第1页
     */
    public void getSynchroVideoListRequest(String muluID, String order, String num, String page, String deviceCode, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getSynchroVideoList,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getSynchroVideoList, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getSynchroVideoList, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(deviceCode + muluID + num + order + page + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("muluID", muluID)
                .addBody("num", num)
                .addBody("order", order)
                .addBody("page", page)
                .addBody("jgcode", deviceCode)
        );
    }

    /**
     * 获取知识点视频播放列表
     *
     * @param name  知识点名称关键词,如:字母归类
     * @param order 排序,1id降序,默认id升序
     * @param num   每页显示条数,默认10条
     * @param page  当前页,默认第1页
     */
    public void getKnowledgeVideoLisRequest(String deviceCode, String name, String xueduan, String xueke, String nianji, String order, String num, String page, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getKnowledgeVideoLis,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getKnowledgeVideoLis, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getKnowledgeVideoLis, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(deviceCode + name + nianji + num + order + page + xueduan + xueke + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("name", name)
                .addBody("nianji", nianji)
                .addBody("num", num)
                .addBody("order", order)
                .addBody("page", page)
                .addBody("xueduan", xueduan)
                .addBody("xueke", xueke)
                .addBody("jgcode", deviceCode)
        );
    }

    /**
     * 获取作文、奥数专题视频播放列表
     *
     * @param name  专题名称关键词,如奥数
     * @param order 排序,1id降序,默认id升序
     */
    public void getSpecialVideoListRequest(String deviceCode, String name, String xueduan, String xueke, String nianji, String xueqi, String order, String num, String page, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getSpecialVideoList,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getSpecialVideoList, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getSpecialVideoList, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(deviceCode + name + nianji + num + order + page + xueduan + xueke + xueqi + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("name", name)
                .addBody("nianji", nianji)
                .addBody("num", num)
                .addBody("order", order)
                .addBody("page", page)
                .addBody("xueduan", xueduan)
                .addBody("xueke", xueke)
                .addBody("xueqi", xueqi)
                .addBody("jgcode", deviceCode)
        );
    }

    /**
     * 获取视频播放信息
     *
     * @param vkName  视频播放列表信息里返回的数据，与id两个参数必传一个
     * @param videoId 视频id(vkname跟id两个参数必传一个)
     */
    public void getVideoUrlRequest(String deviceCode, String vkName, String videoId, ServiceResultCallback callback) {
        String time = String.valueOf(getTimeMillis());
        NetWorkManager.Companion.getInstant().executeRequest(new NetRequestObj(
                RequestAPI.getVideoUrl,
                new NetWorkManager.RequestCallback() {
                    @Override
                    public void onError(@NotNull IOException e) {
                        Log.e(TAG, "获取学科列表 接口请求错误！");
                        callback.requestError(RequestAPI.getVideoUrl, null);
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(@NotNull Response response) {
                        Log.e(TAG, "获取学科列表 接口请求成功！");
                        decodingBody(RequestAPI.getVideoUrl, response, callback);
                    }
                }).addBody("time", time)
                .addBody("sign", createSignature(deviceCode + vkName + videoId + time))
                .addBody("Account", RequestAPI.APP_Account)
                .addBody("jgcode", deviceCode)
                .addBody("vkName", vkName)
                .addBody("videoId", videoId)
        );
    }
    //endregion

    //region ---------------------- 接口处理工具

    /**
     * 对后台返回的消息进行处理，如果消息为NULL，将回调 requestError()
     *
     * @param requestApi 请求数据的接口地址
     * @param response   接口返回的数据
     */
    private void decodingBody(String requestApi, Response response, ServiceResultCallback callback) {
        String resultMessage;
        switch (response.code()) {
            case RequestAPI.messageStateCode_8:
                callback.requestError(requestApi, "没有查询到您要的信息");
                return;
            case RequestAPI.messageStateCode_1000:
                callback.requestError(requestApi, "禁止非法访问");
                return;
            case RequestAPI.messageStateCode_1001:
                callback.requestError(requestApi, "接口请求超时,请重新请求");
                return;
            case RequestAPI.messageStateCode_1002:
                callback.requestError(requestApi, "签名匹配失败,请校正");
                return;
            case RequestAPI.messageStateCode_1003:
                callback.requestError(requestApi, "必填参数不能为空");
                return;
            default:
                break;
        }
        if (response.body() == null) {
            resultMessage = "";
        } else {
            try {
                resultMessage = Okio.buffer(response.body().source()).readUtf8();
            } catch (Exception e) {
                e.printStackTrace();
                resultMessage = "";
            }
        }
        if (TextUtils.isEmpty(resultMessage)) {
            callback.requestError(requestApi, "服务器返回信息为空！");
        } else {
            Log.e(TAG, resultMessage);
            callback.requestCallback(requestApi, resultMessage);
        }
    }

    /**
     * md5加密生成签名参数
     *
     * @param items 由指定的参数拼接的字符串
     * @return 进行了md5加密的字符串
     */
    private String createSignature(String items) {
        return md5Signature(items + RequestAPI.APP_Key);
    }

    /**
     * 对参数进行md5加密
     */
    @NotNull
    private String md5Signature(String txt) {
        byte[] secretBytes;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(txt.getBytes());
        } catch (NoSuchAlgorithmException e) {
            Log.e("-YunStudyVideoManager-", "没有MD5加密算法");
            return "";
        }
        StringBuilder md5Code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        //位数不够前面补零
        for (int i = 0; i < 32 - md5Code.length(); i++)
            md5Code.insert(0, "0");
        return md5Code.toString();
    }

    /**
     * 返回当前时间
     *
     * @return 单位:秒
     */
    private long getTimeMillis() {
        return Math.round(System.currentTimeMillis() * 1.0 / 1000);
    }
    //endregion
}
