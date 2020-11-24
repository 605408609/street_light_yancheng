package com.exc.street.light.co.client.service;

/**
 * @Author tanhonghang
 * @create 2020/10/14 19:20
 */
public interface ResolveService {

    /**
     * mqtt服务重连之后所有设备重新订阅主题
     *
     * @return
     */
    boolean subScriptionAll();

    /**
     * 处理井盖回调数据
     *
     * @param jsonStr
     */
    void resolveCallBack(String jsonStr);
}
