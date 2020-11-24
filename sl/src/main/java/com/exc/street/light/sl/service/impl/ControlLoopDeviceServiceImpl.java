/**
 * @filename:ControlLoopDeviceServiceImpl 2020-09-14
 * @project sl  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service.impl;

import com.exc.street.light.resource.entity.dlm.ControlLoop;
import com.exc.street.light.resource.entity.dlm.ControlLoopDevice;
import com.exc.street.light.resource.vo.req.SlReqInstallLampZkzlVO;
import com.exc.street.light.sl.mapper.ControlLoopDeviceMapper;
import com.exc.street.light.sl.service.ControlLoopDeviceService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**   
 * @Description:TODO(灯具、回路（分组）、集中控制器关联表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Service
public class ControlLoopDeviceServiceImpl  extends ServiceImpl<ControlLoopDeviceMapper, ControlLoopDevice> implements ControlLoopDeviceService  {

    @Override
    public List<SlReqInstallLampZkzlVO> getByDeviceIdList(List<Integer> deviceIdList) {
        return baseMapper.getByDeviceId(deviceIdList);
    }

    @Override
    public ControlLoop selectControlLoopByNum(Integer concentratorId, String num) {
        return baseMapper.selectControlLoopByNum(concentratorId,num);
    }

    @Override
    public List<Integer> getAllLampNoList(String concentratorId) {
        return baseMapper.getAllLampNoList(concentratorId);
    }
}