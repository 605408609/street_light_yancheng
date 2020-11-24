/**
 * @filename:UserRoleService 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.service;

import com.exc.street.light.resource.entity.ua.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**   
 * @Description: 用户角色中间表服务接口
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户id查找
     *
     * @param userId
     * @return
     */
    List<UserRole> findByUserId(Integer userId);

    /**
     * 根据角色id查询用户
     * @param roleId
     * @return
     */
    List<UserRole> selectByRoleId(Integer roleId);
}