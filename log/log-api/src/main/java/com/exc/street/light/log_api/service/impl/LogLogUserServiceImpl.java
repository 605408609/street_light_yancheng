/**
 * @filename:UserServiceImpl 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.log_api.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.mapper.LogUserMapper;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: xuJiaHao
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class LogLogUserServiceImpl extends ServiceImpl<LogUserMapper, User> implements LogUserService {

    private final LogUserMapper userMapper;

    @Override
    public User get(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id", id)
                .eq("status", 1);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public boolean isAdmin(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user.getType() == 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<Integer> getManagerIdList(HttpServletRequest request) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = baseMapper.selectById(userId);
        List<Integer> userIdList = new ArrayList<>();
        if (user != null && user.getAreaId() != null) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getAreaId, user.getAreaId())
                    .eq(User::getFounderId, 1)
                    .eq(User::getStatus, 1);
            List<User> userList = baseMapper.selectList(wrapper);
            if (userList != null && userList.size() > 0) {
                userIdList = userList.stream().map(User::getId).collect(Collectors.toList());

            }
        }
        return userIdList;
    }

}