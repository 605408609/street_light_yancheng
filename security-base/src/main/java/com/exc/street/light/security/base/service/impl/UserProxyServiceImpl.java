package com.exc.street.light.security.base.service.impl;

import com.exc.street.light.security.base.service.UserProxyService;
import com.exc.street.light.security.base.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserProxyServiceImpl implements UserProxyService {

    private static final Logger logger = LoggerFactory.getLogger(UserProxyServiceImpl.class);

    /**
     * 白洞认证地址
     */
    @Value("${security.authe.url}")
    private String AUTHE_URL;

    /**
     * 白洞授权地址
     */
    @Value("${security.authc.url}")
    private String ATUHC_URL;

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Override
    public boolean loginAuthe(String token) {
        String result = httpClientUtil.doGet(AUTHE_URL, null, token);

        logger.info("loginAuthe url:{},result:{}", AUTHE_URL, result);

        if (StringUtils.isEmpty(result)) {
            return false;
        }

        return Boolean.valueOf(result);
    }

    @Override
    public boolean userAuthc(String token, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        String result = httpClientUtil.doGet(ATUHC_URL, map, token);

        logger.info("userAuthc url:{},result:{}", ATUHC_URL, result);

        if (StringUtils.isEmpty(result)) {
            return false;
        }

        return Boolean.valueOf(result);
    }
}
