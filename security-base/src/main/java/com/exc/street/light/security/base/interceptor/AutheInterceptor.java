package com.exc.street.light.security.base.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.security.base.service.UserProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登陆认证拦截器
 *
 * @author Linshiwen
 * @date 2018/12/11
 */
public class AutheInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AuthcInterceptor.class);

    @Autowired
    private UserProxyService userProxyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("ip:{}",request.getRemoteAddr());
        logger.info("port:{}",request.getRemotePort());
        String uri = request.getRequestURI();
        logger.info("authe interceptor uri:{}", uri);
        //跨域放行
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        // 认证
        boolean result = false;
        try {
            result = userProxyService.loginAuthe(request.getHeader("token"));
        } catch (Exception e) {
            logger.error("authe interceptor exception", e);
        }

        if (!result) {
            response.setContentType("application/json; charset=utf-8");
            response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
            PrintWriter out = response.getWriter();
            try {
                JSONObject message = new JSONObject();
                message.put("code", 401);
                message.put("message", "请进行登录认证后再访问");
                out.write(message.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                out.close();
            }
        }

        return result;
    }
}
