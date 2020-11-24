/**
 * @filename:MeteorologicalDeviceServiceImpl 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2018 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.MeteorologicalDeviceDao;
import com.exc.street.light.dlm.service.MeteorologicalDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
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
 * @author: LeiJing
 * 
 */
@Service
public class MeteorologicalDeviceServiceImpl extends ServiceImpl<MeteorologicalDeviceDao, MeteorologicalDevice> implements MeteorologicalDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(MeteorologicalDeviceServiceImpl.class);

    @Autowired
    private MeteorologicalDeviceDao meteorologicalDeviceDao;

    @Override
    public Result addList(List<MeteorologicalDevice> meteorologicalDeviceList) {
        logger.info("批量导入MeteorologicalDevice，接收参数:{}", meteorologicalDeviceList);
        for (MeteorologicalDevice meteorologicalDevice : meteorologicalDeviceList) {
            meteorologicalDeviceDao.insert(meteorologicalDevice);
        }
        return new Result().success("批量导入MeteorologicalDevice成功");
    }

    @Override
    public Result selectAll() {
        logger.info("查询所有MeteorologicalDevice");
        List<MeteorologicalDevice> meteorologicalDeviceList = meteorologicalDeviceDao.selectList(null);
        return new Result().success("查询成功",meteorologicalDeviceList);
    }

    @Override
    public List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList) {
        return this.baseMapper.getDlmRespDeviceVOList(slLampPostIdList);
    }
}