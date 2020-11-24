/**
 * @filename:RadioProgramDao 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.pb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.pb.RadioProgram;
import com.exc.street.light.resource.qo.PbRadioProgramQueryObject;
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
public interface RadioProgramDao extends BaseMapper<RadioProgram> {

    /**
     * 广播节目列表
     * @param page
     * @param queryObject 查询条件和权限过滤
     * @return
     */
    IPage<RadioProgram> getPageList(Page<RadioProgram> page, @Param("queryObject") PbRadioProgramQueryObject queryObject);
}
