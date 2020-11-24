package com.exc.street.light.sl.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.resource.vo.SingleLampParamTempVO;
import com.exc.street.light.resource.vo.sl.LoraReport;
import com.exc.street.light.resource.vo.sl.LoraReportData;
import com.exc.street.light.resource.vo.sl.SingleLampParamRespVO;
import com.exc.street.light.sl.VO.LoraResponse;
import com.exc.street.light.sl.service.SingleLampParamService;
import com.exc.street.light.sl.service.SystemDeviceService;
import com.exc.street.light.sl.utils.CTWingApi;
import com.exc.street.light.sl.utils.MessageGeneration;
import com.exc.street.light.sl.utils.MessageOperationUtil;
import com.exc.street.light.sl.utils.PushDataUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "灯具", value = "灯具")
@RestController
@RequestMapping("/api/sl/lamp/single")
public class SingleLampParamController {

    @Autowired
    SingleLampParamService singleLampParamService;

    @Autowired
    SystemDeviceService systemDeviceService;

    /**
     * @explain 查询所有灯具
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/all")
    @ApiOperation(value = "查询所有灯具", notes = "查询所有灯具,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    public Result<List<SingleLampParamRespVO>> getList(@RequestParam(required = false) String deviceName, HttpServletRequest request) {
        return singleLampParamService.getList(deviceName, request);
    }

    @PostMapping("/lora/json")
    public void json(@RequestBody String json){
        System.out.println("返回json：" + json);
    }



    @PostMapping("/lora/uplink")
    public void uplink(@RequestBody String json){
        //System.out.println("接收信息时间：" + new Date());
        try {
            //封装传递的Json字符串
            LoraReport loraReport = JSON.parseObject(json, LoraReport.class);
            //获取报文
            LoraReportData data = loraReport.getData();
            String messageBase64 = data.getData();
            Base64 base64 = new Base64();
            byte[] decode = base64.decode(messageBase64);
            String message = HexUtil.bytesTohex(decode).replace(" ", "");

            //写出报文
            /*FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream("C:/Users/Gigabyte/Desktop/Lora上报信息.txt",true);
                fileOutputStream.write(message.getBytes());
                String newLine = System.getProperty("line.separator");
                fileOutputStream.write(newLine.getBytes());
            }catch (IOException e){
                System.out.println("上报信息写出出错");
                return;
            }finally {
                if(fileOutputStream != null){
                    fileOutputStream.close();
                }
            }*/

            Integer deviceTypeId = 0;
            //处理报文
            try {
                //解析报文
                List<SingleLampParamTempVO> singleLampParamList = MessageOperationUtil.loraOldGetInformation(loraReport.getDevEUI(),message);
                if(singleLampParamList != null && singleLampParamList.size()>0){
                    List<Integer> deviceTypeIdList = new ArrayList<>();
                    deviceTypeIdList.add(3);
                    deviceTypeIdList.add(4);
                    Integer selectCountByNum = systemDeviceService.selectCountByNum(singleLampParamList.get(0).getDeviceNum(), deviceTypeIdList);
                    if(selectCountByNum==1){
                        deviceTypeId = 3;
                    }else if(selectCountByNum==2){
                        deviceTypeId = 4;
                    }else {
                        return;
                    }
                    for (int i = 0; i<singleLampParamList.size(); i++){
                        //处理报文
                        SingleLampParamTempVO singleLampParam = singleLampParamList.get(i);
                        if(i%2==0){
                            singleLampParam.setLoopNum(1);
                        }else {
                            singleLampParam.setLoopNum(2);
                        }
                        MessageOperationUtil.remind(singleLampParam,deviceTypeId);
                    }
                }
            }catch (Exception e){
                System.out.println("处理接收报文出错");
            }
        }catch (Exception e){
            System.out.println("出错了");
        }
        System.out.println("Lora返回uplink信息处理完毕");
        System.out.println("");
    }


    @PostMapping("/newLora/uplink")
    public void newUplink(@RequestBody String json){
        System.out.println("新lora订阅消息" + json);
        try {
            //封装传递的Json字符串
            JSONObject jsonObject = JSON.parseObject(json);
            //获取报文
            JSONObject content = (JSONObject)jsonObject.get("content");
            LoraResponse loraResponse = JSONObject.parseObject(content.toJSONString(), LoraResponse.class);
            String data = loraResponse.getData();
            String devEui = loraResponse.getDevEui();
            Long timestamp = loraResponse.getTimestamp();


            byte[] bytes = PushDataUtils.checkAndDecrypt(data, timestamp);
            String message = HexUtil.bytesTohex(bytes).replace(" ", "");
            System.out.println(message);
            Integer deviceTypeId = 0;
            //处理报文
            try {
                //解析报文
                List<SingleLampParamTempVO> singleLampParamList = MessageOperationUtil.loraNewGetInformation(devEui,message);
                if(singleLampParamList != null && singleLampParamList.size()>0){
                    List<Integer> deviceTypeIdList = new ArrayList<>();
                    deviceTypeIdList.add(9);
                    deviceTypeIdList.add(10);
                    Integer selectCountByNum = systemDeviceService.selectCountByNum(singleLampParamList.get(0).getDeviceNum(), deviceTypeIdList);
                    if(selectCountByNum==1){
                        deviceTypeId = 9;
                    }else if(selectCountByNum==2){
                        deviceTypeId = 10;
                    }else {
                        return;
                    }
                    for (int i = 0; i<singleLampParamList.size(); i++){
                        //处理报文
                        SingleLampParamTempVO singleLampParam = singleLampParamList.get(i);
                        if(i%2==0){
                            singleLampParam.setLoopNum(1);
                        }else {
                            singleLampParam.setLoopNum(2);
                        }
                        MessageOperationUtil.remind(singleLampParam,4);
                    }
                }
            }catch (Exception e){
                System.out.println("处理接收报文出错");
            }
        }catch (Exception e){
            System.out.println("出错了");
        }
        System.out.println("newLora返回uplink信息处理完毕");
        System.out.println("");
    }

    @PostMapping("/test")
    @ApiOperation(value = "查询所有灯具", notes = "查询所有灯具,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    public void test(){
        /*String deviceId = "52435e37255444ab95bb69c2e39c6fad";
        String message = MessageGeneration.nbTurnOffLight("0000007B");
        try {
            boolean b = CTWingApi.sendMessage(deviceId, message);
            System.out.println(b);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*String devEui = "009569000000D7D1";
        String newLoraNode = MessageOperationUtil.createNewLoraNode(devEui);
        System.out.println(newLoraNode);*/
        List<String> ids = new ArrayList<>();
        ids.add("5f3ce2c47979c4000100769f");
        boolean b = MessageOperationUtil.deleteNewLoraNode(ids);
        System.out.println(b);


        /*String imei = "860411040200071";
        try {
            Result test = CTWingApi.createDevice("测试", imei);
            System.out.println(test.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*try {
            boolean test = CTWingApi.deleteDevice("bea91c37dcf8474a8973bedffac85f17");
            System.out.println(test);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

}
