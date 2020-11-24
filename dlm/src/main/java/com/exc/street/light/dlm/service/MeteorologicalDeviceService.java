/**
 * @filename:MeteorologicalDeviceService 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.qo.MeteorologicalDeviceQueryObject;
import com.exc.street.light.resource.vo.req.EmReqStatisParamVO;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: LeiJing
 * 
 */
public interface MeteorologicalDeviceService extends IService<MeteorologicalDevice> {

    /**
     * 批量插入
     * @param meteorologicalDeviceList
     * @return
     */
    Result addList(List<MeteorologicalDevice> meteorologicalDeviceList);

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