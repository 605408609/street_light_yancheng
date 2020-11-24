package com.exc.street.light.em.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.utils.HttpUtil;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 气象数据解析类
 * @Author: Xiaok
 * @Date: 2020/10/20 15:03
 */
public class AnalyzeUtil {

    /**
     * 根据数据获取mac地址
     * @param content
     * @return
     */
    public static String getMac(String content){
        String[] dataArr = content.split(",");
        for (String data : dataArr) {
            String[] dataArr2 = data.split("=");
            if(dataArr2[0].equals("MAC")){
                return dataArr2[1];
            }
        }
        return null;
    }

    @Autowired
    private HttpConnectionPools httpConnectionPools;

    public static void main(String[] args) {
        Float f = 1.21f;
        List<String> numList = new ArrayList<>();
        numList.add("y60-720-30497");
        String prototype = "温度："+f+",湿度："+f+",气压："+f+",质量："+f;
        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isAll",0);
        jsonObject.put("num",-1);
        jsonObject.put("align","top");
        jsonObject.put("direction","left");
        jsonObject.put("html","<i style='background:#000;color:#FFF'>" + prototype + "</i>");
        jsonObject.put("prototype", prototype);
        jsonObject.put("interval",50);
        jsonObject.put("step",1);
        jsonObject.put("numList",numList);
        System.out.println("11111111发送到显示屏数据：{}"+jsonObject.toString());

        String s = jsonObject.toJSONString();
//        Properties initProp = new Properties(System.getProperties());
//        System.out.println("当前系统编码:" + initProp.getProperty("file.encoding"));
//        System.out.println("当前系统语言:" + initProp.getProperty("user.language"));
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        System.out.println("11111111发送到显示屏数据：{}"+jsonObject.toString());
//        JSONObject registerResult = JSON.parseObject(HttpUtil.post("http://192.168.111.110:60024" + "/api/ir/screen/subtitle/send", s, headMap));
//        System.out.println("2222222222显示屏返回：{}"+registerResult.toString());



        String url = "http://192.168.111.110:60024" + "/api/ir/screen/subtitle/send";

        BasicHeader basicHeader = new BasicHeader("Content-Type","application/json;charset=UTF-8");
        HttpConnectionPools httpConnectionPools = new HttpConnectionPools();
        String jsonResult = httpConnectionPools.post(url, jsonObject, basicHeader);
        System.out.println("2222222222显示屏返回：{}"+jsonResult);
    }
}
