/**
 * @filename:DeviceUpgradeLogService 2020-08-25
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.DeviceUpgradeLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.resp.SlRespDeviceUpgradeLogVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(升级记录表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
public interface DeviceUpgradeLogService extends IService<DeviceUpgradeLog> {

    /**
     * 查询升级记录列表
     * @param isSuccess
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    Result pulldown(Integer isSuccess,Integer pageNum,Integer pageSize,HttpServletRequest request);

    /**
     * 删除升级记录
     * @param id
     * @param request
     * @return
     */
    Result delete(Integer id,HttpServletRequest request);

}