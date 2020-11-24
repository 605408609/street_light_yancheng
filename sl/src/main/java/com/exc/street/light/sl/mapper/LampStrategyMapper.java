/**
 * @filename:LampStrategyDao 2020-08-26
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.sl.LampStrategy;
import com.exc.street.light.resource.qo.SlLampStrategyQuery;
import com.exc.street.light.resource.vo.resp.SlRespStrategyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Mapper
public interface LampStrategyMapper extends BaseMapper<LampStrategy> {

    /**
     * 灯控策略分页条件查询
     * @param page
     * @param slLampStrategyQuery
     * @return
     */
    IPage<SlRespStrategyVO> selectLampStrategyWithPage(Page<SlRespStrategyVO> page, @Param("slLampStrategyQuery") SlLampStrategyQuery slLampStrategyQuery);
}
