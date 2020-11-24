package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.ComDeviceMapper;
import com.exc.street.light.electricity.service.ComChannelService;
import com.exc.street.light.electricity.service.ComDeviceService;
import com.exc.street.light.resource.entity.electricity.ComChannel;
import com.exc.street.light.resource.entity.electricity.ComDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Linshiwen
 * @date 2018/07/04
 */
@Service
public class ComDeviceServiceImpl extends ServiceImpl<ComDeviceMapper, ComDevice> implements ComDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(ComDeviceServiceImpl.class);
    @Autowired
    private ComChannelService comChannelService;


    @Override
    public void importDevice(List<ComDevice> comDevices, List<ComChannel> comChannels, Integer nid) {
        logger.info("判断电表设备是否已保存在数据库");
        for (ComDevice device : comDevices) {
            ComDevice comDevice = baseMapper.findByNidAndAddrAndCom(nid, device.getDeviceAddress(), device.getCanPort());
            if (comDevice == null) {
                logger.info("电表设备不存在时,则导入");
                device.setNid(nid);
                baseMapper.insert(device);
                logger.info("导入点表回路");
                List<ComChannel> channels = comChannels.stream()
                        .filter((comChannel) -> comChannel.getComPort().equals(device.getCanPort()))
                        .filter((comChannel) -> comChannel.getDeviceAddress().equals(device.getDeviceAddress()))
                        .collect(Collectors.toList());
                comChannelService.saveList(channels, nid, device);
            }
        }
    }

    @Override
    public void deleteByNid(Integer nid) {
        baseMapper.deleteByNid(nid);
    }
}
