/**
 * @filename:WifiApDeviceServiceImpl 2020-03-16
 * @project wifi  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.WifiApDeviceDao;
import com.exc.street.light.dlm.service.WifiApDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;
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
public class WifiApDeviceServiceImpl extends ServiceImpl<WifiApDeviceDao, WifiApDevice> implements WifiApDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(WifiApDeviceServiceImpl.class);

    @Autowired
    WifiApDeviceDao wifiApDeviceDao;

    @Override
    public Result addList(List<WifiApDevice> wifiApDeviceList) {
        logger.info("批量导入WifiApDevice，接收参数:{}", wifiApDeviceList);
        for (WifiApDevice wifiApDevice : wifiApDeviceList) {
            wifiApDeviceDao.insert(wifiApDevice);
        }
        return new Result().success("批量导入WifiApDevice成功");
    }

    @Override
    public Result selectAll() {
        logger.info("查询所有WifiApDevice");
        List<WifiApDevice> wifiApDeviceList = wifiApDeviceDao.selectList(null);
        return new Result().success("查询成功",wifiApDeviceList);
    }

    @Override
    public List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList) {
        return this.baseMapper.getDlmRespDeviceVOList(slLampPostIdList);
    }
}