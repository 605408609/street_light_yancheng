/**
 * @filename:LampDeviceStrategyServiceImpl 2020-03-24
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDeviceStrategy;
import com.exc.street.light.sl.mapper.LampDeviceStrategyDao;
import com.exc.street.light.sl.service.LampDeviceStrategyService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class LampDeviceStrategyServiceImpl extends ServiceImpl<LampDeviceStrategyDao, LampDeviceStrategy> implements LampDeviceStrategyService {

    @Override
    public Result list(List<Integer> lampDeviceIdList, List<Integer> lampStrategyIdList, HttpServletRequest request) {
        LambdaQueryWrapper<LampDeviceStrategy> wrapper = new LambdaQueryWrapper();
        if (lampDeviceIdList != null && lampDeviceIdList.size() > 0 && lampStrategyIdList != null && lampStrategyIdList.size() > 0) {
            wrapper.in(LampDeviceStrategy::getDeviceId, lampDeviceIdList).in(LampDeviceStrategy::getStrategyId, lampStrategyIdList);
        } else if (lampDeviceIdList != null && lampDeviceIdList.size() > 0) {
            wrapper.in(LampDeviceStrategy::getDeviceId, lampDeviceIdList);
        } else if (lampStrategyIdList != null && lampStrategyIdList.size() > 0) {
            wrapper.in(LampDeviceStrategy::getStrategyId, lampStrategyIdList);
        } else {
            wrapper.in(LampDeviceStrategy::getStrategyId, new ArrayList<>());
        }
        List<LampDeviceStrategy> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result usedStrategyById(Integer deviceId) {
        Result result = new Result().error("查询失败");
        QueryWrapper<LampDeviceStrategy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        queryWrapper.eq("used",1);
        try {
            LampDeviceStrategy lampDeviceStrategy = this.getOne(queryWrapper);
            return result.success("查询成功",lampDeviceStrategy);
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }
    }
}