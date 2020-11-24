/**
 * @filename:DeviceUpgradeLogStatusService 2020-08-25
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.DeviceUpgradeLogStatus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.sl.LampDevice;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(设备升级状态表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
public interface DeviceUpgradeLogStatusService extends IService<DeviceUpgradeLogStatus> {

    /**
     * 重新升级
     * @param request
     * @return
     */
    Result upgradeStatusAgain(Integer logId, HttpServletRequest request);

    /**
     * 记录升级状态及升级记录及升级
     */
    Result upgradeStatus(MultipartFile multipartFile, String deviceIdList, HttpServletRequest request);

    /**
     * 根据升级记录id获取对应的升级状态集合
     * @param logId
     * @return
     */
    List<DeviceUpgradeLogStatus> getListByLogId(Integer logId);
}