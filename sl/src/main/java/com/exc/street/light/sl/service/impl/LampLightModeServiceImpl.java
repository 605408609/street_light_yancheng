/**
 * @filename:LampLightModeServiceImpl 2020-08-27
 * @project sl  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampLightMode;
import com.exc.street.light.sl.mapper.LampLightModeMapper;
import com.exc.street.light.sl.service.LampLightModeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
public class LampLightModeServiceImpl extends ServiceImpl<LampLightModeMapper, LampLightMode> implements LampLightModeService {

    private static final Logger logger = LoggerFactory.getLogger(LampLightModeServiceImpl.class);

    @Override
    public Result listLightModeWithOptionQuery(HttpServletRequest request) {
        logger.info("listLightModeWithOptionQuery - 亮灯方式下拉列表");
        List<LampLightMode> lightModeList = this.list();
        Result<List<LampLightMode>> result = new Result<>();
        return result.success(lightModeList);
    }
}