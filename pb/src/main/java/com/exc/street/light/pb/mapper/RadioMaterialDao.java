/**
 * @filename:RadioMaterialDao 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.pb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.pb.RadioMaterial;
import com.exc.street.light.resource.qo.PbRadioMaterialQueryObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface RadioMaterialDao extends BaseMapper<RadioMaterial> {

    public String getNameByUserId(@Param("id")Integer userId);

    /**
     * 广播素材列表
     * @param page
     * @param queryObject 查询条件和权限过滤
     * @return
     */
    IPage<RadioMaterial> getPageList(Page<RadioMaterial> page, @Param("queryObject") PbRadioMaterialQueryObject queryObject);

}
