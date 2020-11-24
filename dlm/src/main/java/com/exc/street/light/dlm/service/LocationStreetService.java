/**
 * @filename:LocationStreetService 2020-03-16
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationStreet;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface LocationStreetService extends IService<LocationStreet> {

    /**
     * 查询街道下拉列表
     *
     * @param areaId
     * @param streetName
     * @param request
     * @return
     */
    Result pulldown(Integer areaId, String streetName, HttpServletRequest request);

    /**
     * 添加街道
     *
     * @param locationStreet
     * @param request
     * @return
     */
    Result add(LocationStreet locationStreet, HttpServletRequest request);

    /**
     * 根据区域id集合查询街道集合
     *
     * @param areaIdList
     * @param request
     * @return
     */
    Result getByArea(List<Integer> areaIdList, HttpServletRequest request);

    /**
     * 根据id删除街道
     *
     * @param id
     * @param request
     * @return
     */
    Result delete(Long id, HttpServletRequest request);

    /**
     * 根据区域id集合删除街道
     *
     * @param areaIdList
     * @param request
     * @return
     */
    Result deleteByAreaIdList(List<Integer> areaIdList, HttpServletRequest request);

    /**
     * 街道验证唯一性
     *
     * @param locationStreet
     * @param request
     * @return
     */
    Result unique(LocationStreet locationStreet, HttpServletRequest request);

    /**
     * 修改街道
     *
     * @param locationStreet
     * @param request
     * @return
     */
    Result updateStreet(LocationStreet locationStreet, HttpServletRequest request);


    /**
     * 根据id查询街道信息
     *
     * @param id
     * @param request
     * @return
     */
    Result get(Long id, HttpServletRequest request);
}