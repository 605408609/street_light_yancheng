/**
 * @filename:GroupDao 2020-03-18
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.dlm.LocationGroup;
import com.exc.street.light.resource.qo.DlmGroupQuery;
import com.exc.street.light.resource.vo.resp.DlmRespGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Repository
@Mapper
public interface LocationGroupMapper extends BaseMapper<LocationGroup> {

    /**
     * 分组分页条件查询
     *
     * @param iPage
     * @param dlmGroupQuery
     * @return
     */
    List<DlmRespGroupVO> query(IPage<DlmRespGroupVO> iPage,@Param("dlmGroupQuery") DlmGroupQuery dlmGroupQuery);
}
