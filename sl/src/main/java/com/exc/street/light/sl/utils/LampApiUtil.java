package com.exc.street.light.sl.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.sl.config.parameter.SlLampParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口调用工具类
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Component
public class LampApiUtil {
    private static final Logger logger = LoggerFactory.getLogger(LampApiUtil.class);
    @Autowired
    private SlLampParameter slLampParameter;

    public Result getToken() {
        Result result = new Result();
        Map<String, String> map = new HashMap<>(2);
        map.put("username", slLampParameter.getUsername());
        map.put("pswd", slLampParameter.getPassword());
        Map<String, String> contentMap = new HashMap<>(1);
//        contentMap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        contentMap.put("Content-Type", "application/json;charset=UTF-8");
        logger.info("登录信息:map:{}", map);
//        HttpUtil httpUtil = new HttpUtil();
//        String json = httpUtil.doPostJson(slLampParameter.getAddress() + slLampParameter.getLoginUrl(), (JSONObject) JSON.toJSON(map), null);
        String json = HttpUtil.post(slLampParameter.getAddress() + slLampParameter.getLoginUrl(), JSON.toJSONString(map), contentMap);
        logger.info("登录接口返回信息:{}", json);
        if (json == null) {
            return result.error("第三方接口调用超时");
        }
        JSONObject jsonObject = JSON.parseObject(json);
        int slResult = jsonObject.getIntValue("result");
        if (slResult == 1) {
            return result.error("登录请求参数有误");
        }
        Object data = jsonObject.get("data");
        JSONObject jsonObject1 = JSON.parseObject(data.toString());
        String token = jsonObject1.getString("token");
        Result successResult = result.success("");
        successResult.setMessage(token);
        return successResult;
    }
}