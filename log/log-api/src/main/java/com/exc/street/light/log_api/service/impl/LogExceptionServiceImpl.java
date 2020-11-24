/**
 * @filename:LogExceptionServiceImpl 2020-05-08
 * @project log  V1.0
 * Copyright(c) 2018 xuJiaHao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.log_api.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.log_api.service.LogExceptionService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.log.LogException;
import com.exc.street.light.log_api.mapper.LogExceptionMapper;
import com.exc.street.light.resource.qo.LogExceDataQueryObject;
import com.exc.street.light.resource.vo.resp.LogRespDataVO;
import com.exc.street.light.resource.vo.resp.LogRespExceDataVO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: xuJiaHao
 * 
 */
@Service
public class LogExceptionServiceImpl  extends ServiceImpl<LogExceptionMapper, LogException> implements LogExceptionService {


    @Override
    public Result getPage(LogExceDataQueryObject logExceDataQueryObject, HttpServletRequest request) {
        IPage iPage = new Page<LogRespExceDataVO>(logExceDataQueryObject.getPageNum(),logExceDataQueryObject.getPageSize());
        List<LogRespExceDataVO> logRespExceDataVOList = this.baseMapper.findPage(iPage,logExceDataQueryObject);
        iPage.setRecords(logRespExceDataVOList);
        Result result = new Result();
        return result.success(iPage);
    }
}