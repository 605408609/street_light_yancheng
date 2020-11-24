package com.exc.street.light.electricity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.electricity.ComChannel;
import com.exc.street.light.resource.entity.electricity.ComDevice;

import java.util.List;


/**
 * 串口设备服务接口
 *
 * @author Linshiwen
 * @date 2018/07/04
 */
public interface ComDeviceService extends IService<ComDevice> {

    /**
     * 导入设备
     *
     * @param comDevices
     * @param comChannels
     * @param nid
     */
    void importDevice(List<ComDevice> comDevices, List<ComChannel> comChannels, Integer nid);

    /**
     * 根据节点id删除
     *
     * @param nid
     */
    void deleteByNid(Integer nid);
}
