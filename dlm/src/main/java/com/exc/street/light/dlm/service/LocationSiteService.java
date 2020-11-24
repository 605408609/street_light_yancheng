/**
 * @filename:LocationSiteService 2020-03-16
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationSite;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface LocationSiteService extends IService<LocationSite> {

    /**
     * 查询站点下拉列表
     *
     * @param streetId
     * @param siteName
     * @param request
     * @return
     */
    Result pulldown(Integer streetId, String siteName, HttpServletRequest request);

    /**
     * 添加站点
     *
     * @param locationSite
     * @param request
     * @return
     */
    Result add(LocationSite locationSite, HttpServletRequest request);

    /**
     * 根据街道id集合查询站点集合
     *
     * @param streetIdList
     * @param request
     * @return
     */
    Result getByStreet(List<Integer> streetIdList, HttpServletRequest request);

    /**
     * 删除站点
     *
     * @param id
     * @param request
     * @return
     */
    Result delete(Long id, HttpServletRequest request);

    /**
     * 根据街道id集合删除站点
     *
     * @param streetIdList
     * @param request
     * @return
     */
    Result deleteByStreetIdList(List<Integer> streetIdList, HttpServletRequest request);

    /**
     * 站点验证唯一性
     *
     * @param locationSite
     * @param request
     * @return
     */
    Result unique(LocationSite locationSite, HttpServletRequest request);

    /**
     * 修改站点
     *
     * @param locationSite
     * @param request
     * @return
     */
    Result updateSite(LocationSite locationSite, HttpServletRequest request);

    /**
     * 根据id查询站点信息
     *
     * @param id
     * @param request
     * @return
     */
    Result get(Long id, HttpServletRequest request);
}