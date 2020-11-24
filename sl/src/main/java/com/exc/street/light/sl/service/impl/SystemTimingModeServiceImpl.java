/**
 * @filename:SystemTimingModeServiceImpl 2020-08-27
 * @project sl  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.SystemTimingMode;
import com.exc.street.light.sl.mapper.SystemTimingModeMapper;
import com.exc.street.light.sl.service.SystemTimingModeService;
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
 * @author: xiezhipeng
 *
 */
@Service
public class SystemTimingModeServiceImpl extends ServiceImpl<SystemTimingModeMapper, SystemTimingMode> implements SystemTimingModeService {

    private static final Logger logger = LoggerFactory.getLogger(SystemTimingModeServiceImpl.class);

    @Override
    public Result listTimingModeWithOptionQuery(HttpServletRequest request) {
        logger.info("listTimingModeWithOptionQuery - 定时方式下拉列表");
        List<SystemTimingMode> timingModeList = this.list();
        Result<List<SystemTimingMode>> result = new Result<>();
        return result.success(timingModeList);
    }
}