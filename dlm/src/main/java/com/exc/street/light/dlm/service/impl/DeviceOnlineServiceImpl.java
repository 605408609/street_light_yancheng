package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.DeviceOnlineDao;
import com.exc.street.light.dlm.service.DeviceOnlineService;
import com.exc.street.light.resource.entity.dlm.DeviceOnline;
import com.exc.street.light.resource.vo.DeviceAllVo;
import com.exc.street.light.resource.vo.DeviceTypeGroupVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceOnlineServiceImpl extends ServiceImpl<DeviceOnlineDao, DeviceOnline> implements DeviceOnlineService {
    @Override
    public List<DeviceTypeGroupVO> typeGroup(String dateTime,List<Integer> lampPostIdList) {
        return this.baseMapper.typeGroup(dateTime,lampPostIdList);
    }

    @Override
    public List<DeviceAllVo> deviceAll() {
        return this.baseMapper.deviceAll();
    }

    @Override
    public List<DeviceOnline> deviceExist(Integer id, Integer type, String format) {
        return this.baseMapper.deviceExist(id,type,format);
    }
}
