/**
 * @filename:RadioPlayDeviceService 2020-05-11
 * @project pb  V1.0
 * Copyright(c) 2020 XiaoKun Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.pb.service;

import com.exc.street.light.resource.entity.pb.RadioPlayDevice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.req.PbReqRadioPlayVO;

/**
 * @Description:TODO(广播播放设备中间表服务层)
 * @version: V1.0
 * @author: XiaoKun
 * 
 */
public interface RadioPlayDeviceService extends IService<RadioPlayDevice> {

    /**
     * 保存定时广播与（分组id，设备id）之间的绑定
     * @param pbReqRadioPlayVO
     * @return
     */
    public boolean saveBind(PbReqRadioPlayVO pbReqRadioPlayVO);
}