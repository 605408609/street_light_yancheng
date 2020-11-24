/**
 * @filename:LampDeviceServiceImpl 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.LampDeviceDao;
import com.exc.street.light.dlm.service.LampDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Service
public class LampDeviceServiceImpl extends ServiceImpl<LampDeviceDao, LampDevice> implements LampDeviceService {

    private static final Logger logger = LoggerFactory.getLogger(LampDeviceServiceImpl.class);

    @Autowired
    LampDeviceDao lampDeviceDao;

    @Override
    public Result addList(List<LampDevice> lampDeviceList) {
        logger.info("批量导入LampDevice，接收参数:{}", lampDeviceList);
        for (LampDevice lampDevice : lampDeviceList) {
            lampDeviceDao.insert(lampDevice);
        }
        return new Result().success("批量导入LampDevice成功");
    }

    @Override
    public Result selectAll() {
        logger.info("查询所有LampDevice");
        List<LampDevice> lampDeviceList = lampDeviceDao.selectList(null);
        return new Result().success("查询成功",lampDeviceList);
    }
}