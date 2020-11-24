package com.exc.street.light.electricity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.ComChannel;
import com.exc.street.light.resource.entity.electricity.ComDevice;

import java.util.List;


/**
 * 串口回路服务
 *
 * @author Linshiwen
 * @date 2018/07/04
 */
public interface ComChannelService extends IService<ComChannel> {
    /**
     * 保存回路
     *
     * @param comChannels
     * @param nid         节点id
     * @param comDevice
     * @return
     */
    Result saveList(List<ComChannel> comChannels, Integer nid, ComDevice comDevice);

}
