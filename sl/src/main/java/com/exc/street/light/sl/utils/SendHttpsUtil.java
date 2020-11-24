package com.exc.street.light.sl.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampStrategyAction;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.resource.utils.HttpsUtil;
import com.exc.street.light.sl.VO.*;
import com.exc.street.light.sl.config.parameter.HttpsControlApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SendHttpsUtil {

    private static String token;

    private static HttpsControlApi httpsControlApi;

    @Autowired
    public void singleLampService(HttpsControlApi httpsControlApi){
        SendHttpsUtil.httpsControlApi = httpsControlApi;
    }

    public static void main(String[] args) {
        String message = MessageGeneration.control("00000001","04","00",50);
        List<String> deviceIdList = new ArrayList<>();
        deviceIdList.add("119c3da7-388a-49f0-ad78-99354aa7da03");
        deviceIdList.add("56a02e44-10d2-4ca9-a48b-ecade5675963");

        IssueResponse issue = batchIssue(message, deviceIdList);
        System.out.println(issue);

        //issueSelect("56a02e44-10d2-4ca9-a48b-ecade5675963");


    }

    /**
     * 发送https请求，请求参数为json字符串格式
     * @param url
     * @param params
     * @param headerParameters
     * @return
     */
    public static Result<JSONObject> sendHttpsJson(String url, String params, Map<String, String> headerParameters, String method){
        JSONObject result = new JSONObject();

        String selfcertpwd = httpsControlApi.getSelfcertpwd();
        String trustcapwd = httpsControlApi.getTrustcapwd();

        String testPath = httpsControlApi.getTestPath();
        String selfcertpath = testPath + httpsControlApi.getSelfcertpath();
        String trustcapath = testPath + httpsControlApi.getTrustcapath();
        try {
            // 调用 get 请求
            String res = HttpsUtil.httpsSendJson(url, headerParameters, params,method,selfcertpwd,trustcapwd,selfcertpath,trustcapath);
            //打印返回参数
            res = res.substring(res.indexOf("{"));//截取
            result = JSONObject.parseObject(res);//转JSON
            //System.out.println(result.toString());//打印
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result().success(result);
    }

    /**
     * 发送https请求，请求参数为字符串拼接格式
     * @param url
     * @param params
     * @param headerParameters
     * @return
     */
    public static Result<JSONObject> sendHttpsForm(String url, Map<String, String> params, Map<String, String> headerParameters, String method){
        String selfcertpwd = httpsControlApi.getSelfcertpwd();
        String trustcapwd = httpsControlApi.getTrustcapwd();

        String testPath = httpsControlApi.getTestPath();
        String selfcertpath = testPath + httpsControlApi.getSelfcertpath();
        String trustcapath = testPath + httpsControlApi.getTrustcapath();
        JSONObject result = new JSONObject();
        try {
            // 调用 get 请求
            String res = HttpsUtil.httpsSendForm(url, headerParameters, params,method,selfcertpwd,trustcapwd,selfcertpath,trustcapath);
            //打印返回参数
            res = res.substring(res.indexOf("{"));//截取
            result = JSONObject.parseObject(res);//转JSON
            //System.out.println(result.toString());//打印
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result().success(result);
    }

    /**
     * 获取token
     * @return
     */
    public static String getToken() {
        String accessToken = "";
        try {
            //获取token   https://device.api.ct10649.com:8743/iocm/app/sec/v1.1.0/login
            String address = httpsControlApi.getUrl() + httpsControlApi.getToken();
            //请求参数
            Map<String, String> params = new HashMap<String, String>();
            Map<String, String> headerParameters = new HashMap<String, String>();
            //参数
            params.put("appId", httpsControlApi.getAppKey());
            params.put("secret", httpsControlApi.getSecret());
            Result result = sendHttpsForm(address, params, headerParameters,"POST");
            JSONObject data = (JSONObject)result.getData();
            if(!data.isEmpty()){
                accessToken = (String)data.get("accessToken");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        token = accessToken;
        return accessToken;
    }

    /**
     * 查询设备信息
     * @param deviceId
     * @return
     */
    public static List<SingleLampParamVO> select(String deviceId) {
        if(token.isEmpty()){
            getToken();
        }
        List<SingleLampParamVO> singleLampParamVOList = new ArrayList<>();
        SingleLampParamVO singleLampParamVO = null;
        try {
            //查询单个设备信息  https://device.api.ct10649.com:8743/iocm/app/dm/v1.4.0/devices
            String address = httpsControlApi.getUrl() + httpsControlApi.getSelect() + "/" + deviceId;
            //请求参数
            Map<String, String> headerParameters = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            //请求头
            headerParameters.put("app_key",httpsControlApi.getAppKey());
            headerParameters.put("Authorization","Bearer "+token);
            //参数
            params.put("select", "imsi");
            Result result = sendHttpsForm(address, params, headerParameters,"GET");
            JSONObject data = (JSONObject)result.getData();
            //如果设备id为空，则表示查询全部
            if(deviceId.isEmpty()){
                JSONArray jsonArray = (JSONArray)data.get("devices");
                singleLampParamVOList = JSON.parseArray(jsonArray.toJSONString(), SingleLampParamVO.class);
            }else {
                singleLampParamVO = JSON.parseObject(data.toJSONString(), SingleLampParamVO.class);
                singleLampParamVOList.add(singleLampParamVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return singleLampParamVOList;
    }

    /**
     * 下发命令
     * @param message
     * @param deviceId
     * @return
     */
    public static IssueResponse issue(String message, String deviceId) {
        if(token.isEmpty()){
            getToken();
        }
        IssueResponse issueResponse = new IssueResponse();
        try {
            //下发指令  https://device.api.ct10649.com:8743/iocm/app/cmd/v1.4.0/deviceCommands
            String address = httpsControlApi.getUrl() + httpsControlApi.getIssue();
            //请求参数
            Map<String, String> headerParameters = new HashMap<>();
            JSONObject params = new JSONObject();
            //请求头
            headerParameters.put("app_key",httpsControlApi.getAppKey());
            headerParameters.put("Authorization","Bearer "+token);
            //参数
            CommandDTO commandDTO = new CommandDTO();
            commandDTO.setServiceId("Control");
            commandDTO.setMethod("DOWNLINK");
            ObjectNode objectNode = new ObjectNode();
            objectNode.setData(message);
            commandDTO.setParas(objectNode);

            params.put("command", commandDTO);
            params.put("deviceId", deviceId);
            params.put("expireTime",0);
            params.put("callbackUrl",httpsControlApi.getCallbackUrl());

            Result result = sendHttpsJson(address, params.toJSONString(), headerParameters,"POST");
            JSONObject data = (JSONObject)result.getData();
            if(!data.isEmpty()){
                issueResponse = JSON.parseObject(data.toJSONString(), IssueResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return issueResponse;
    }

    /**
     * 下发策略指令
     * @param lampDeviceNumList
     * @param lampStrategyActionList
     * @return
     */
    public static Result setStrategy(List<String> lampDeviceNumList, List<LampStrategyAction> lampStrategyActionList) {
        Result result = new Result();
//        for (String num : lampDeviceNumList) {
//            //创建场景集合
//            if(lampStrategyActionList.size()>3){
//                return new Result().error("策略个数已超出（3）");
//            }
//            List<Integer> sceneList = new ArrayList<>();
//            Map<Integer,LampStrategyAction> immediately = new HashMap<>();
//            for (int i = 0; i < lampStrategyActionList.size(); i++){
//                LampStrategyAction lampStrategyAction = lampStrategyActionList.get(i);
//                Integer executionMode = lampStrategyAction.getExecutionMode();
//                if (executionMode == 1) {
//                    //每天,将定时执行分出来
//                    SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd");
//
//                    //获取参数
//                    String startDate = lampStrategyAction.getStartDate();
//                    String endDate = lampStrategyAction.getEndDate();
//                    String executionTime = lampStrategyAction.getExecutionTime();
//
//                    Integer type = lampStrategyAction.getType();
//                    Integer brightness = lampStrategyAction.getBrightness();
//
//                    StrategyVO strategyVO = new StrategyVO();
//                    strategyVO.setScene(i+1);
//                    strategyVO.setType(type);
//                    strategyVO.setBrightness(brightness);
//
//                    String format = dateFormatJustDay.format(new Date());
//                    if(format.equals(startDate)){
//                        //当天开始，直接执行
//                        sceneList.add(i+1);
//                        immediately.put(i+1,lampStrategyAction);
//                    }else{
//                        //非当天执行，拼接为当天00：00：00点
//                        String startDateString = startDate + " 00:00:00";
//                        //定时任务执行场景使能
//                        strategyVO.setEnable(1);
//                        strategyVO.setTime(startDate+"T"+executionTime);
//                        scheduledTasks(startDateString,num,strategyVO);
//                    }
//                    //定时任务执行关闭场景
//                    String endDateString = endDate + " 23:55:00";
//                    strategyVO.setEnable(0);
//                    strategyVO.setTime(endDate+"T"+executionTime);
//                    scheduledTasks(endDateString,num,strategyVO);
//                } else if (executionMode == 2) {
//                    //每周，只留下周期任务,暂时不考虑
//                    //sceneList.add(i);
//                    //直接执行
//                    return new Result().error("目前暂不支持按周定时的策略");
//                } else {
//                    System.out.println("执行模式错误");
//                }
//            }
//
//            List<StrategyVO> strategyVOList = new ArrayList<>();
//            for (Integer scene : sceneList) {
//                StrategyVO strategyVO = new StrategyVO();
//                strategyVO.setScene(scene);
//                strategyVO.setEnable(1);
//                LampStrategyAction lampStrategyAction = immediately.get(scene);
//                String startDate = lampStrategyAction.getStartDate()+"T"+lampStrategyAction.getExecutionTime();
//                strategyVO.setTime(startDate);
//                Integer type = lampStrategyAction.getType();
//                strategyVO.setType(type);
//                Integer brightness = lampStrategyAction.getBrightness();
//                strategyVO.setBrightness(brightness);
//                strategyVOList.add(strategyVO);
//            }
//            result = sendStrategy(num, strategyVOList);
//        }
        return result;
    }

    public static void scheduledTasks(String DateString,String addr,StrategyVO strategyVO){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(DateString);
            Timer timer = new Timer(DateString);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    //执行任务，发送场景使能或关闭的消息
                    List<StrategyVO> strategyVOList = new ArrayList<>();
                    strategyVOList.add(strategyVO);
                    sendStrategy(addr,strategyVOList);
                    System.out.println("定时下发策略："+DateString+":"+strategyVO);
                    Date date1 = new Date();
                    if(date1.getTime()+10>date.getTime()){
                        timer.cancel();
                    }
                }
            }, date,20);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成策略报文及发送
     * @param num
     * @param strategyVOList
     * @return
     */
    public static Result sendStrategy(String num, List<StrategyVO> strategyVOList) {
        IssueResponse issue = new IssueResponse();
        Result result = new Result();
        for (StrategyVO strategyVO : strategyVOList) {
            SetStrategyParamVO setStrategyParamVO = new SetStrategyParamVO();
            //执行时间
            String time = strategyVO.getTime();
            String hour = time.substring(11, 13);
            String minute = time.substring(14, 16);
            String second = time.substring(17, 19);
            String executionTime = HexUtil.intToHexStringOne(Integer.parseInt(hour))+ HexUtil.intToHexStringOne(Integer.parseInt(minute)) + HexUtil.intToHexStringOne(Integer.parseInt(second));
            //使能位
            Integer enable = strategyVO.getEnable();
            if(enable==0){
                setStrategyParamVO.setEnable("01");
            }else {
                setStrategyParamVO.setEnable("00");
            }

            String strategyNum = HexUtil.intToHexStringOne(strategyVO.getScene());
            setStrategyParamVO.setStrategyNum(strategyNum);
            setStrategyParamVO.setExecutionTimeOne(executionTime);
            setStrategyParamVO.setExecutionTimeTwo(executionTime);
            //策略一：亮灯状态
            Integer type = strategyVO.getType();
            if(type==0){
                setStrategyParamVO.setExecutionTypeOne("0501");
            }else {
                setStrategyParamVO.setExecutionTypeOne("0500");
            }
            //策略二：亮度
            String brightness = HexUtil.intToHexStringOne(strategyVO.getBrightness());
            setStrategyParamVO.setExecutionTypeTwo("04"+brightness);
            String message = MessageGeneration.setStrategy("00000001", setStrategyParamVO);

            System.out.println(message);
            issue = issue(message,num);
            String status = issue.getStatus();
            if("FAILED".equals(status)){
                result.error("命令下发失败");
            }
            if("TIMEOUT".equals(status)){
                result.error("命令下发已超时");
            }
        }
        return result.success("下发成功");
    }

    /**
     * 策略使能
     * @param lampDeviceNumList
     * @return
     */
    public static Result strategyEnable(List<String> lampDeviceNumList) {
        try {
            for (String num : lampDeviceNumList) {
                String message = MessageGeneration.scene("00000001", "01", "00");
                issue(message,num);
            }
        }catch (Exception e){
            return new Result().error("策略使能操作失败");
        }
        return new Result().success("策略使能操作成功");
    }

    /**
     * 电能查询
     * @param lampDeviceNumList
     * @return
     */
    public static Result electricEnergy(List<String> lampDeviceNumList) {
        try {
            for (String num : lampDeviceNumList) {
                String message = MessageGeneration.fixedInfo("00000001", FrameHeadMatch.POWERQUERY);
                issue(message,num);
            }
        }catch (Exception e){
            return new Result().error("电能查询失败");
        }
        return new Result().success("电能查询成功");
    }




    /**
     * 批量下发命令
     * @param message
     * @param deviceIdList
     * @return
     */
    public static IssueResponse batchIssue(String message, List<String> deviceIdList) {
        if(token.isEmpty()){
            getToken();
        }
        IssueResponse issueResponse = new IssueResponse();
        try {
            //下发指令  https://device.api.ct10649.com:8743/iocm/app/batchtask/v1.1.0/tasks
            String address = httpsControlApi.getUrl() + httpsControlApi.getBatchIssue();
            //请求参数
            Map<String, String> headerParameters = new HashMap<>();
            JSONObject params = new JSONObject();
            //请求头
            headerParameters.put("app_key","BltfJzVu_J9p5c2xskLgDHb5ys4a");
            headerParameters.put("Authorization","Bearer "+ token);
            //参数
            CommandDTO commandDTO = new CommandDTO();
            commandDTO.setServiceId("Control");
            commandDTO.setMethod("DOWNLINK");
            ObjectNode objectNode = new ObjectNode();
            objectNode.setData(message);
            commandDTO.setParas(objectNode);

            BatchIssueRequestVO batchIssueRequestVO = new BatchIssueRequestVO();
            batchIssueRequestVO.setType("DeviceList");
            batchIssueRequestVO.setDeviceList(deviceIdList);
            batchIssueRequestVO.setCommand(commandDTO);


            params.put("appId","BltfJzVu_J9p5c2xskLgDHb5ys4a");
            params.put("param",batchIssueRequestVO);
            params.put("taskName","" + new Date().getTime());
            params.put("taskType","DeviceCmd");
            params.put("timeout",30);


            Result result = sendHttpsJson(address, params.toJSONString(), headerParameters,"POST");
            System.out.println(result);
            /*JSONObject data = (JSONObject)result.getData();
            if(!data.isEmpty()){
                issueResponse = JSON.parseObject(data.toJSONString(), IssueResponse.class);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return issueResponse;
    }


    /**
     * 设备 数据/信息 变化接口订阅
     * @param notifyType
     * @param url
     * @return
     */
    public static void deviceSubscription(String notifyType,String url) {
        if(token.isEmpty()){
            getToken();
        }
        try {
            //https://server:port/iocm/app/sub/v1.2.0/subscriptions
            String address = httpsControlApi.getUrl() + "/iocm/app/sub/v1.2.0/subscriptions";
            //请求参数
            Map<String, String> headerParameters = new HashMap<>();
            JSONObject params = new JSONObject();
            //请求头
            headerParameters.put("app_key","BltfJzVu_J9p5c2xskLgDHb5ys4a");
            headerParameters.put("Authorization","Bearer "+ token);
            //参数
            params.put("appId","BltfJzVu_J9p5c2xskLgDHb5ys4a");
            params.put("notifyType",notifyType);
            params.put("callbackUrl",url);


            Result result = sendHttpsJson(address, params.toJSONString(), headerParameters,"POST");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
