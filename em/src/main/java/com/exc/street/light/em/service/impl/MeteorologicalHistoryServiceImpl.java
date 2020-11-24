/**
 * @filename:MeteorologicalHistoryServiceImpl 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2018 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.em.service.impl;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.em.MeteorologicalHistory;
import com.exc.street.light.em.mapper.MeteorologicalHistoryDao;
import com.exc.street.light.em.service.MeteorologicalHistoryService;
import com.exc.street.light.resource.vo.req.EmReqStatisParamVO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Service
public class MeteorologicalHistoryServiceImpl  extends ServiceImpl<MeteorologicalHistoryDao, MeteorologicalHistory> implements MeteorologicalHistoryService  {

}