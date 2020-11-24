/**
 * @filename:PermissionServiceImpl 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ua.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.Permission;
import com.exc.street.light.resource.entity.ua.Role;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.resp.UaRespPermissionVO;
import com.exc.street.light.ua.mapper.PermissionDao;
import com.exc.street.light.ua.service.PermissionService;
import com.exc.street.light.ua.service.RoleService;
import com.exc.street.light.ua.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 权限服务接口实现类
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements PermissionService {

    protected Result result = new Result();

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Override
    public List<Permission> findByRoleId(Integer roleId) {
        return permissionDao.selectByRoleId(roleId);
    }

    @Override
    public List<Permission> selectByIdList(List<Integer> permissionIdList) {
        return permissionDao.selectByIdList(permissionIdList);
    }

    @Override
    public List<UaRespPermissionVO> buildPermissionTree(List<Permission> permissionList, Integer parentId) {
        List<UaRespPermissionVO> respPermissionVOList = new ArrayList<>();
        // 父id为parentId的子集
        List<Permission> collect = permissionList.stream()
                .filter(a -> a.getParentId().equals(parentId)).collect(Collectors.toList());
        // 循环子集
        for (Permission permission : collect) {
            UaRespPermissionVO respPermissionVO = new UaRespPermissionVO();
            // 递归得到子集
            List<UaRespPermissionVO> respPermissionVOList1 = buildPermissionTree(permissionList, permission.getId());
            // 构造返回对象
            BeanUtils.copyProperties(permission, respPermissionVO);
            respPermissionVO.setRespPermissionVOList(respPermissionVOList1);
            respPermissionVOList.add(respPermissionVO);
        }
        return respPermissionVOList;
    }

    @Override
    public Result selectAll(HttpServletRequest request) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        List<UaRespPermissionVO> respPermissionVOList = getUaRespPermissionVOS(userId);
        return result.success(respPermissionVOList);
    }

    @Override
    public Result selectByFounderId(Integer founderId, HttpServletRequest request) {
        List<UaRespPermissionVO> uaRespPermissionVos = getUaRespPermissionVOS(founderId);
        Result result = new Result();
        return result.success(uaRespPermissionVos);
    }

    private List<UaRespPermissionVO> getUaRespPermissionVOS(Integer userId) {
        boolean isAdmin = userService.isAdmin(userId);
        List<Permission> permissionList = null;
        if (isAdmin) {
            LambdaUpdateWrapper<Permission> wrapper = new LambdaUpdateWrapper<>();
            wrapper.orderByAsc(Permission::getOrders);
            permissionList = permissionDao.selectList(wrapper);
            for (int i = 0; i < permissionList.size(); i++) {
                removePermission(permissionList, i);
            }
        } else {
            List<Role> roleList = roleService.findByUserId(userId);
            if (roleList != null) {
                Integer roleId = roleList.get(0).getId();
                permissionList = permissionDao.selectByRoleId(roleId);
                // 如果不是超级管理员的话，其他用户在给角色设置权限时就没有工单审核的权限
                for (int i = 0; i < permissionList.size(); i++) {
                    if ("permission:module:order:create:check".equals(permissionList.get(i).getCode())) {
                        permissionList.remove(permissionList.get(i));
                    }
                    if ("dm:module:group".equals(permissionList.get(i).getCode())) {
                        permissionList.remove(permissionList.get(i));
                    }
                    removePermission(permissionList, i);
                }
            }
        }
        // 生成权限树
        return buildPermissionTree(permissionList, 0);
    }

    /**
     * 删除指定的权限，只有超管才有分区的权限，新增和编辑角色时，权限树没有这个勾选框
     * @param permissionList
     * @param i
     */
    private void removePermission(List<Permission> permissionList, int i) {
        if ("dm:module:location".equals(permissionList.get(i).getCode())) {
            permissionList.remove(permissionList.get(i));
        }
        // 没有场景下发路由
        if (permissionList.get(i).getIsShow() == 0) {
            permissionList.remove(permissionList.get(i));
        }
    }

}