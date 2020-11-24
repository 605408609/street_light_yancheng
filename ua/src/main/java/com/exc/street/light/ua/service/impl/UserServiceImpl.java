/**
 * @filename:UserServiceImpl 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ua.service.impl;

import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.util.IpUtil;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ProjectPic;
import com.exc.street.light.resource.entity.log.LogLogin;
import com.exc.street.light.resource.entity.ua.Permission;
import com.exc.street.light.resource.entity.ua.Role;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.ua.UserRole;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.UaUserVO;
import com.exc.street.light.resource.vo.resp.DlmRespProjectPicVO;
import com.exc.street.light.resource.vo.resp.UaRespLocationAreaVO;
import com.exc.street.light.resource.vo.resp.UaRespPermissionVO;
import com.exc.street.light.resource.vo.resp.UaRespSimpleUserVO;
import com.exc.street.light.ua.mapper.RoleDao;
import com.exc.street.light.ua.mapper.UserDao;
import com.exc.street.light.ua.qo.UserQueryObject;
import com.exc.street.light.ua.service.*;
import com.exc.street.light.ua.util.Constans;
import com.exc.street.light.ua.util.RSAUtils;
import com.exc.street.light.ua.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 用户管理服务实现类
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    protected Result<Object> result = new Result<>();

    @Resource
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    @Lazy
    private PermissionService permissionService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LogLoginService logLoginService;

    @Autowired
    private ProjectPicService projectPicService;

    @Override
    public boolean isAdmin(Integer userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        User user = userDao.selectOne(queryWrapper);
        if (user.getType() == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Result login(JSONObject json, HttpServletRequest httpServletRequest) {
        // 登录日志对象
        LogLogin log = new LogLogin();
        log.setStatus(1);
        log.setCreateTime(new Date());
        log.setMethod(httpServletRequest.getMethod());
        log.setRequIp(IpUtil.getRemoteIp(httpServletRequest));
        log.setUri(URLUtil.getPath(httpServletRequest.getRequestURI()));
        log.setRequParam(json.toString());

        String phone = json.getString("username");
        String pwd = json.getString("password");
        String password = RSAUtils.decryptBase64(pwd.trim());
        System.out.println("解密后的密码" + password);
        User user = userDao.findByPhoneAndPassword(phone, password);
        long currentTime = System.currentTimeMillis();
        if (user != null) {
            if (user.getFailCount() == null) {
                user.setFailCount(0);
            }
            log.setOperatorId(user.getId());
            if (user.getForbidden() == 0) {
                log.setStatus(0);
                log.setDescription("用户:" + phone + "登录失败，账号已禁用，请联系管理员");
                logLoginService.save(log);
                return result.error("登录失败，账号已禁用，请联系管理员");
            }
            //验证用户有效期 1：永久有效 2：设定有效期 3：用户已过期
            if (user.getStatus() == 3) {
                log.setStatus(0);
                log.setDescription("用户:" + phone + "登录失败,用户已过有效期");
                logLoginService.save(log);
                return result.error("登录失败,用户已过有效期");
            }
            if (user.getPeriodType() == 2) {
                if (user.getValidityPeriod() != null) {
                    long currentTimeMillis = System.currentTimeMillis();
                    long validityPeriodTimeMillis = user.getValidityPeriod().getTime() + 24 * 60 * 60 * 1000;
                    if (currentTimeMillis > validityPeriodTimeMillis) {
                        //status==3 用户过期
                        user.setStatus(3);
                        userDao.updateById(user);
                        log.setStatus(0);
                        log.setDescription("用户:" + phone + "登录失败,用户已过有效期");
                        logLoginService.save(log);
                        return result.error("登录失败,用户已过有效期");
                    }
                }
            }
            //1天时间过期
            Map<String, Object> map = new HashMap<>(3);
            Date expireTime = new Date(currentTime + 24 * 60 * 60 * 1000);
            String token = JavaWebTokenUtil.createJavaWebToken(user.getId() + "", phone, expireTime);
            map.put("token", token);
            map.put("userId", user.getId());
            map.put("userName", user.getName());
            map.put("gender", user.getGender());
            map.put("accountName", user.getAccountName());
            List<Role> byUserId = roleDao.selectByUserId(user.getId());
            map.put("roleId", byUserId.get(0).getId());
            map.put("roleName", byUserId.get(0).getName());

            // 获取项目图片信息
            DlmRespProjectPicVO projectPicRespVO = projectPicService.getProjectPicRespVO(user.getId());
            if (projectPicRespVO != null && projectPicRespVO.getName() != null) {
                map.put("projectName", projectPicRespVO.getName());
            }

            // 区域名称用于app端使用
            UaRespLocationAreaVO areaVO = userDao.selectAreaById(user.getAreaId());
            if (areaVO != null) {
                map.put("areaId", user.getAreaId());
                map.put("areaName", areaVO.getName());
            }
            if (user.getType().equals(1)) {
                //超级管理员拥有所有权限
                LambdaUpdateWrapper<Permission> wrapper = new LambdaUpdateWrapper<>();
                wrapper.orderByAsc(Permission::getOrders);
                List<Permission> permissionList = permissionService.list(wrapper);
                List<UaRespPermissionVO> permissionVOList = permissionService.buildPermissionTree(permissionList, 0);
                map.put("permissionList", permissionList);
                map.put("permissions", permissionVOList);
            } else {
                // 根据用户id查询角色id
                List<UserRole> userRoles = userRoleService.findByUserId(user.getId());
                for (UserRole userRole : userRoles) {
                    List<Permission> permissionList = permissionService.findByRoleId(userRole.getRoleId());
                    List<UaRespPermissionVO> permissionVOList = permissionService.buildPermissionTree(permissionList, 0);
                    map.put("permissionList", permissionList);
                    map.put("permissions", permissionVOList);
                }
            }
            log.setDescription("用户:" + phone + "登录成功");
            log.setRespParam(JSONArray.toJSON(map.get("userName")).toString());
            // 更改在线状态
            user.setOnline(0);
            user.setOnlineTime(new Date());
            user.setFailCount(0);
            userDao.updateById(user);
            logLoginService.save(log);
            // 添加用户登录信息至Redis
            long expireTimeLong = (expireTime.getTime() - (new Date()).getTime()) / 1000;
            // 用户id为key，token集合为value
            redisUtil.lSet(Constans.ACCESS_TOKEN + user.getId(), token);
            redisUtil.expire(Constans.ACCESS_TOKEN + user.getId(), expireTimeLong);
            return this.result.success(JSONArray.toJSON(map));
        }
        log.setStatus(0);
        log.setDescription("用户:" + phone + "登录失败，用户名或密码错误");
        logLoginService.save(log);
        return result.error("用户名或密码错误");
    }

    @Override
    public Result add(User user, HttpServletRequest request) {
        // 查询手机号是否已存在
        Integer founderId = JavaWebTokenUtil.parserStaffIdByToken(request);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", user.getPhone())
                // 该电话号码的用户删除后依然可以再次新增
                .eq("status", 1);
        User user1 = userDao.selectOne(queryWrapper);
        if (user1 != null) {
            return result.error("手机号已存在");
        }
        user.setCreateTime(new Date());
        user.setFounderId(founderId);
        userDao.insert(user);
        // 新增用户对应的角色
        bindingRole(user);
        // 绑定用户和图片
        if (user.getPicId() != null) {
            projectPicService.associatePictureAndProject(user.getPicId(), user.getId());
        }
        return result.success("新增成功");
    }

    /**
     * 绑定用户对应的角色
     *
     * @param user
     */
    private void bindingRole(User user) {
        List<Integer> roleIds = user.getRoleIds();
        for (Integer roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoleService.save(userRole);
        }
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        // 更改用户状态，将其设置为无效
        userDao.updateStatus(id);
        // 删除用户后，将其关联的图片删除
        LambdaQueryWrapper<ProjectPic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectPic::getUserId, id);
        ProjectPic projectPic = projectPicService.getOne(queryWrapper);
        if (projectPic != null) {
            projectPicService.deletePicById(projectPic.getId(), request);
        }
        return result.success("删除成功");
    }

    @Override
    public Result get(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id", id)
                .eq("status", 1);
        User user = userDao.selectOne(queryWrapper);
        UaUserVO userVO = new UaUserVO();
        if (user.getAreaId() != null) {
            UaRespLocationAreaVO areaVO = userDao.selectAreaById(user.getAreaId());
            userVO.setAreaName(areaVO.getName());
        }
        BeanUtils.copyProperties(user, userVO);
        // 用户的角色信息
        List<Role> roleList = roleDao.selectByUserId(id);
        userVO.setRoles(roleList);
        // 项目图片信息
        DlmRespProjectPicVO projectPicRespVO = projectPicService.getProjectPicRespVO(id);
        userVO.setProjectPicVO(projectPicRespVO);
        return result.success(userVO);
    }

    @Override
    public Result selectNameByUserIds(String ids) {
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);
        List<User> userList = null;
        if (idList != null && idList.size() > 0) {
            userList = userDao.selectNameByUserIds(idList);
        }
        Result<List<User>> result = new Result<List<User>>();
        return result.success(userList);
    }

    @Override
    public Result modify(User user, HttpServletRequest request) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", user.getPhone())
                // 该电话号码的用户删除后依然可以再次修改该号码
                .eq("status", 1)
                .ne("id", user.getId());
        User u = userDao.selectOne(wrapper);
        if (u != null) {
            return result.error("编辑失败,手机号已存在");
        }
        user.setUpdateTime(new Date());
        userDao.updateById(user);
        // 删除用户之前绑定的角色
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        userRoleService.remove(queryWrapper);
        // 绑定用户对应的角色
        bindingRole(user);
        // 绑定用户和图片
        if (user.getPicId() != null) {
            projectPicService.associatePictureAndProject(user.getPicId(), user.getId());
        }
        return result.success("编辑成功");
    }

    @Override
    public Result getPages(HttpServletRequest request, UserQueryObject qo) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        boolean isAdmin = this.isAdmin(userId);
        // 不是超级管理员的用户只能看到自己创建的用户列表
        if (!isAdmin) {
            qo.setFounderId(userId);
        }
        Page<UaUserVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<UaUserVO> list = userDao.getPageList(page, qo.getName(), qo.getPhone(), qo.getRoleId(), qo.getFounderId());
        return result.success(list);
    }

    @Override
    public Result logout(User user) {
        User user1 = userDao.selectById(user.getId());
        // 更改在线状态
        user1.setOnline(1);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", user1.getId());
        userDao.update(user1, queryWrapper);
        return result.success("退出成功");
    }

    @Override
    public Result checkPassword(HttpServletRequest request, String oldPassword) {
        System.out.println(oldPassword);
        JSONObject jsonObject = JSONObject.parseObject(oldPassword);
        String oldPassword1 = jsonObject.getString("oldPassword");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userDao.selectById(userId);
        if (!user.getPassword().equals(oldPassword1)) {
            return result.error("您输入的原密码有误，请重新输入");
        }
        return result.success("验证成功");
    }

    @Override
    public Result modifyPassword(HttpServletRequest request, String password) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        boolean isAdmin = this.isAdmin(userId);
        JSONObject jsonObject = JSONObject.parseObject(password);
        String password1 = jsonObject.getString("password");
        User user = userDao.selectById(userId);
        if (isAdmin) {
            return result.error("超级管理员密码不能修改");
        }
        if (user.getPassword().equals(password1)) {
            return result.error("新密码不能与旧密码相同");
        }
        user.setPassword(password1);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        userDao.update(user, queryWrapper);
        return result.success("密码修改成功");
    }

    @Override
    public Result uniqueness(Integer id, String accountName) {
        Integer isUniqueness = 0;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_name", accountName);
        User user = userDao.selectOne(queryWrapper);
        if (id != null) {
            if (user != null && !user.getId().equals(id)) {
                isUniqueness = 1;
            }
        } else if (user != null) {
            isUniqueness = 1;
        }
        return result.success(isUniqueness);
    }

    @Override
    public Result areaUserList(HttpServletRequest request) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User u = userDao.selectById(userId);
        boolean flag = this.isAdmin(userId);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (!flag) {
            wrapper.eq(User::getAreaId, u.getAreaId());
        }
        // 根据用户的身份查询分区下的用户信息，超管查询所有，其他人员查询当前用户所在分区下的所有用户
        List<User> userList = userDao.selectList(wrapper);
        List<Integer> areaIdList = userList.stream().map(User::getAreaId).distinct().collect(Collectors.toList());
        List<UaRespLocationAreaVO> locationAreaVOList = userDao.selectAreaByAreaIdList(areaIdList);
        for (UaRespLocationAreaVO uaRespLocationAreaVO : locationAreaVOList) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("area_id", uaRespLocationAreaVO.getId())
                    .ne("status", 2)
                    .ne("type", 1);
            uaRespLocationAreaVO.setDistinctId("area" + uaRespLocationAreaVO.getId());
            List<UaRespSimpleUserVO> userVOList = new ArrayList<>();
            List<User> list = userDao.selectList(queryWrapper);
            for (User user : list) {
                List<Role> roleList = roleDao.selectByUserId(user.getId());
                UaRespSimpleUserVO userVO = new UaRespSimpleUserVO();
                userVO.setId(user.getId());
                userVO.setName(user.getName());
                userVO.setDistinctId("user" + user.getId());
                userVO.setRoleId(roleList.get(0).getId());
                userVO.setRoleName(roleList.get(0).getName());
                userVOList.add(userVO);
            }
            uaRespLocationAreaVO.setUserList(userVOList);
        }
        Result<List<UaRespLocationAreaVO>> result = new Result<List<UaRespLocationAreaVO>>();
        return result.success(locationAreaVOList);
    }

    @Override
    public Result getUserPullList(HttpServletRequest request) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("founder_id", userId)
                .eq("status", 1);
        List<User> userList = userDao.selectList(queryWrapper);
        Result<List<User>> result = new Result<List<User>>();
        return result.success(userList);
    }

    @Override
    public Result updateUserByAreaId(Integer areaId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAreaId, areaId);
        List<User> userList = userDao.selectList(wrapper);
        List<Integer> userIdList = userList.stream().map(User::getId).collect(Collectors.toList());
        userDao.updateBatchUserAreaId(userIdList);
        Result result = new Result();
        return result.success("修改成功");
    }

    @Override
    public Result getUserId(Integer id) {
        Integer userId = recursiveByUserId(id);
        return result.success(userId);
    }

    @Override
    public Result KickOff(Integer id) {
        removeToken(id);
        User user = userDao.selectById(id);
        user.setOnline(1);
        userDao.updateById(user);
        Result result = new Result();
        return result.success("踢下线成功");
    }

    @Override
    public Result forbidden(Integer id, Integer forbidden) {
        User user = userDao.selectById(id);
        if (user.getOnline() == 0) {
            // 如果用户在线，需要移除当前token
            removeToken(id);
        }
        // 改为离线状态
        user.setOnline(1);
        // 修改禁用状态
        user.setForbidden(forbidden);
        userDao.updateById(user);
        Result result = new Result();
        if (forbidden == 1) {
            return result.success("启用成功");
        }
        return result.success("禁用成功");
    }

    @Override
    public List<User> selectByRoleId(Integer roleId) {
        List<User> userList = userDao.selectRoleId(roleId);
        return userList;
    }

    /**
     * 移除Redis中的token
     *
     * @param id
     */
    private void removeToken(Integer id) {
        List tokenList = redisUtil.lGet(Constans.ACCESS_TOKEN + id, 0, -1);
        for (Object o : tokenList) {
            redisUtil.lRemove(Constans.ACCESS_TOKEN + id, tokenList.size(), o);
        }
    }

    /**
     * 根据用户id查询创建人id，当创建人id和超管的id相等时返回当前用户的id
     *
     * @param id
     * @return
     */
    private Integer recursiveByUserId(Integer id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 查询超级管理员信息
        wrapper.eq(User::getType, 1);
        User user = userDao.selectOne(wrapper);
        Integer userId = id;
        Integer founderId = userDao.getUserId(id);
        if (!founderId.equals(user.getId())) {
            userId = recursiveByUserId(founderId);
        }
        return userId;
    }

}