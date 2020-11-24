/**
 * @filename:AlarmDao 2020-09-24
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.woa.Alarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**   
 * @Description:TODO(告警表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Mapper
public interface AlarmMapper extends BaseMapper<Alarm> {

    /**
     * 获取指定灯杆及告警类型的最新一条数据
     * @param lampPostId
     * @param typeId
     * @return
     */
    Alarm getLastTime(@Param("lampPostId")Integer lampPostId, @Param("typeId")Integer typeId);
}
