/**
 * @filename:AhDeviceService 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;

import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Huang Min
 * 
 */
public interface RadioDeviceService extends IService<RadioDevice> {
    /**
     * 批量插入
     * @param radioDeviceList
     * @return
     */
    Result addList(List<RadioDevice> radioDeviceList);

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