package com.exc.street.light.ua.realm;

import org.apache.shiro.authz.Permission;

/**
 * @author LinShiWen
 * @date 2020/1/7
 */
public class MyPermission implements Permission {
    private String permission;

    public MyPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean implies(Permission p) {
        if (!(p instanceof MyPermission)) {
            return false;
        }
        MyPermission mp = (MyPermission) p;
        if (!this.permission.equals(mp.permission)) {
            return false;
        }
        return true;
    }
}