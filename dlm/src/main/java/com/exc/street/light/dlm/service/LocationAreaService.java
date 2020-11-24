/**
 * @filename:LocationAreaService 2020-03-16
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationArea;
import com.exc.street.light.resource.qo.DlmControlLoopOfDeviceQuery;
import com.exc.street.light.resource.vo.req.DlmReqLocationAreaVO;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface LocationAreaService extends IService<LocationArea> {


    /**
     * 获取市级灯具信息
     * @return
     */
    Result getLampCity(HttpServletRequest request);

    /**
     * 城市灯具控制
     * @param request
     * @param vo
     * @return
     */
    Result cityControl(HttpServletRequest request,SlReqLightControlVO vo);


    /**
     * 灯具整体控制
     * @param request
     * @param vo
     * @return
     */
    Result wholeControl(HttpServletRequest request,SlReqLightControlVO vo);

    /**
     * 查询区域详细列表
     *
     * @param hierarchy
     * @param deviceTypeNum
     * @param eliminate
     * @param pageNum
     * @param pageSize
     * @param lampPositionId
     * @param request
     * @return
     */
    Result getList(Integer hierarchy, Integer deviceTypeNum, Integer eliminate, Integer pageNum, Integer pageSize , Integer lampPositionId, HttpServletRequest request);

    /**
     * 根据设备型号/升级记录查询灯具下拉列表
     * @param deviceType
     * @param logId
     * @param isSuccess
     * @param eliminate
     * @param pageNum
     * @param pageSize
     * @param lampPositionId
     * @param request
     * @return
     */
    Result getListByDeviceType(Integer deviceType,Integer logId,Integer isSuccess, Integer eliminate, Integer pageNum, Integer pageSize, Integer lampPositionId, HttpServletRequest request);

    /**
     * 查询区域下拉列表
     *
     * @param areaName
     * @return
     */
    Result pulldown(String areaName, HttpServletRequest request);

    /**
     * 添加区域
     *
     * @param locationAreaVO
     * @param request
     * @return
     */
    Result add(DlmReqLocationAreaVO locationAreaVO, HttpServletRequest request);

    /**
     * 根据id删除分区
     *
     * @param id
     * @param request
     * @return
     */
    Result delete(Long id, HttpServletRequest request);

    /**
     * 区域验证唯一性
     *
     * @param locationArea
     * @param request
     * @return
     */
    Result unique(LocationArea locationArea, HttpServletRequest request);

    /**
     * 修改区域
     *
     * @param locationArea
     * @param request
     * @return
     */
    Result updateArea(DlmReqLocationAreaVO locationArea, HttpServletRequest request);

    /**
     * 根据区域id查询区域详情
     * @param id
     * @param request
     * @return
     */
    Result get(Integer id, HttpServletRequest request);

    /**
     * 根据集控id和分组id查询灯具
     * @param loopQuery
     * @param request
     * @return
     */
    Result getDeviceByControlIdAndLoopId(DlmControlLoopOfDeviceQuery loopQuery, HttpServletRequest request);

    /**
     * 根据设备型号查询灯具下拉列表
     * @param request
     * @return
     */
    //Result pulldownByDeviceType(HttpServletRequest request);
}