/**
 * @filename:LampDeviceService 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.entity.sl.LampDevice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**   
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
public interface LampDeviceService extends IService<LampDevice> {

    /**
     * 批量插入
     * @param lampDeviceList
     * @return
     */
    Result addList(List<LampDevice> lampDeviceList);

    /**
     * 查询所有
     */
    Result selectAll();
}