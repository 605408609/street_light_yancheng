package com.exc.street.light.dlm.service;

/**
 * 心跳包解析服务
 *
 * @author Linshiwen
 * @date 2018/7/31
 */
public interface HeartbeatAnalysisService {
    /**
     * 解析心跳包
     * @param data
     * @param clientIP
     */
    void analyze(byte[] data, String clientIP);
}
