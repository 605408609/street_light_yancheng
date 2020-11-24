/**
 * @filename:LogNormalServiceImpl 2020-05-08
 * @project log  V1.0
 * Copyright(c) 2018 xuJiaHao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.log_api.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.log_api.service.LogNormalService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.log.LogNormal;
import com.exc.street.light.log_api.mapper.LogNormalMapper;
import com.exc.street.light.resource.qo.LogDataQueryObject;
import com.exc.street.light.resource.vo.resp.LogRespDataVO;
import javassist.LoaderClassPath;
import lombok.extern.slf4j.Slf4j;
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
public class LogNormalServiceImpl  extends ServiceImpl<LogNormalMapper, LogNormal> implements LogNormalService {

    @Override
    public Result getPage(LogDataQueryObject logDataQueryObject, HttpServletRequest request) {
        IPage iPage = new Page<LogRespDataVO>(logDataQueryObject.getPageNum(),logDataQueryObject.getPageSize());
        List<LogRespDataVO> logRespDataVOList = this.baseMapper.findPage(iPage,logDataQueryObject);
        iPage.setRecords(logRespDataVOList);
        Result result = new Result();
        return result.success(iPage);
    }
}