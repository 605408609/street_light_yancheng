package com.exc.street.light.security.base.service;


/**
 * 用户代理服务
 *
 * @author Linshiwen
 * @date 2018/12/11
 */
public interface UserProxyService {

    /**
     * 登录认证
     *
     * @param token
     * @return
     */
    public boolean loginAuthe(String token);

    /**
     * 授权
     *
     * @param token
     * @param code
     * @return
     */
    public boolean userAuthc(String token, String code);
}
