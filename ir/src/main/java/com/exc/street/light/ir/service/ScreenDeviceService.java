/**
 * @filename:ScreenDeviceService 2020-03-17
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.qo.IrScreenDeviceQuery;
import com.exc.street.light.resource.vo.req.IrReqScreenOperateVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface ScreenDeviceService extends IService<ScreenDevice> {

    /**
     * 查询显示屏(根据灯杆id集合)
     *
     * @param lampPostIdList
     * @param request
     * @return
     */
    Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request);

    /**
     * 添加显示屏
     *
     * @param screenDevice
     * @param request
     * @return
     */
    Result add(ScreenDevice screenDevice, HttpServletRequest request);

    /**
     * 显示屏更多操作，屏幕开关，设置音量，设置亮度
     *
     * @param irReqScreenOperateVO
     * @param httpServletRequest
     * @return
     */
    Result operate(IrReqScreenOperateVO irReqScreenOperateVO, HttpServletRequest httpServletRequest);

    /**
     * 批量删除显示屏设备
     *
     * @param ids
     * @param request
     * @return
     */
    Result batchDelete(String ids, HttpServletRequest request);

    /**
     * 修改显示屏设备
     *
     * @param screenDevice
     * @param request
     * @return
     */
    Result updateDevice(ScreenDevice screenDevice, HttpServletRequest request);

    /**
     * 获取显示屏设备列表(分页查询)
     *
     * @param irScreenDeviceQuery
     * @param httpServletRequest
     * @return
     */
    Result getQuery(IrScreenDeviceQuery irScreenDeviceQuery, HttpServletRequest httpServletRequest);

    /**
     * 显示屏设备验证唯一性
     *
     * @param screenDevice
     * @param request
     * @return
     */
    Result unique(ScreenDevice screenDevice, HttpServletRequest request);

    /**
     * 获取显示屏详情
     *
     * @param id
     * @param request
     * @return
     */
    Result detail(Integer id, HttpServletRequest request);

    /**
     * 刷新设备数据
     *
     * @param screenDeviceIdList
     * @return
     */
    Result refresh(List<Integer> screenDeviceIdList);

    /**
     * 刷新显示屏状态
     *
     * @return
     */
    Result getRefreshDate(List<Integer> screenDeviceIdList);

    /**
     * 获取截屏
     * @return
     */
    Result getScreenshots(Integer id);
}