/**
 * @filename:ControlLoopDeviceServiceImpl 2020-08-24
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.ControlLoopDeviceMapper;
import com.exc.street.light.dlm.service.ControlLoopDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ControlLoopDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 集控分组回路关联表(服务实现)
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Service
public class ControlLoopDeviceServiceImpl extends ServiceImpl<ControlLoopDeviceMapper, ControlLoopDevice> implements ControlLoopDeviceService {

    private static final Logger logger = LoggerFactory.getLogger(ControlLoopDeviceServiceImpl.class);

    @Override
    public Result getControlLoopDeviceByLoopIdList(List<Integer> loopIdList, HttpServletRequest request) {
        logger.info("根据分组id集合查询集控分组回路关联关系，接收参数:{}", loopIdList);
        LambdaQueryWrapper<ControlLoopDevice> wrapper = new LambdaQueryWrapper<>();
        if (loopIdList != null && loopIdList.size() > 0) {
            wrapper.in(ControlLoopDevice::getLoopId, loopIdList);
        }
        List<ControlLoopDevice> controlLoopDeviceList = baseMapper.selectList(wrapper);
        Result<List<ControlLoopDevice>> result = new Result<>();
        return result.success(controlLoopDeviceList);
    }
}