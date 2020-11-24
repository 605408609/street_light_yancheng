/**
 * @filename:LocationDistributeCabinetDao 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.dlm.LocationDistributeCabinet;
import com.exc.street.light.resource.qo.DlmDistributeCabinetQuery;
import com.exc.street.light.resource.vo.resp.DlmRespDistributeCabinetVO;
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
public interface LocationDistributeCabinetMapper extends BaseMapper<LocationDistributeCabinet> {

    /**
     * 配电柜详情
     * @param cabinetId
     * @return
     */
    DlmRespDistributeCabinetVO selectDistributeCabinetByCabinetId(@Param("cabinetId") Integer cabinetId);

    /**
     * 分页查询配电柜列表
     * @param page
     * @param cabinetQuery
     * @return
     */
    IPage<DlmRespDistributeCabinetVO> selectDistributeCabinetWithPageByCabinetQuery(Page<DlmRespDistributeCabinetVO> page, @Param("cabinetQuery") DlmDistributeCabinetQuery cabinetQuery);

    /**
     * 查询配电柜列表
     * @param cabinetQuery
     * @return
     */
    List<DlmRespDistributeCabinetVO> selectDistributeCabinetListByCabinetQuery(@Param("cabinetQuery") DlmDistributeCabinetQuery cabinetQuery);
}
