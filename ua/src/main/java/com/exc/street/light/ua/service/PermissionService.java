/**
 * @filename:PermissionService 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.Permission;
import com.exc.street.light.resource.vo.resp.UaRespPermissionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 权限服务接口
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 根据角色id查询
     *
     * @param roleId
     * @return
     */
    List<Permission> findByRoleId(Integer roleId);

    /**
     * 根据权限id集合查询所有权限
     *
     * @param permissionIdList
     * @return
     */
    List<Permission> selectByIdList(List<Integer> permissionIdList);

    /**
     * 递归权限集合得到权限树状数据
     * @param permissionList
     * @param parentId
     * @return
     */
    List<UaRespPermissionVO> buildPermissionTree(List<Permission> permissionList, Integer parentId);

    /**
     * 权限树
     * @param request
     * @return
     */
    Result selectAll(HttpServletRequest request);

    /**
     * 根据创建人id查询权限信息
     * @param founderId
     * @return
     */
    Result selectByFounderId(Integer founderId, HttpServletRequest request);
}
