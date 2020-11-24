/**
 * @filename:ControlLoopSceneStatusDao 2020-11-05
 * @project dlm  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.dto.dlm.ControlLoopSceneStatusDTO;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.dlm.ControlLoopSceneStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(集控回路场景状态表数据访问层)
 *
 * @version: V1.0
 * @author: Xiaok
 * 
 */
@Mapper
public interface ControlLoopSceneStatusMapper extends BaseMapper<ControlLoopSceneStatus> {

    /**
     * 获取未下发成功的回路
     * @param mac
     * @return
     */
    List<ControlLoopSceneStatusDTO> findSceneStatusList(@Param("mac")String mac);

}
