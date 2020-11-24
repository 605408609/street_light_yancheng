/**
 * @filename:PermissionDao 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.ua.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description: 权限数据访问层
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface PermissionDao extends BaseMapper<Permission> {

    /**
     * 根据角色id获取权限
     *
     * @param roleId
     * @return
     */
    List<Permission> selectByRoleId(Integer roleId);

    /**
     * 根据权限id集合查询所有权限
     *
     * @param permissionIdList
     * @return
     */
    List<Permission> selectByIdList(List<Integer> permissionIdList);
}
