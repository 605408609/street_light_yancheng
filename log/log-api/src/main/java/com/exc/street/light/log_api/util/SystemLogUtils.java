package com.exc.street.light.log_api.util;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.exc.street.light.resource.entity.log.LogException;
import com.exc.street.light.resource.entity.log.LogNormal;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * @Author: XuJiaHao
 * @Description: 日志工具类
 * @Date: Created in 23:29 2020/5/7
 * @Modified:
 */
@Slf4j
@UtilityClass
public class SystemLogUtils {

    /**
     * 正常
     *
     * @param modul
     * @param desc
     * @param type
     * @param respObj
     * @return
     */
    public LogNormal getSysNormalLog(String params,String modul ,String type, String desc, String respObj) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        log.info("param:{},type:{},method:{},reason:{},modul:{}",params,HttpUtil.toParams(request.getParameterMap()),type,request.getMethod(),modul);
        LogNormal logNormal = new LogNormal();
        logNormal.setRequParam(params);
        logNormal.setLogType(type);
        logNormal.setRespParam(respObj);
        logNormal.setMethod(request.getMethod());
        logNormal.setModul(modul);
        logNormal.setUri(URLUtil.getPath(request.getRequestURI()));
        logNormal.setRequIp(IpUtil.getRemoteIp(request));
        logNormal.setDescription(desc);
        logNormal.setCreateTime(new Date());
//        logNormal.setUserName(Objects.requireNonNull(getUsername(request)));
        logNormal.setUserId(Objects.requireNonNull(getUserId(request)));
        return logNormal;
    }

    /**
     *  异常
     * @param modul
     * @param desc
     * @param type
     * @return
     */
    public LogException getSysExceptionLog(String params,String modul ,String type ,String desc, String reason) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        log.info("param:{},type:{},method:{},reason:{},modul:{}",HttpUtil.toParams(request.getParameterMap()),type,request.getMethod(),reason,modul);
        LogException logException = new LogException();
        logException.setRequParam(params);
        logException.setLogType(type);
        logException.setMethod(request.getMethod());
        logException.setReason(reason);
        logException.setModul(modul);
        logException.setRequIp(IpUtil.getRemoteIp(request));
        logException.setUri(URLUtil.getPath(request.getRequestURI()));
        logException.setDescription(desc);
        logException.setCreateTime(new Date());
//        logException.setUserName(Objects.requireNonNull(getUsername(request)));
        logException.setUserId(Objects.requireNonNull(getUserId(request)));
        return logException;
    }

    /**
     * 获取用户id
     *
     * @return clientId
     */
    private int getUserId( HttpServletRequest request) {
        String  token =  request.getHeader("token");
        int userId = JavaWebTokenUtil.parserJavaWebToken(token);
        return userId;
    }

    /**
     * 获取用户名称
     *
     * @return username
     */
    private String getUsername( HttpServletRequest request) {
        String  token = request.getHeader("token");
        log.info("获取的token为:{}",token);
        int userId = JavaWebTokenUtil.parserJavaWebToken(token);
        log.info("获取到的用户id为:{}",userId);
        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        log.info("获取到的信息为:{}",userName);
        return userName;
    }
}
