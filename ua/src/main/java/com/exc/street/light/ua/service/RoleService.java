/**
 * @filename:RoleService 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.Role;
import com.exc.street.light.resource.vo.req.UaReqRoleVO;
import com.exc.street.light.ua.qo.RoleQueryObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户id查询角色
     *
     * @param userId
     * @return
     */
    List<Role> findByUserId(Integer userId);

    /**
     * 新增角色
     * @param reqRoleVO
     * @return
     */
    Result add(UaReqRoleVO reqRoleVO, HttpServletRequest request);

    /**
     * 编辑角色
     * @param reqRoleVO
     * @param request
     * @return
     */
    Result modify(UaReqRoleVO reqRoleVO, HttpServletRequest request);

    /**
     * 删除角色
     * @param id
     * @param request
     * @return
     */
    Result delete(Integer id, HttpServletRequest request);

    /**
     * 角色详情
     * @param id
     * @return
     */
    Result get(Integer id);

    /**
     * 角色列表
     * @param request
     * @return
     */
    Result getPages(HttpServletRequest request, RoleQueryObject queryObject);

    /**
     * 角色下拉列表
     * @param request
     * @return
     */
    Result getRoleList(HttpServletRequest request);

    /**
     * 角色名称的唯一性校验
     * @param id
     * @param name
     * @return
     */
    Result uniqueness(Integer id, String name);

}