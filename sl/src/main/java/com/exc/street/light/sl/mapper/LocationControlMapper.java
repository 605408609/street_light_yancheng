/**
 * @filename:LocationControlDao 2020-09-15
 * @project sl  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import org.apache.ibatis.annotations.Mapper;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Mapper
public interface LocationControlMapper extends BaseMapper<LocationControl> {
	
}
