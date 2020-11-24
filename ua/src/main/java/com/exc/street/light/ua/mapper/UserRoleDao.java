/**
 * @filename:UserRoleDao 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.ua.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description: 用户角色数据访问层
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface UserRoleDao extends BaseMapper<UserRole> {


    /**
     * 根据用户id查询角色
     *
     * @param userId
     * @return
     */
    List<UserRole> selectByUserId(Integer userId);

    /**
     * 根据角色id查询用户
     * @param roleId
     * @return
     */
    List<UserRole> selectByRoleId(Integer roleId);
}
