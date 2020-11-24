/**
 * @filename:LogNormalDao 2020-05-08
 * @project log  V1.0
 * Copyright(c) 2020 xuJiaHao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.log_api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.qo.LogDataQueryObject;
import com.exc.street.light.resource.vo.resp.LogRespDataVO;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.log.LogNormal;
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
public interface LogNormalMapper extends BaseMapper<LogNormal> {
	/**
	 * 分页查询数据
	 *
	 * @param iPage
	 * @param logDataQueryObject
	 * @return
	 */
	List<LogRespDataVO> findPage(IPage<LogRespDataVO> iPage, @Param("logDataQueryObject")LogDataQueryObject logDataQueryObject);

	List<LogNormal> selectOn();
}
