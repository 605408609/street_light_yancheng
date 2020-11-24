package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.ComChannelMapper;
import com.exc.street.light.electricity.service.ComChannelService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.ComChannel;
import com.exc.street.light.resource.entity.electricity.ComDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Linshiwen
 * @date 2018/07/04
 */
@Service
public class ComChannelServiceImpl extends ServiceImpl<ComChannelMapper,ComChannel> implements ComChannelService {
    private static final Logger logger = LoggerFactory.getLogger(ComChannelServiceImpl.class);

    @Override
    public Result saveList(List<ComChannel> comChannels, Integer nid, ComDevice comDevice) {
        Integer comDeviceId = comDevice.getId();
        for (ComChannel comChannel : comChannels) {
            comChannel.setCid(comDeviceId);
            comChannel.setNid(nid);
            int num = baseMapper.getByTagIdAndNid(nid, comChannel.getTagId());
            logger.info("查询数据库是否已保存该记录:" + num);
            if (num == 0) {
                logger.info("num=" + num + ",不存在则新增记录");
                baseMapper.insert(comChannel);
            }
        }
        return new Result().success("");
    }

}
