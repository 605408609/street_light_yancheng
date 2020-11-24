package com.exc.street.light.em.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * java web token
 *
 * @author Linshiwen
 * @date 2018/6/23
 */
public class JwtToken implements AuthenticationToken {

    /**
     * 秘钥
     */
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
