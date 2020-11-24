/**
 * @filename:LampDeviceController 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.resource.qo.SlLampDeviceQuery;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.resource.vo.SingleLampParamTempVO;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamReqVO;
import com.exc.street.light.sl.VO.DeviceDataChangedVO;
import com.exc.street.light.sl.VO.DeviceInfoChangedVO;
import com.exc.street.light.sl.VO.DeviceServiceData;
import com.exc.street.light.sl.service.LampDeviceService;
import com.exc.street.light.sl.service.SystemDeviceService;
import com.exc.street.light.sl.utils.MessageOperationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>灯控设备控制器</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-17
 */
@Api(tags = "灯控设备控制器", value = "灯控设备控制器")
@RestController
@RequestMapping("/api/sl/lamp/device")
public class LampDeviceController{

    @Autowired
    private LampDeviceService lampDeviceService;

    @Autowired
    private SystemDeviceService systemDeviceService;

    /**
     * @explain 查询所有灯具
     * @author Longshuangyang
     * @time 2020-03-16
     */
    /*@GetMapping("/all")
    @ApiOperation(value = "查询所有灯具", notes = "查询所有灯具,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    public Result<List<LampDevice>> getList(@RequestParam(required = false) String deviceName, HttpServletRequest request) {
        return lampDeviceService.getList(deviceName, request);
    }*/

    /**
     * @explain 查询灯具(根据灯杆id集合)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/pulldown/by/lamp/post")
    @ApiOperation(value = "查询灯具(根据灯杆id集合)", notes = "查询灯具(根据灯杆id集合),作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    public Result pulldownByLampPost(@RequestParam(required = false) List<Integer> lampPostIdList, HttpServletRequest request) {
        return systemDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * @explain 查询灯具(根据灯杆id集合)
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PostMapping("/pulldown/by/lamp/post")
    @ApiOperation(value = "查询灯具(根据灯杆id集合)", notes = "查询灯具(根据灯杆id集合),作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    public Result pulldownByLampPostGet(@ApiParam @RequestBody List<Integer> lampPostIdList, HttpServletRequest request) {
        return systemDeviceService.pulldownByLampPost(lampPostIdList, request);
    }

    /**
     * 实时控制
     *
     * @param request
     * @param vo      控制参数
     * @return
     */
    @PostMapping("/control")
    @ApiOperation(value = "下发控制", notes = "下发控制,作者：Longshuangyang")
    @RequiresPermissions(value = {"sl:module:light:strategy:light", "sl:module:map:light"}, logical = Logical.OR)
    @SystemLog(logModul = "智慧照明",logType = "控制",logDesc = "下发控制")
    public Result control(HttpServletRequest request, @RequestBody SlReqLightControlVO vo) {
        return systemDeviceService.control(request, vo);
    }

    /**
     * 实时控制
     *
     * @param request
     * @param vo      控制参数
     * @return
     */
    @PostMapping("/controlTemp")
    @ApiOperation(value = "下发控制", notes = "下发控制,作者：Longshuangyang")
    @RequiresPermissions(value = {"sl:module:light:strategy:light", "sl:module:map:light"}, logical = Logical.OR)
    @SystemLog(logModul = "智慧照明",logType = "控制",logDesc = "下发控制")
    public Result controlTemp(HttpServletRequest request, @RequestBody SlReqLightControlVO vo) {
        return lampDeviceService.controlTemp(request, vo);
    }


    /**
     * 多播组实时控制
     *
     * @param request
     * @param vo      控制参数
     * @return
     */
    /*@PostMapping("/allControl")
    @ApiOperation(value = "多播组下发控制", notes = "下发控制,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control:detail")
    public Result allControl(HttpServletRequest request, @RequestBody SlReqLightControlVO vo) {
        return lampDeviceService.controlAll(request, vo);
    }*/



    /**
     * @explain 灯控设备分页条件查询
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/page")
    @ApiOperation(value = "灯控设备分页条件查询", notes = "灯控设备分页条件查询,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control")
    public Result getPage(SlLampDeviceQuery slLampDeviceQuery, HttpServletRequest request) {
        return systemDeviceService.getPage(slLampDeviceQuery, request);
    }

    /**
     * @explain 添加灯控设备
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加灯控设备", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control:add")
    @SystemLog(logModul = "设备管理",logType = "新增",logDesc = "添加灯控设备")
    public Result add(@ApiParam @RequestBody SingleLampParamReqVO singleLampParamRespVO, HttpServletRequest request) {
        return systemDeviceService.add(singleLampParamRespVO, request);
    }

    /**
     * @explain 修改灯控设备
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改灯控设备", notes = "修改灯控设备,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control:update")
    @SystemLog(logModul = "设备管理",logType = "编辑",logDesc = "修改灯控设备")
    public Result updateDevice(@ApiParam @RequestBody SingleLampParamReqVO singleLampParamRespVO, HttpServletRequest request) {
        return systemDeviceService.updateDevice(singleLampParamRespVO, request);
    }

    /**
     * @explain 灯控设备验证唯一性
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @PostMapping("/unique")
    @ApiOperation(value = "灯控设备验证唯一性", notes = "灯控设备分页条件查询,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control:add")
    public Result unique(@ApiParam @RequestBody SingleLampParamReqVO singleLampParamRespVO, HttpServletRequest request) {
        return systemDeviceService.unique(singleLampParamRespVO, request);
    }

    /**
     * @explain 灯控设备验证唯一性(NH)
     * @author Huangjinhao
     * @time 2020-03-17
     */
    @PostMapping("/uniqueNh")
    @ApiOperation(value = "灯控设备验证唯一性", notes = "灯控设备分页条件查询,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control:add")
    public Result uniqueNh(@ApiParam @RequestBody SingleLampParamReqVO singleLampParamRespVO, HttpServletRequest request) {
        return systemDeviceService.uniqueNh(singleLampParamRespVO, request);
    }

    /**
     * @explain 灯控设备详情
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "灯控设备详情", notes = "灯控设备详情,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:same:device:light:control:detail")
    public Result detail(@PathVariable Integer id, HttpServletRequest request) {
        return systemDeviceService.detail(id, request);
    }

    /**
     * @explain 删除设备
     * @author hjh
     * @time 2020-03-23
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除设备", notes = "删除设备,作者：hjh")
    @RequiresPermissions(value = "dm:module:same:device:light:control:delete")
    public Result delete(@PathVariable Integer id,HttpServletRequest request) {
        return systemDeviceService.delete(id, request);
    }

    /**
     * @explain 批量删除设备
     * @author hjh
     * @time 2020-03-23
     */
    @DeleteMapping("/deleteList/{ids}")
    @ApiOperation(value = "删除设备", notes = "删除设备,作者：hjh")
    @RequiresPermissions(value = "dm:module:same:device:light:control:delete")
    public Result delete(@PathVariable List<Integer> ids,HttpServletRequest request) {
        return systemDeviceService.delete(ids, request);
    }

    /**
     * @explain 订阅设备信息接口
     * @author huangjinhao
     * @time 2020-03-17
     */
    @PostMapping("/deviceInfoChanged")
    @ApiOperation(value = "订阅设备信息接口", notes = "订阅设备信息接口,作者：huangjinhao")
    public String deviceInfoChanged(@RequestBody String json) {
        System.out.println(json);
        System.out.println("设备信息有变化");

        DeviceInfoChangedVO deviceInfoChangedVO = JSON.parseObject(json, DeviceInfoChangedVO.class);
        System.out.println(deviceInfoChangedVO);

        //lampDeviceService.deviceInfoChanged(deviceInfoChangedVO);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Status Code","200 OK");
        return jsonObject.toJSONString();
    }

    /**
     * @explain 订阅设备数据接口
     * @author huangjinhao
     * @time 2020-03-17
     */
    @PostMapping("/deviceDataChanged")
    @ApiOperation(value = "订阅设备数据接口", notes = "订阅设备数据接口,作者：huangjinhao")
    public String deviceDataChanged(@RequestBody String json) {
        System.out.println("设备数据接口信息："+json);
        //数据从此处返回
        DeviceDataChangedVO deviceDataChangedVO = JSON.parseObject(json, DeviceDataChangedVO.class);
        DeviceServiceData deviceServiceData = deviceDataChangedVO.getService();
        String deviceId = deviceDataChangedVO.getDeviceId();
        String data = deviceServiceData.getData().getData();
        //解析上行协议信息
        MessageOperationUtil.getShuncomInformation(data,deviceId);
        //lampDeviceService.deviceDataChangedVO(deviceDataChangedVO);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Status Code","200 OK");
        return jsonObject.toJSONString();
    }

    /**
     * @explain 设置设备变化订阅接口
     * @author huangjinhao
     * @time 2020-03-17
     */
    @GetMapping("/subscription")
    @ApiOperation(value = "设置设备变化订阅接口", notes = "设置设备变化订阅接口,作者：huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control:detail")
    public Result subscription(@RequestParam Integer type,@RequestParam String url,HttpServletRequest request) {
        return lampDeviceService.subscription(type,url,request);
    }

    /**
     * @explain 下发命令获取返回值推送接口
     * @author huangjinhao
     * @time 2020-03-17
     */
    @PostMapping("/issue")
    @ApiOperation(value = "下发命令获取返回值推送接口", notes = "下发命令获取返回值推送接口,作者：huangjinhao")
    public String issue(@RequestBody String json) {
        System.out.println(json);
        System.out.println("下发命令返回的消息接收完毕");
        //lampDeviceService.deviceDataChangedVO(deviceDataChangedVO);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Status Code","200 OK");
        return jsonObject.toJSONString();
    }

    /**
     * @explain 订阅设备信息接口
     * @author huangjinhao
     * @time 2020-03-17
     */
    @PostMapping("/test")
    @ApiOperation(value = "订阅设备信息接口", notes = "订阅设备信息接口,作者：huangjinhao")
    public String test(@RequestBody String json) {
        System.out.println(json);
        System.out.println("！！！！！！！！！！！！！！！！！！！！！！！！！！！#@#@#@###################@#@#@#@#");
        return null;
    }

    @PostMapping("/CTWing/subscription")
    public void subscriptionNb(@RequestBody String json){
        try {
            System.out.println("CTWing订阅消息nb" + json);
            JSONObject jsonObject = (JSONObject)JSONObject.parse(json);
            JSONObject payloadJson = (JSONObject)jsonObject.get("payload");
            String imei = (String)jsonObject.get("IMEI");
            String data = (String) payloadJson.get("data");
            Base64 base64 = new Base64();
            byte[] decode = base64.decode(data);
            String message = HexUtil.bytesTohex(decode).replace(" ", "");
            System.out.println(message);

            Integer deviceTypeId = 0;

            //解析报文
            List<SingleLampParamTempVO> singleLampParamList = MessageOperationUtil.dxnbGetInformation(imei,message);
            if(singleLampParamList != null && singleLampParamList.size()>0){
                List<Integer> deviceTypeIdList = new ArrayList<>();
                deviceTypeIdList.add(7);
                deviceTypeIdList.add(8);
                Integer selectCountByNum = systemDeviceService.selectCountByNum(singleLampParamList.get(0).getDeviceNum(), deviceTypeIdList);
                if(selectCountByNum==1){
                    deviceTypeId = 7;
                }else if(selectCountByNum==2){
                    deviceTypeId = 8;
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
            e.printStackTrace();
        }
    }

    @PostMapping("/CTWing/subscription/catOne")
    public void subscriptionCatOne(@RequestBody String json){
        try {
            System.out.println("CTWing订阅消息catOne" + json);
            JSONObject jsonObject = (JSONObject)JSONObject.parse(json);
            JSONObject payloadJson = (JSONObject)jsonObject.get("payload");
            String deviceId = (String)jsonObject.get("deviceId");
            String message = (String) payloadJson.get("data");
            System.out.println(message);
            Integer deviceTypeId = 0;

            //解析报文
            List<SingleLampParamTempVO> singleLampParamList = MessageOperationUtil.dxCatOneGetInformation(deviceId,message);
            if(singleLampParamList != null && singleLampParamList.size()>0){
                List<Integer> deviceTypeIdList = new ArrayList<>();
                deviceTypeIdList.add(14);
                deviceTypeIdList.add(15);
                Integer selectCountByNum = systemDeviceService.selectCountByNum(singleLampParamList.get(0).getDeviceNum(), deviceTypeIdList);
                if(selectCountByNum==1){
                    deviceTypeId = 14;
                }else if(selectCountByNum==2){
                    deviceTypeId = 15;
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
            e.printStackTrace();
        }
    }

}