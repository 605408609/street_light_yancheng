package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.dlm.DeviceOnline;
import com.exc.street.light.resource.vo.DeviceAllVo;
import com.exc.street.light.resource.vo.DeviceTypeGroupVO;

import java.util.List;

public interface DeviceOnlineService extends IService<DeviceOnline> {
    List<DeviceTypeGroupVO> typeGroup(String dateTime,List<Integer> lampPostIdList);
    List<DeviceAllVo> deviceAll();

    /**
     * 查询时间当天是否已经存在记录
     * @param id
     * @param type
     * @param format
     * @return
     */
    List<DeviceOnline> deviceExist(Integer id, Integer type, String format);
}
