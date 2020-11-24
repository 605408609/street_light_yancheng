/**
 * @filename:ControlEnergyYearServiceImpl 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.exc.street.light.resource.entity.dlm.ControlEnergyYear;
import com.exc.street.light.dlm.mapper.ControlEnergyYearMapper;
import com.exc.street.light.dlm.service.ControlEnergyYearService;
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
public class ControlEnergyYearServiceImpl extends ServiceImpl<ControlEnergyYearMapper, ControlEnergyYear> implements ControlEnergyYearService {

    private static final Logger logger = LoggerFactory.getLogger(ControlEnergyYearServiceImpl.class);

    @Override
    public ControlEnergyYear selectOneByYearTime(Integer controlId, String formatYear) {
        logger.info("查询年度能耗: controlId:{} formatYear{}", controlId, formatYear);
        return baseMapper.selectOneByYearTime(controlId, formatYear);
    }
}