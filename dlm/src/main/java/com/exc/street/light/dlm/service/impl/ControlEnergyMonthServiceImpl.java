/**
 * @filename:ControlEnergyMonthServiceImpl 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.exc.street.light.resource.entity.dlm.ControlEnergyMonth;
import com.exc.street.light.dlm.mapper.ControlEnergyMonthMapper;
import com.exc.street.light.dlm.service.ControlEnergyMonthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Service
public class ControlEnergyMonthServiceImpl extends ServiceImpl<ControlEnergyMonthMapper, ControlEnergyMonth> implements ControlEnergyMonthService {

    private static final Logger logger = LoggerFactory.getLogger(ControlEnergyMonthServiceImpl.class);

    @Override
    public ControlEnergyMonth selectOneByMonthTime(Integer controlId, String formatMonth) {
        logger.info("查询月度能耗: controlId:{} formatMonth{}", controlId, formatMonth);
        return baseMapper.selectOneByMonthTime(controlId, formatMonth);
    }
}