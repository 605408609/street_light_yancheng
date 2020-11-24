/**
 * @filename:UserService 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.log_api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.ua.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xuJiaHao
 *
 */
public interface LogUserService extends IService<User> {

    /**
     * 用户详情
     *
     * @param id
     * @return
     */
    User get(Integer id);

    /**
     * 判断用户是否是超级管理员
     *
     * @param userId
     * @return
     */
    boolean isAdmin(Integer userId);

    /**
     * 获取当前区域下的管理员id集合
     * @param request
     * @return
     */
    List<Integer> getManagerIdList(HttpServletRequest request);

}