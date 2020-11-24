/**
 * @filename:WifiApDeviceService 2020-03-16
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;

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
     * 批量插入
     * @param wifiApDeviceList
     * @return
     */
    Result addList(List<WifiApDevice> wifiApDeviceList);

    /**
     * 查询所有
     */
    Result selectAll();

    /**
     * 根据灯杆id获取灯具返回对象集合
     * @param slLampPostIdList
     * @return
     */
    List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList);
}