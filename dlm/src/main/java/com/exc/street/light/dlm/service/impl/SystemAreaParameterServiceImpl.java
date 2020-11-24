/**
 * @filename:SystemAreaParameterServiceImpl 2020-08-31
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SystemAreaParameter;
import com.exc.street.light.dlm.mapper.SystemAreaParameterMapper;
import com.exc.street.light.dlm.service.SystemAreaParameterService;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 区域参数信息(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
public class SystemAreaParameterServiceImpl extends ServiceImpl<SystemAreaParameterMapper, SystemAreaParameter> implements SystemAreaParameterService {

    private static final Logger logger = LoggerFactory.getLogger(SystemAreaParameterServiceImpl.class);

    @Autowired
    private LogUserService logUserService;

    @Override
    public Result detailOfAreaParameter(HttpServletRequest request) {
        logger.info("detailOfAreaParameter 区域参数信息");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        boolean flag = logUserService.isAdmin(userId);
        User user = logUserService.get(userId);
        Result<List<SystemAreaParameter>> result = new Result<>();
        List<SystemAreaParameter> parameterList = null;
        LambdaQueryWrapper<SystemAreaParameter> wrapper = new LambdaQueryWrapper<>();
        if (!flag) {
            // 不是超级管理员，根据分区查询
            wrapper.eq(SystemAreaParameter::getAreaId, user.getAreaId());
            parameterList = baseMapper.selectParameterList(user.getAreaId());
            return result.success(parameterList);
        } else {
            parameterList = baseMapper.selectParameterList(null);
        }
        return result.success(parameterList);
    }
}