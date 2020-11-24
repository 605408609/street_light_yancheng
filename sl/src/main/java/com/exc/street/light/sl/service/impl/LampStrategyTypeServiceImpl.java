/**
 * @filename:LampStrategyTypeServiceImpl 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampStrategyType;
import com.exc.street.light.sl.mapper.LampStrategyTypeDao;
import com.exc.street.light.sl.service.LampStrategyTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
@Service
public class LampStrategyTypeServiceImpl extends ServiceImpl<LampStrategyTypeDao, LampStrategyType> implements LampStrategyTypeService {
    private static final Logger logger = LoggerFactory.getLogger(LampStrategyTypeServiceImpl.class);

    @Override
    public Result pulldown(HttpServletRequest request) {
        logger.info("查询策略下拉列表");
        List<LampStrategyType> list = this.list();
        Result result = new Result();
        return result.success(list);
    }
}