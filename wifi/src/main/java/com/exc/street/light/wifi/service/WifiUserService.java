/**
 * @filename:WifiUserService 2020-03-12
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.wifi.WifiUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.wifi.qo.WifiUserQueryObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: wifi用户(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface WifiUserService extends IService<WifiUser> {

    /**
     * wifi用户统计列表
     * @param request
     * @param queryObject
     * @return
     */
    Result getList(HttpServletRequest request, WifiUserQueryObject queryObject);

    /**
     * 获取wifi用户信息并同步到数据库
     * @return
     */
    Result getWifiUserInfo();

}