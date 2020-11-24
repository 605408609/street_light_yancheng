package com.exc.street.light.electricity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.electricity.CanChannelType;

import java.util.List;


/**
 * @author Linshiwen
 * @date 2018/05/23
 */
public interface CanChannelTypeService extends IService<CanChannelType> {
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
