/**
 * @filename:AhDeviceServiceImpl 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2018 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.AhDeviceDao;
import com.exc.street.light.dlm.service.AhDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.occ.AhDevice;
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
 * @author: Huang Min
 * 
 */
@Service
public class AhDeviceServiceImpl extends ServiceImpl<AhDeviceDao, AhDevice> implements AhDeviceService {

    private static final Logger logger = LoggerFactory.getLogger(AhDeviceServiceImpl.class);

    @Autowired
    AhDeviceDao ahDeviceDao;

    @Override
    public Result addList(List<AhDevice> ahDeviceList) {
        logger.info("批量导入AhDevice，接收参数:{}", ahDeviceList);
        for (AhDevice ahDevice : ahDeviceList) {
            ahDeviceDao.insert(ahDevice);
        }
        return new Result().success("批量导入AhDevice成功");
    }

    @Override
    public Result selectAll() {
        logger.info("查询所有AhDevice");
        List<AhDevice> ahDeviceList = ahDeviceDao.selectList(null);
        return new Result().success("查询成功",ahDeviceList);
    }

    @Override
    public List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList) {
        return this.baseMapper.getDlmRespDeviceVOList(slLampPostIdList);
    }
}