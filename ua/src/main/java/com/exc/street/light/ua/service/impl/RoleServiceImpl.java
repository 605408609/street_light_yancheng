/**
 * @filename:RoleServiceImpl 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.*;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.UaReqRoleVO;
import com.exc.street.light.resource.vo.resp.UaRespPermissionVO;
import com.exc.street.light.resource.vo.resp.UaRespRoleVO;
import com.exc.street.light.ua.mapper.RoleDao;
import com.exc.street.light.ua.qo.RoleQueryObject;
import com.exc.street.light.ua.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 角色管理服务实现类
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    protected Result result = new Result();

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    @Lazy
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Override
    public List<Role> findByUserId(Integer userId) {
        return roleDao.selectByUserId(userId);
    }

    @Override
    public Result add(UaReqRoleVO reqRoleVO, HttpServletRequest request) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Role role = new Role();
        role.setCreateTime(new Date());
        role.setName(reqRoleVO.getName());
        role.setFounderId(userId);
        roleDao.insert(role);
        bindRolePermission(reqRoleVO.getPermissionIdList(), role.getId());
        return result.success("新增角色成功");
    }

    @Override
    public Result modify(UaReqRoleVO reqRoleVO, HttpServletRequest request) {
        Role role = roleDao.selectById(reqRoleVO.getId());
        role.setName(reqRoleVO.getName());
        role.setUpdateTime(new Date());
        roleDao.updateById(role);
        // 删除之前角色关联权限
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", role.getId());
        rolePermissionService.remove(queryWrapper);
        // 绑定新关联的角色权限关系
        bindRolePermission(reqRoleVO.getPermissionIdList(), role.getId());
        return result.success("编辑角色成功");
    }

    @Override
    public Result delete(Integer roleId, HttpServletRequest request) {
        // 删除角色前查询该角色下是否有正常用户
        List<User> userList = userService.selectByRoleId(roleId);
        if (userList != null && userList.size() > 0) {
            return result.error("删除角色失败，该角色还有关联用户");
        }
        // 删除用户角色关联关系
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId, roleId);
        userRoleService.remove(wrapper);
        // 删除角色关联的权限
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        rolePermissionService.remove(queryWrapper);
        // 删除角色
        roleDao.deleteById(roleId);
        return result.success("删除角色成功");
    }

    @Override
    public Result get(Integer roleId) {
        // 获取角色对象
        Role role = roleDao.selectById(roleId);
        // 根据角色id查询权限
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<RolePermission> rolePermissionList = rolePermissionService.list(queryWrapper);
        // 权限id集合
        List<Integer> permissionIdList = rolePermissionList.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        UaReqRoleVO reqRoleVO = new UaReqRoleVO();
        reqRoleVO.setPermissionIdList(permissionIdList);
        // 添加一个0防止为空时报错
        permissionIdList.add(0);
        // 根据权限id集合查询所有权限
        List<Permission> permissionList = permissionService.selectByIdList(permissionIdList);
        // 递归生成树状结构数据
        List<UaRespPermissionVO> respPermissionVOList = permissionService.buildPermissionTree(permissionList, 0);
        reqRoleVO.setRespPermissionVOList(respPermissionVOList);
        reqRoleVO.setName(role.getName());
        reqRoleVO.setFounderId(role.getFounderId());
        return result.success(reqRoleVO);
    }

    @Override
    public Result getPages(HttpServletRequest request, RoleQueryObject qo) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        boolean isAdmin = userService.isAdmin(userId);
        List<Role> roleList = roleDao.selectByUserId(userId);
        // 判断该用户是否是超级管理员
        if (!isAdmin) {
            qo.setFounderId(userId);
            qo.setRoleId(roleList.get(0).getId());
        }
        Page<UaRespRoleVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<UaRespRoleVO> list = roleDao.getPageList(page, qo.getFounderId(), qo.getRoleId());
        return result.success(list);
    }

    @Override
    public Result getRoleList(HttpServletRequest request) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        boolean isAdmin = userService.isAdmin(userId);
        Role role = new Role();
        // 判断该用户是否是超级管理员
        if (!isAdmin) {
            role.setFounderId(userId);
        }
        List<Role> roleList = roleDao.selectByUserId(userId);
        role.setId(roleList.get(0).getId());
        List<Role> roleVOList = roleDao.getRoleList(role.getFounderId(), role.getId());
        return result.success(roleVOList);
    }

    /**
     * 绑定角色与权限的关系
     *
     * @param permissionIdList
     * @param roleId
     */
    private void bindRolePermission(List<Integer> permissionIdList, Integer roleId) {
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for (Integer permissionId : permissionIdList) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(permissionId);
            rolePermission.setRoleId(roleId);
            rolePermissionList.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissionList);
    }

    @Override
    public Result uniqueness(Integer id, String name) {
        int isUniqueness = 0;
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        Role role = roleDao.selectOne(queryWrapper);
        // 编辑时判重
        if (id != null) {
            if (role != null && !role.getId().equals(id)) {
                isUniqueness = 1;
            }
        } else if (role != null) {
            isUniqueness = 1;
        }
        Result<Integer> result = new Result<>();
        return result.success(isUniqueness);
    }
}