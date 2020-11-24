/**
 * @filename:SsDeviceService 2020-03-17
 * @project ss  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.vo.SsDeviceVO;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Huang Min
 * 
 */
public interface SsDeviceService extends IService<SsDevice> {
    /**
     * 批量插入
     * @param ssDeviceList
     * @return
     */
    Result addList(List<SsDevice> ssDeviceList);

    /**
     * 查询所有
     */
    Result selectAll();

    /**
     * 根据灯杆id获取灯具返回对象集合
     * @param slLampPostIdList
     * @return
     */
    List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList);
}