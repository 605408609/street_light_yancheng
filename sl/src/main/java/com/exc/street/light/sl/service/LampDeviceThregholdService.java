/**
 * @filename:LampDeviceThregholdService 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDeviceThreghold;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.req.SlReqThresholdAddListVO;
import com.exc.street.light.resource.vo.req.SlReqThresholdAddVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(设备阈值数据表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
public interface LampDeviceThregholdService extends IService<LampDeviceThreghold> {

    /**
     * 设备阈值详情
     * @param id
     * @param request
     * @return
     */
    Result detail(Integer id, HttpServletRequest request);

    /**
     * 新增设备阈值
     * @param slReqThresholdAddListVO
     * @param request
     * @return
     */
    Result add(SlReqThresholdAddListVO slReqThresholdAddListVO, HttpServletRequest request);

    /**
     * 删除设备阈值
     * @param request
     * @return
     */
    Result delete(HttpServletRequest request);

    /**
     * 修改设备阈值
     * @param slReqThresholdAddVOList
     * @param request
     * @return
     */
    Result updateThreghold(List<SlReqThresholdAddVO> slReqThresholdAddVOList, HttpServletRequest request);

    /**
     * 根据设备id集合删除设备阈值
     * @param deviceIdList
     * @param request
     * @return
     */
    Result deleteByDeviceIdList(List<Integer> deviceIdList, HttpServletRequest request);
}