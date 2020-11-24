/**
 * @filename:UserRoleServiceImpl 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.service.impl;

import com.exc.street.light.resource.entity.ua.UserRole;
import com.exc.street.light.ua.mapper.UserRoleDao;
import com.exc.street.light.ua.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**   
 * @Description: 用户角色中间表服务接口实现类
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Service
public class UserRoleServiceImpl  extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService  {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public List<UserRole> findByUserId(Integer userId) {
        return userRoleDao.selectByUserId(userId);
    }

    @Override
    public List<UserRole> selectByRoleId(Integer roleId) {
        return userRoleDao.selectByRoleId(roleId);
    }
}