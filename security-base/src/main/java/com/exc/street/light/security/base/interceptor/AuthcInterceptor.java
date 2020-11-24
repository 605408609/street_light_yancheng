package com.exc.street.light.security.base.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.security.base.service.UserProxyService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限验证拦截器
 *
 * @author Linshiwen
 * @date 2018/12/11
 */
public class AuthcInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AuthcInterceptor.class);

    @Autowired
    private UserProxyService userProxyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        //跨域放行
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequiresPermissions requiresPermissions = handlerMethod.getMethodAnnotation(RequiresPermissions.class);

        /*String authcCode = null;
        if (requiresPermissions != null) {
            authcCode = requiresPermissions.value()[0];
        }
        logger.info("authc interceptor uri:{}", uri);

        // 鉴权
        boolean result = false;

        if (!StringUtils.isEmpty(authcCode)) {
            try {
                result = userProxyService.userAuthc(request.getHeader("token"), authcCode);
            } catch (Exception e) {
                logger.error("authc interceptor exception", e);
            }
        }*/

        // 鉴权
        boolean result = false;
        String[] authcCode = null;
        Logical logical = null;
        if (requiresPermissions != null) {
            authcCode = requiresPermissions.value();
            logical = requiresPermissions.logical();
        }
        logger.info("authc interceptor uri:{}", uri);
        // logical有两种类型，默认是AND类型，考虑到
        if ((Logical.AND).equals(logical)) {
            List<Boolean> flagList = new ArrayList<>();
            for (String code : authcCode) {
                if (!StringUtils.isEmpty(code)) {
                    try {
                        result = userProxyService.userAuthc(request.getHeader("token"), code);
                        flagList.add(result);
                        for (Boolean b : flagList) {
                            if (b.equals(false)) {
                                result = false;
                            }
                        }
                    } catch (Exception e) {
                        logger.error("authc interceptor exception", e);
                    }
                }
            }
        }
        if ((Logical.OR).equals(logical)) {
            List<Boolean> flagList = new ArrayList<>();
            for (String code : authcCode) {
                if (!StringUtils.isEmpty(code)) {
                    try {
                        result = userProxyService.userAuthc(request.getHeader("token"), code);
                        flagList.add(result);
                        for (Boolean b : flagList) {
                            if (b.equals(true)) {
                                result = true;
                            }
                        }
                    } catch (Exception e) {
                        logger.error("authc interceptor exception", e);
                    }
                }
            }

        }

        if (!result) {
            response.setContentType("application/json; charset=utf-8");
            response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
            PrintWriter out = response.getWriter();
            try {
                JSONObject message = new JSONObject();
                message.put("code", 403);
                message.put("message", "您没有相应的访问权限");
                out.write(message.toJSONString());
            } catch (Exception e) {
            } finally {
                out.close();
            }
        }

        return result;
    }
}
