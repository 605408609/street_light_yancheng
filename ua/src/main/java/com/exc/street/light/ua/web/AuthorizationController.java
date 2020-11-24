package com.exc.street.light.ua.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.Role;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.ua.service.RoleService;
import com.exc.street.light.ua.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 认证授权控制器
 *
 * @author Linshiwen
 * @date 2019/1/10
 */
@RestController
@RequestMapping("/api/ua")
public class AuthorizationController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 认证
     *
     * @return
     */
    @RequestMapping(value = "/authe", method = RequestMethod.GET)
    public boolean isAuthenticated() {
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated();
    }

    /**
     * 授权
     *
     * @param code    权限编码
     * @param request
     * @return
     */
    @RequestMapping(value = "/authc", method = RequestMethod.GET)
    public boolean authorization(String code, HttpServletRequest request) {
        //判断用户是否是超级管理员,是则放行
        String token = request.getHeader("token");
        Integer userId = JavaWebTokenUtil.parserJavaWebToken(token);
        User user = userService.getById(userId);
        if (user == null) {
            return false;
        }
        List<Role> roles = roleService.findByUserId(userId);
        for (Role role : roles) {
            if (role.getType().equals(1)) {
                return true;
            }
        }
        Subject subject = SecurityUtils.getSubject();
        boolean permitted = subject.isPermitted(code);
        return permitted;
    }

    /**
     * 401
     *
     * @return
     */
    @RequestMapping("/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result unauthorized() {
        return new Result(401, null, "Unauthorized", null);
    }

    /**
     * 检查token是否过期
     *
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Result check(String token) {
        Integer webToken = JavaWebTokenUtil.parserJavaWebToken(token);
        if (webToken == null) {
            return new Result(10086, null, "token已过期", null);
        } else {
            return new Result();
        }
    }
}
