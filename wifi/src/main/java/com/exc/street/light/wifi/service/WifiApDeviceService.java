/**
 * @filename:WifiApDeviceService 2020-03-16
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.wifi.qo.ApDeviceQueryObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: AP设备(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface WifiApDeviceService extends IService<WifiApDevice> {

    /**
     * 新增ap设备
     * @param wifiApDevice
     * @param request
     * @return
     */
    Result add(WifiApDevice wifiApDevice, HttpServletRequest request);

    /**
     * 编辑ap设备
     * @param wifiApDevice
     * @param request
     * @return
     */
    Result modify(WifiApDevice wifiApDevice, HttpServletRequest request);

    /**
     * 删除ap设备
     * @param id
     * @param request
     * @return
     */
    Result delete(Integer id, HttpServletRequest request);

    /**
     * AP设备详情
     * @param id
     * @return
     */
    Result get(Integer id, HttpServletRequest request);

    /**
     * AP设备列表
     * @param request
     * @param queryObject
     * @return
     */
    Result getList(HttpServletRequest request, ApDeviceQueryObject queryObject);

    /**
     * 批量删除
     * @param request
     * @param ids
     * @return
     */
    Result batchDelete(HttpServletRequest request, String ids);

    /**
     * 根据灯杆id集合查询所AP
     * @param lampPostIdList
     * @param request
     * @return
     */
    Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request);

    /**
     * 设备名称和编号唯一性校验, 0:不存在 1：已存在
     * @param id
     * @param name
     * @return
     */
    Result nameUniqueness(Integer id, String name, String num);

    /**
     * 获取ap设备状态信息并同步到数据库
     * @return
     */
    Result getStatusApDevice();

    /**
     * app端ap设备简单信息
     * @param id
     * @return
     */
    Result getByApp(Integer id);


    /**
     * AP设备编号唯一性校验, 0:不存在 1：已存在
     * @param id
     * @param num
     * @return
     */
//    Result numUniqueness(Integer id, String num);
}