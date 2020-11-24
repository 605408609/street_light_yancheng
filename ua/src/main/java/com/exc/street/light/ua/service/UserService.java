/**
 * @filename:UserService 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.ua.qo.UserQueryObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**   
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface UserService extends IService<User> {

    /**
     * 判断用户是否是超级管理员
     *
     * @param userId
     * @return
     */
    boolean isAdmin(Integer userId);

    /**
     * 用户登录
     *
     * @param json
     * @param httpServletRequest
     * @return
     */
    Result login(JSONObject json, HttpServletRequest httpServletRequest);

    /**
     * 新增用户
     *
     * @param user
     * @param request
     * @return
     */
    Result add(User user, HttpServletRequest request);

    /**
     * 删除用户
     *
     * @param id
     * @param request
     * @return
     */
    Result delete(Integer id, HttpServletRequest request);

    /**
     * 用户详情
     *
     * @param id
     * @return
     */
    Result get(Integer id);

    /**
     * 根据用户id查询名称集合
     * @param ids
     * @return
     */
    Result selectNameByUserIds(String ids);

    /**
     * 编辑用户
     * @param user
     * @param request
     * @return
     */
    Result modify(User user, HttpServletRequest request);

    /**
     * 用户列表
     *
     * @param request
     * @param queryObject
     * @return
     */
    Result getPages(HttpServletRequest request, UserQueryObject queryObject);

    /**
     * 退出登录
     * @param user
     * @return
     */
    Result logout(User user);

    /**
     * 校验密码
     * @param request
     * @param oldPassword
     * @return
     */
    Result checkPassword(HttpServletRequest request, String oldPassword);

    /**
     * 修改密码
     * @param password
     * @return
     */
    Result modifyPassword(HttpServletRequest request, String password);

    /**
     * 账号名称唯一性校验
     * @param id
     * @param accountName
     * @return
     */
    Result uniqueness(Integer id, String accountName);

    /**
     * 分区和用户列表
     * @return
     */
    Result areaUserList(HttpServletRequest request);

    /**
     * 用户下拉列表
     * @param request
     * @return
     */
    Result getUserPullList(HttpServletRequest request);

    /**
     * 删除区域后用户绑定的区域id设为空
     * @param areaId
     * @return
     */
    Result updateUserByAreaId(Integer areaId);

    /**
     * 根据用户id查询其父级的founderId与超管id相等的用户id
     * @param id
     * @return
     */
    Result getUserId(Integer id);

    /**
     * 踢用户下线
     * @param id
     * @return
     */
    Result KickOff(Integer id);

    /**
     * 禁用用户
     * @param id
     * @param forbidden
     * @return
     */
    Result forbidden(Integer id, Integer forbidden);

    /**
     * 根据角色id查询正常用户
     * @param roleId
     * @return
     */
    List<User> selectByRoleId(Integer roleId);

}