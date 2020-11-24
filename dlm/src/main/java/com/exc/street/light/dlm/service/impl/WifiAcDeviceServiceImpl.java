/**
 * @filename:WifiApDeviceServiceImpl 2020-03-16
 * @project wifi  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.WifiAcDeviceDao;
import com.exc.street.light.dlm.mapper.WifiApDeviceDao;
import com.exc.street.light.dlm.service.WifiAcDeviceService;
import com.exc.street.light.dlm.service.WifiApDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.wifi.WifiAcDevice;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Service
public class WifiAcDeviceServiceImpl extends ServiceImpl<WifiAcDeviceDao, WifiAcDevice> implements WifiAcDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(WifiAcDeviceServiceImpl.class);

    @Autowired
    WifiAcDeviceDao wifiAcDeviceDao;

    @Override
    public Result addList(List<WifiAcDevice> wifiAcDeviceList) {
        logger.info("批量导入WifiAcDevice，接收参数:{}", wifiAcDeviceList);
        for (WifiAcDevice wifiAcDevice : wifiAcDeviceList) {
            wifiAcDeviceDao.insert(wifiAcDevice);
        }
        return new Result().success("批量导入WifiAcDevice成功");
    }

    @Override
    public Result selectAll() {
        logger.info("查询所有WifiAcDevice");
        List<WifiAcDevice> wifiAcDeviceList = wifiAcDeviceDao.selectList(null);
        return new Result().success("查询成功",wifiAcDeviceList);
    }
}