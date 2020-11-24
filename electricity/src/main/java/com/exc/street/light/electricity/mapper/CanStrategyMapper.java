/**
 * @filename:CanStrategyDao 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.electricity.CanStrategy;
import com.exc.street.light.resource.qo.electricity.CanStrategyQueryObject;
import com.exc.street.light.resource.vo.electricity.RespCanStrategyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:TODO(路灯网关策略表数据访问层)
 *
 * @version: V1.0
 * @author: Xiaok
 *
 */
@Mapper
public interface CanStrategyMapper extends BaseMapper<CanStrategy> {

    /**
     * 获取分页列表
     * @param page
     * @param qo
     * @return
     */
    IPage<RespCanStrategyVO> getPageList(IPage<RespCanStrategyVO> page, @Param("qo") CanStrategyQueryObject qo);

}
