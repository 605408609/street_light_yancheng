package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.electricity.CanChannelType;

import java.util.List;

/**
 * 回路类型Mapper
 *
 * @author Linshiwen
 * @date 2018/5/28
 */
public interface CanChannelTypeMapper extends BaseMapper<CanChannelType> {
    /**
     * 获取继电器类型的回路
     *
     * @return
     */
    List<CanChannelType> getRelayType();

    /**
     * 获取除网络回路类型外的所有继电器回路类型
     *
     * @return
     */
    List<CanChannelType> find();
}