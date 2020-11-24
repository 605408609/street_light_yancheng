/**
 * @filename:LocationControlService 2020-09-15
 * @project sl  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.dlm.LocationControl;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: LeiJing
 * 
 */
public interface LocationControlService extends IService<LocationControl> {
	/**
	 * 查询中科智联集中控制器下所有的单灯控制器并修改为其在线/离线状态，status: 0 离线，1 在线
	 *
	 * @param concentratorId	中科智联集中控制器ID，唯一编号
	 * @param status			0 离线，1 在线
	 */
	void findAllAndUpdateStatus(String concentratorId, Integer status);

	/**
	 * 单灯控制器修改在线/离线状态
	 *
	 * @param num		中科智联单灯控制器ID，唯一编号
	 * @param status	0 离线，1 在线
	 */
	void updateStatusByNum(String num, Integer status);
}