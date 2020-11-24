/**
 * @filename:RoleDao 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.ua.Role;
import com.exc.street.light.resource.vo.resp.UaRespRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface RoleDao extends BaseMapper<Role> {

    /**
     * 根据用户id查询角色
     *
     * @param userId
     * @return
     */
    List<Role> selectByUserId(@Param("userId") Integer userId);

    /**
     * 角色列表
     * @param page
     * @param founderId
     * @return
     */
    IPage<UaRespRoleVO> getPageList(Page<UaRespRoleVO> page, @Param("founderId") Integer founderId, @Param("roleId") Integer roleId);

    /**
     * 角色下拉列表
     * @param founderId
     * @return
     */
    List<Role> getRoleList(@Param("founderId") Integer founderId, @Param("roleId") Integer roleId);
}
