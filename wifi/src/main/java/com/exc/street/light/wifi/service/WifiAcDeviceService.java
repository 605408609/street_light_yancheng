/**
 * @filename:WifiAcDeviceService 2020-03-27
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.wifi.WifiAcDevice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.wifi.qo.AcDeviceQueryObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: AC设备(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface WifiAcDeviceService extends IService<WifiAcDevice> {

    /**
     * 新增ac设备
     * @param wifiAcDevice
     * @param request
     * @return
     */
    Result add(WifiAcDevice wifiAcDevice, HttpServletRequest request);

    /**
     * 编辑ac设备
     * @param wifiAcDevice
     * @param request
     * @return
     */
    Result modify(WifiAcDevice wifiAcDevice, HttpServletRequest request);

    /**
     * 刪除ac设备
     * @param id
     * @param request
     * @return
     */
    Result delete(Integer id, HttpServletRequest request);

    /**
     * AC设备详情
     * @param id
     * @param request
     * @return
     */
    Result get(Integer id, HttpServletRequest request);

    /**
     * AC设备列表
     * @param request
     * @param queryObject
     * @return
     */
    Result getList(HttpServletRequest request, AcDeviceQueryObject queryObject);

    /**
     * 批量删除
     * @param request
     * @param ids
     * @return
     */
    Result batchDelete(HttpServletRequest request, String ids);

    /**
     * 获取ac设备ip地址下拉框
     * @param request
     * @return
     */
    Result getAcIpList(HttpServletRequest request);

    /**
     * AC设备名称的唯一性校验, 0:不存在 1：已存在
     * @param id
     * @param name
     * @return
     */
    Result nameUniqueness(Integer id, String name, String num);

//    /**
//     * AC设备编号的唯一性校验, 0:不存在 1：已存在
//     * @param id
//     * @param num
//     * @return
//     */
//    Result numUniqueness(Integer id, String num);

    /**
     * 获取ac设备的状态并同步到数据库
     * @return
     */
    Result getStatusAcDevice();

}