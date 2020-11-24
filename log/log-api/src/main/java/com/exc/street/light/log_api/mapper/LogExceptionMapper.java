/**
 * @filename:LogExceptionDao 2020-05-08
 * @project log  V1.0
 * Copyright(c) 2020 xuJiaHao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.log_api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.qo.LogDataQueryObject;
import com.exc.street.light.resource.qo.LogExceDataQueryObject;
import com.exc.street.light.resource.vo.resp.LogRespDataVO;
import com.exc.street.light.resource.vo.resp.LogRespExceDataVO;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.log.LogException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xuJiaHao
 * 
 */
@Mapper
public interface LogExceptionMapper extends BaseMapper<LogException> {

    List<LogRespExceDataVO> findPage(IPage<LogRespExceDataVO> iPage, @Param("logExceDataQueryObject") LogExceDataQueryObject logExceDataQueryObject);
}
