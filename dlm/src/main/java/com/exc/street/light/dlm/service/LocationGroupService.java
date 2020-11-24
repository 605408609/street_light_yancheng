/**
 * @filename:GroupService 2020-03-18
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationGroup;
import com.exc.street.light.resource.qo.DlmGroupQuery;
import com.exc.street.light.resource.vo.req.DlmReqGroupVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface LocationGroupService extends IService<LocationGroup> {

    /**
     * 分组下拉列表
     *
     * @param groupName
     * @param request
     * @return
     */
    Result pulldown(String groupName, HttpServletRequest request);

    /**
     * 添加分组
     *
     * @param dlmReqGroupVO
     * @param request
     * @return
     */
    Result add(DlmReqGroupVO dlmReqGroupVO, HttpServletRequest request);

    /**
     * 分组分页条件查询
     *
     * @param dlmGroupQuery
     * @param request
     * @return
     */
    Result getPage(DlmGroupQuery dlmGroupQuery, HttpServletRequest request);

    /**
     * 获取分组详情
     *
     * @param groupId
     * @param request
     * @return
     */
    Result get(Integer groupId, HttpServletRequest request);

    /**
     * 修改分组
     *
     * @param dlmReqGroupVO
     * @param request
     * @return
     */
    Result updateGroup(DlmReqGroupVO dlmReqGroupVO, HttpServletRequest request);

    /**
     * 查询分组详细列表
     *
     * @param hierarchy
     * @param deviceTypeNum
     * @param typeId
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    Result getList(Integer hierarchy, Integer deviceTypeNum, Integer typeId, Integer pageNum, Integer pageSize, HttpServletRequest request);

    /**
     * 根据id删除分组
     *
     * @param id
     * @param request
     * @return
     */
    Result delete(Long id, HttpServletRequest request);

    /**
     * 分组验证唯一性
     *
     * @param dlmReqGroupVO
     * @param request
     * @return
     */
    Result unique(DlmReqGroupVO dlmReqGroupVO, HttpServletRequest request);
}