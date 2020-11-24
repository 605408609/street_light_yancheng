/**
 * @filename:ScreenProgramMaterialDao 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.ir.ScreenProgramMaterial;
import com.exc.street.light.resource.vo.IrProgramMaterialVO;
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
public interface ScreenProgramMaterialMapper extends BaseMapper<ScreenProgramMaterial> {

    /**
     * 获取节目素材中间对象集合
     *
     * @param programId
     * @return
     */
    List<IrProgramMaterialVO> getIrProgramMaterialVO(@Param("programId") Integer programId);
}
