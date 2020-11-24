/**
 * @filename:SystemAreaParameterDao 2020-08-31
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.dlm.SystemAreaParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface SystemAreaParameterMapper extends BaseMapper<SystemAreaParameter> {

    /**
     * 根据区域id查询区域参数信息
     * @param areaId
     * @return
     */
    List<SystemAreaParameter> selectParameterList(@Param("areaId") Integer areaId);
}
