/**
 * @filename:ControlLoopDao 2020-08-24
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.dto.dlm.ControlLoopDTO;
import com.exc.street.light.resource.entity.dlm.ControlLoop;
import com.exc.street.light.resource.qo.DlmControlLoopQuery;
import com.exc.street.light.resource.vo.resp.DlmRespControlLoopVO;
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
public interface ControlLoopMapper extends BaseMapper<ControlLoop> {

    /**
     * 分页查询集控分组列表
     * @param page
     * @param loopQuery
     * @param typeId
     * @return
     */
    IPage<DlmRespControlLoopVO> selectControlLoopWithPageByLoopQuery(Page<DlmRespControlLoopVO> page, @Param("loopQuery") DlmControlLoopQuery loopQuery, @Param("typeId") Integer typeId);

    /**
     * 根据回路id集合查询回路和集控相关信息
     * @param loopIdList
     * @return
     */
    List<ControlLoopDTO> selectControlLoopByIdList(@Param("loopIdList") List<Integer> loopIdList);
}
