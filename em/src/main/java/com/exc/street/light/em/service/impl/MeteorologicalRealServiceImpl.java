/**
 * @filename:MeteorologicalRealServiceImpl 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2018 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.em.service.impl;

import com.exc.street.light.em.service.MeteorologicalDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.em.MeteorologicalReal;
import com.exc.street.light.em.mapper.MeteorologicalRealDao;
import com.exc.street.light.em.service.MeteorologicalRealService;
import com.exc.street.light.resource.utils.SocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Service
public class MeteorologicalRealServiceImpl  extends ServiceImpl<MeteorologicalRealDao, MeteorologicalReal> implements MeteorologicalRealService  {
    private static final Logger logger = LoggerFactory.getLogger(MeteorologicalRealServiceImpl.class);

    @Autowired
    private MeteorologicalDeviceService meteorologicalDeviceService;

    private Result result = new Result();


}