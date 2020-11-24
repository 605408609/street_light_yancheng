/**
 * @filename:CanChannelSceneStatusDao 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.dto.electricity.CanChannelSceneStatusDTO;
import com.exc.street.light.resource.entity.electricity.CanChannelSceneStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:TODO(路灯网关回路场景添加状态表数据访问层)
 *
 * @version: V1.0
 * @author: Xiaok
 *
 */
@Mapper
public interface CanChannelSceneStatusMapper extends BaseMapper<CanChannelSceneStatus> {

    /**
     * 获取未下发成功的回路
     * @param mac 网关mac地址
     * @return
     */
    List<CanChannelSceneStatusDTO> findSceneStatusList(@Param("mac") String mac);
}
