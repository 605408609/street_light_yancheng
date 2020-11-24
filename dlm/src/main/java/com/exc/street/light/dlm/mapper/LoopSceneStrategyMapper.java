/**
 * @filename:LoopSceneStrategyDao 2020-11-07
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.qo.DlmSceneStrategyQuery;
import com.exc.street.light.resource.vo.resp.DlmRespSceneStrategyVO;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.dlm.LoopSceneStrategy;
import org.apache.ibatis.annotations.Param;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Mapper
public interface LoopSceneStrategyMapper extends BaseMapper<LoopSceneStrategy> {

    /**
     * 场景策略分页条件查询
     * @param page
     * @param dlmSceneStrategyQuery
     * @return
     */
    IPage<DlmRespSceneStrategyVO> selectSceneStrategyWithPage(Page<DlmRespSceneStrategyVO> page, @Param("dlmSceneStrategyQuery") DlmSceneStrategyQuery dlmSceneStrategyQuery);
}
