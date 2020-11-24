/**
 * @filename:ControlLoopDeviceService 2020-09-14
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.entity.dlm.ControlLoop;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.dlm.ControlLoopDevice;
import com.exc.street.light.resource.vo.req.SlReqInstallLampZkzlVO;

import java.util.List;

/**
 * @Description:TODO(灯具、回路（分组）、集中控制器关联表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
public interface ControlLoopDeviceService extends IService<ControlLoopDevice> {

    List<SlReqInstallLampZkzlVO> getByDeviceIdList(List<Integer> deviceIdList);

    /**
     * 根据集中控制器id及分组num查询集控分组
     * @param concentratorId
     * @param num
     * @return
     */
    ControlLoop selectControlLoopByNum(Integer concentratorId, String num);

    /**
     * 查找当前集中控制器下的所有序号
     * @param concentratorId
     * @return
     */
    List<Integer> getAllLampNoList(String concentratorId);
}