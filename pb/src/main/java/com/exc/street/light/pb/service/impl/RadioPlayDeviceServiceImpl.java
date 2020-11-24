/**
 * @filename:RadioPlayDeviceServiceImpl 2020-05-11
 * @project pb  V1.0
 * Copyright(c) 2018 XiaoKun Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.pb.mapper.RadioPlayDeviceMapper;
import com.exc.street.light.pb.service.RadioDeviceService;
import com.exc.street.light.pb.service.RadioPlayDeviceService;
import com.exc.street.light.resource.entity.pb.RadioPlayDevice;
import com.exc.street.light.resource.vo.req.PbReqRadioPlayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:TODO(广播播放设备中间表服务实现)
 * @version: V1.0
 * @author: XiaoKun
 */
@Service
public class RadioPlayDeviceServiceImpl extends ServiceImpl<RadioPlayDeviceMapper, RadioPlayDevice> implements RadioPlayDeviceService {

    @Autowired
    private RadioDeviceService radioDeviceService;

    @Override
    public boolean saveBind(PbReqRadioPlayVO pbReqRadioPlayVO) {
        if (pbReqRadioPlayVO == null || pbReqRadioPlayVO.getId() == null || pbReqRadioPlayVO.getDeviceType() == null) {
            return false;
        }
        //1、删除定时广播所有的关联
        LambdaQueryWrapper<RadioPlayDevice> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(RadioPlayDevice::getPlayId, pbReqRadioPlayVO.getId());
        this.remove(deleteWrapper);
        List<Integer> deviceIdList = new ArrayList<>();
        List<Integer> groupIds = pbReqRadioPlayVO.getGroupIds();
        Date now = new Date();
        //需要保存关联的数组
        List<RadioPlayDevice> radioPlayDeviceList = new ArrayList<>();
        if (pbReqRadioPlayVO.getDeviceType().equals(2) && groupIds != null && !groupIds.isEmpty()) {
            //2、保存定时广播与分组的关联
            deviceIdList = radioDeviceService.getIdListByGroupIdList(pbReqRadioPlayVO.getGroupIds());
            //保存分组与定时广播之间的绑定
            for (Integer groupId : groupIds) {
                RadioPlayDevice radioPlayDevice = new RadioPlayDevice();
                radioPlayDevice.setPlayId(pbReqRadioPlayVO.getId());
                radioPlayDevice.setCreateTime(now);
                radioPlayDevice.setGroupId(groupId);
                radioPlayDeviceList.add(radioPlayDevice);
            }
        } else if (pbReqRadioPlayVO.getDeviceType().equals(1)) {
            deviceIdList = pbReqRadioPlayVO.getDeviceIds();
        }
        //3、保存定时广播与设备的关联
        if (!deviceIdList.isEmpty()) {
            for (Integer deviceId : deviceIdList) {
                RadioPlayDevice radioPlayDevice = new RadioPlayDevice();
                radioPlayDevice.setPlayId(pbReqRadioPlayVO.getId());
                radioPlayDevice.setDeviceId(deviceId);
                radioPlayDevice.setCreateTime(now);
                radioPlayDeviceList.add(radioPlayDevice);
            }
        }
        this.saveBatch(radioPlayDeviceList);
        return true;
    }
}