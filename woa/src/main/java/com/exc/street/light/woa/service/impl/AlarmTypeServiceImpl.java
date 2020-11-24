/**
 * @filename:AlarmTypeServiceImpl 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.woa.service.impl;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.woa.AlarmType;
import com.exc.street.light.woa.mapper.AlarmTypeMapper;
import com.exc.street.light.woa.service.AlarmTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
public class AlarmTypeServiceImpl  extends ServiceImpl<AlarmTypeMapper, AlarmType> implements AlarmTypeService  {
    private static final Logger logger = LoggerFactory.getLogger(AlarmTypeServiceImpl.class);

    @Override
    public Result pulldown(HttpServletRequest httpServletRequest) {
        logger.info("告警类型下拉列表");
        List<AlarmType> list = this.list();
        Result result = new Result();
        return result.success(list);
    }
}