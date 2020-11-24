package com.exc.street.light.dlm.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.dlm.config.parameter.LoraApi;
import com.exc.street.light.dlm.config.parameter.LoraNewApi;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.HttpsUtil;
import com.exc.street.light.resource.vo.sl.LoraCreateNodeParam;
import com.exc.street.light.resource.vo.sl.LoraIssueOrderReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MessageOperationUtil {
    private static final Logger log = LoggerFactory.getLogger(MessageOperationUtil.class);
    private static LoraApi loraApi;

    @Autowired
    public void loraApi(LoraApi loraApi){
        MessageOperationUtil.loraApi = loraApi;
    }

    private static LoraNewApi loraNewApi;

    @Autowired
    public void loraNewApi(LoraNewApi loraNewApi){
        MessageOperationUtil.loraNewApi = loraNewApi;
    }


    /**
     * 创建Lora节点（设备）
     * @param DevName
     * @param DevEUI
     * @return
     */
    public static boolean createNode(String DevName, String DevEUI){

        try {
            //请求头token
            Map<String,String> headers = new HashMap<>();
            headers.put("x-access-token",loraApi.getToken());

            //封装参数并转为Json字符串
            LoraCreateNodeParam loraCreateNodeParam = new LoraCreateNodeParam();
            loraCreateNodeParam.setAppID(Integer.parseInt(loraApi.getAppId()));
            //自动生成
            loraCreateNodeParam.setDevName(DevName);
            //非自动生成
            loraCreateNodeParam.setDevEUI(DevEUI);
            String AppEUI = DevEUI.substring(0, 8)+"00000000";
            loraCreateNodeParam.setAppEUI(AppEUI);
            loraCreateNodeParam.setRegion("CN470");
            loraCreateNodeParam.setSubnet("CH_00-07");
            loraCreateNodeParam.setSupportClassB(false);
            loraCreateNodeParam.setSupportClassC(true);
            loraCreateNodeParam.setAuthType("abp");
            //loraCreateNodeParam.setAppKey("");
            String DevAddr = DevEUI.substring(8, 16);
            loraCreateNodeParam.setDevAddr(DevAddr);
            loraCreateNodeParam.setAppSKey(loraApi.getAppSKey());
            loraCreateNodeParam.setNwkSKey(loraApi.getNwkSKey());
            loraCreateNodeParam.setMacVersion("1.0.2");
            String loraCreateNodeParamString = JSON.toJSONString(loraCreateNodeParam);


            //通过http请求发送消息
            String post = HttpsUtil.httpsSendJson(loraApi.getCreateNode(),headers,loraCreateNodeParamString,"POST");
            post = post.substring(post.indexOf("{"));//截取
            LoraIssueOrderReturn loraIssueOrderReturn = JSON.parseObject(post, LoraIssueOrderReturn.class);
            Double code = loraIssueOrderReturn.getCode();
            System.out.println(post);
            if(code == 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lora多播组增加节点（设备）
     * @param devEUI
     * @return
     */
    public static boolean createNodeMc(String devEUI, String mcEUI){
        try {
            //请求头token
            Map<String,String> headers = new HashMap<>();
            headers.put("x-access-token",loraApi.getToken());

            List<String> devEUIs = new ArrayList<>();
            devEUIs.add(devEUI);
            //封装参数并转为Json字符串
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mcEUI",mcEUI);
            jsonObject.put("devEUIs",devEUIs);
            //通过http请求发送消息
            String post = HttpsUtil.httpsSendJson(loraApi.getCreateNodeMc(),headers,jsonObject.toJSONString(),"POST");
            post = post.substring(post.indexOf("{"));//截取
            LoraIssueOrderReturn loraIssueOrderReturn = JSON.parseObject(post, LoraIssueOrderReturn.class);
            Double code = loraIssueOrderReturn.getCode();
            System.out.println(post);
            if(code == 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String createNewLoraNode(String devEui){

        String getToken = sendNewLoraLogin();
        JSONObject tokenResult = JSON.parseObject(getToken);
        JSONObject tokenData = (JSONObject)tokenResult.get("data");
        String token = (String)tokenData.get("jwt");

        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","Bearer "+token);

        String devAddr = devEui.substring(8,16);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appsKey",loraNewApi.getAppsKey());
        jsonObject.put("clazz","CLASS_C");
        jsonObject.put("devAddr",devAddr);
        jsonObject.put("devEui",devEui);
        jsonObject.put("fcntBits","FCNT_32");
        jsonObject.put("group","");
        jsonObject.put("lorawanVersion","LoRaWAN_1_0_2");
        jsonObject.put("nwksKey",loraNewApi.getNwksKey());
        jsonObject.put("projectId",loraNewApi.getProjectId());

        String post = HttpUtil.post(loraNewApi.getCreateNode(), jsonObject.toJSONString(), headMap);
        System.out.println(post);
        JSONObject result = JSON.parseObject(post);
        Integer code = (Integer)result.get("code");
        if(code==8000){
            JSONObject data = (JSONObject) result.get("data");
            String id = (String) data.get("id");
            return id;
        }else {
            return "";
        }
    }

    public static String sendNewLoraLogin(){
        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        JSONObject jsonObject = new JSONObject();
        /*jsonObject.put("username",loraNewApi.getUsername());
        jsonObject.put("password",loraNewApi.getPassword());
        String post = HttpUtil.post(loraNewApi.getLogin(), jsonObject.toJSONString(),headMap);*/
        jsonObject.put("username","dvk018");
        jsonObject.put("password","888888");
        String post = HttpUtil.post("http://47.110.127.110:8080/api/v1/auth/login", jsonObject.toJSONString(),headMap);
        return post;
    }

}
