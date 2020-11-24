/**
 * @filename:MeteorologicalRealDao 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.em.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.em.MeteorologicalReal;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Repository
@Mapper
public interface MeteorologicalRealDao extends BaseMapper<MeteorologicalReal> {
	
}
