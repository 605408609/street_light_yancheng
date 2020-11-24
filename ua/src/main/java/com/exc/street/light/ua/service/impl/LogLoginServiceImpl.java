/**
 * @filename:LogLoginServiceImpl 2020-06-09
 * @project ua  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.log.LogLogin;
import com.exc.street.light.resource.qo.LogLoginQueryObject;
import com.exc.street.light.resource.vo.resp.LogRespLoginVO;
import com.exc.street.light.ua.mapper.LogLoginDao;
import com.exc.street.light.ua.service.LogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**   
 * @Description:登录日志(服务实现)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Service
public class LogLoginServiceImpl  extends ServiceImpl<LogLoginDao, LogLogin> implements LogLoginService  {

    @Autowired
    private LogLoginDao logLoginDao;

    @Override
    public Result getPages(HttpServletRequest request, LogLoginQueryObject queryObject) {
        Result<Object> result = new Result<>();
        Page<LogRespLoginVO> page = new Page<>(queryObject.getPageNum(), queryObject.getPageSize());
        IPage<LogRespLoginVO> list = logLoginDao.getPageList(page, queryObject);
        return result.success(list);
    }
}