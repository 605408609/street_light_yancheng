/**
 * @filename:ScreenPlayDao 2020-04-26
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.ir.ScreenPlay;
import com.exc.street.light.resource.qo.IrScreenPlayQuery;
import com.exc.street.light.resource.vo.resp.IrRespScreenPlayVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Repository
@Mapper
public interface ScreenPlayMapper extends BaseMapper<ScreenPlay> {

    /**
     * 分页查询播放中的节目列表
     *
     * @param page
     * @param irScreenPlayQuery
     * @return
     */
    IPage<IrRespScreenPlayVO> query(IPage<IrScreenPlayQuery> page, IrScreenPlayQuery irScreenPlayQuery);
}
