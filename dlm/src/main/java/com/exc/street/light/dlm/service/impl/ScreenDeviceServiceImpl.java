/**
 * @filename:ScreenDeviceServiceImpl 2020-03-17
 * @project ir  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.ScreenDeviceDao;
import com.exc.street.light.dlm.service.ScreenDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
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
 * @author: Longshuangyang
 * 
 */
@Service
public class ScreenDeviceServiceImpl extends ServiceImpl<ScreenDeviceDao, ScreenDevice> implements ScreenDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(ScreenDeviceServiceImpl.class);

    @Autowired
    ScreenDeviceDao screenDeviceDao;

    @Override
    public Result addList(List<ScreenDevice> screenDeviceList) {
        logger.info("批量导入ScreenDevice，接收参数:{}", screenDeviceList);
        for (ScreenDevice screenDevice : screenDeviceList) {
            screenDeviceDao.insert(screenDevice);
        }
        return new Result().success("批量导入ScreenDevice成功");
    }

    @Override
    public Result selectAll() {
        logger.info("查询所有ScreenDevice");
        List<ScreenDevice> screenDeviceList = screenDeviceDao.selectList(null);
        return new Result().success("查询成功",screenDeviceList);
    }

    @Override
    public List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList) {
        return this.baseMapper.getDlmRespDeviceVOList(slLampPostIdList);
    }
}