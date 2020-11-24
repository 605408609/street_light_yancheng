/**
 * @filename:WifiApService 2020-03-12
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.wifi.WifiAp;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.qo.WifiChartStatisticQueryObject;
import com.exc.street.light.wifi.qo.ApQueryObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: wifiAp(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface WifiApService extends IService<WifiAp> {

    /**
     * AP统计列表
     * @param request
     * @param queryObject
     * @return
     */
    Result getList(HttpServletRequest request, ApQueryObject queryObject);

    /**
     * wifi报表统计
     * @param request
     * @param queryObject
     * @return
     */
    Result chartStatistic(HttpServletRequest request, WifiChartStatisticQueryObject queryObject);

    /**
     * 获取ap信息同步到数据库
     * @return
     */
    Result getInfoApDevice();

    /**
     * 获取ap设备信息同步到历史数据库
     * @return
     */
    Result getHistoryInfoApDevice();


}