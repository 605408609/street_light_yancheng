package com.exc.street.light.ua.realm;

import com.exc.street.light.resource.entity.ua.Permission;
import com.exc.street.light.resource.entity.ua.Role;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.ua.service.PermissionService;
import com.exc.street.light.ua.service.RoleService;
import com.exc.street.light.ua.service.UserService;
import com.exc.street.light.ua.token.JwtToken;
import com.exc.street.light.ua.util.Constans;
import com.exc.street.light.ua.util.RedisUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义realm
 *
 * @author Linshiwen
 * @date 2018/10/25
 */
@Service
public class MyRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    @Lazy
    private RoleService roleService;
    @Autowired
    @Lazy
    private PermissionService permissionService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权(接口保护，验证接口调用权限时调用)
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Integer userId = JavaWebTokenUtil.parserJavaWebToken(principals.toString());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 判断用户是否是超级管理员
        boolean isAdmin = userService.isAdmin(userId);
        // 用户权限列表，根据用户拥有的权限标识与如@permission标注的接口对比，决定是否可以调用接口
        Set<String> permission = new HashSet<>();
        if (isAdmin) {
            //超级管理员拥有所有权限
            List<Permission> permissions = permissionService.list();
            for (Permission permission1 : permissions) {
                permission.add(permission1.getCode());
            }
        } else {
            List<Role> roles = roleService.findByUserId(userId);
            for (Role role : roles) {
                simpleAuthorizationInfo.addRole(role.getName());
                List<Permission> permissions = permissionService.findByRoleId(role.getId());
                for (Permission permission1 : permissions) {
                    permission.add(permission1.getCode());
                }
            }
        }
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     *
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得用户id，用于和数据库进行对比
        Integer userId = JavaWebTokenUtil.parserJavaWebToken(token);
        if (userId == null) {
            throw new AuthenticationException("token invalid");
        }

        User user = userService.getById(userId);
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        // 从Redis中获取该用户的token
        List list = redisUtil.lGet(Constans.ACCESS_TOKEN + userId, 0, -1);
        if (list.size() == 0) {
            // 被踢下线的用户的缓存中没有token，直接抛异常
            throw new AuthenticationException("token invalid");
        } else {
            if (!list.contains(token)) {
                throw new AuthenticationException("token invalid");
            }
        }
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}

