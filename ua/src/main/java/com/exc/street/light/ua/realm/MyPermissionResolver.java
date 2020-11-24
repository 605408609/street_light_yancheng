package com.exc.street.light.ua.realm;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;

/**
 * @author LinShiWen
 * @date 2020/1/7
 */
public class MyPermissionResolver implements PermissionResolver {
    @Override
    public Permission resolvePermission(String permissionString) {
        return new MyPermission(permissionString);
    }
}