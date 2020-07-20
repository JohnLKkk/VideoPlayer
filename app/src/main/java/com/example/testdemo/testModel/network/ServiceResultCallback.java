package com.example.testdemo.testModel.network;

/**
 * Created by Void on 2020/3/17 16:09
 */
public interface ServiceResultCallback {
    /**
     * 接口调用结果回调
     *
     * @param requestApi 与结果对应的请求接口
     * @param result     服务器返回的结果
     */
    void requestCallback(String requestApi, String result);

    /**
     * 接口调用异常
     *
     * @param requestApi 与结果对应的请求接口
     */
    void requestError(String requestApi,String errorMessage);
}
