/**
 * @filename:RadioProgramMaterialDao 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.pb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.pb.RadioProgramMaterial;
import com.exc.street.light.resource.vo.PbRespMaterialSizeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Repository
@Mapper
public interface RadioProgramMaterialDao extends BaseMapper<RadioProgramMaterial> {

    List<PbRespMaterialSizeVO> selectMaterialSizeByProgramId(@Param("programId") Integer programId);
}
