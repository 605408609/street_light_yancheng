/**
 * @filename:EdAshcanController 2020-09-28
 * @project ed  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ed.web;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.ed.service.EdAshcanService;
import com.exc.street.light.ed.service.impl.EdAshcanServiceImpl;
import com.exc.street.light.ed.utils.RedisUtil;
import com.exc.street.light.lj.utils.CTWingApi;
import com.exc.street.light.lj.utils.MessageParse;
import com.exc.street.light.lj.vo.TrashVo;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ed.EdAshcan;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.resource.vo.req.EdReqAshcanPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**   
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 垃圾桶信息表Controller</P>
 * @version: V1.0
 * @author: Huang Jin Hao
 * @time    2020-09-28
 *
 */
@Api(tags = "垃圾桶信息表",value="垃圾桶信息表" )
@RestController
@RequestMapping("/api/ed/ed/ashcan")
public class EdAshcanController {
    private static final Logger logger = LoggerFactory.getLogger(EdAshcanServiceImpl.class);
    @Autowired
    private EdAshcanService edAshcanService;
    @Autowired
    private RedisUtil redisUtil;
    
    /**
     * @explain 垃圾桶设备分页条件查询
     * @author Huangjinhao
     * @time 2020-09-07
     */
    @GetMapping("/page")
    @ApiOperation(value = "垃圾桶设备分页条件查询", notes = "垃圾桶设备分页条件查询,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:trash:detail")
    public Result getPage(EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request) {
        return edAshcanService.getPage(edReqAshcanPageVO, request);
    }

    /**
     * @explain 垃圾桶设备验证唯一性
     * @author Huangjinhao
     * @time 2020-03-17
     */
    @PostMapping("/unique")
    @ApiOperation(value = "垃圾桶设备验证唯一性", notes = "垃圾桶设备验证唯一性,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:trash:add")
    public Result unique(@ApiParam @RequestBody EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request) {
        return edAshcanService.unique(edReqAshcanPageVO, request);
    }

    /**
     * @explain 添加垃圾桶设备
     * @author Huangjinhao
     * @time 2020-09-07
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加垃圾桶设备", notes = "作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:trash:add")
    @SystemLog(logModul = "设备管理",logType = "新增",logDesc = "新增垃圾桶设备")
    public Result add(@ApiParam @RequestBody EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request) {
        return edAshcanService.add(edReqAshcanPageVO, request);
    }

    /**
     * @explain 修改垃圾桶设备
     * @author Huangjinhao
     * @time 2020-03-16
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改垃圾桶设备", notes = "修改垃圾桶设备,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:trash:update")
    @SystemLog(logModul = "设备管理",logType = "编辑",logDesc = "编辑垃圾桶设备")
    public Result updateDevice(@ApiParam @RequestBody EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request) {
        return edAshcanService.updateDevice(edReqAshcanPageVO, request);
    }

    /**
     * @explain 批量删除垃圾桶设备
     * @author Huangjinhao
     * @time 2020-03-23
     */
    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "批量删除垃圾桶设备", notes = "批量删除垃圾桶设备,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:trash:delete")
    @SystemLog(logModul = "设备管理",logType = "删除",logDesc = "删除垃圾桶设备")
    public Result delete(@PathVariable String ids, HttpServletRequest request) {
        return edAshcanService.deleteByIds(ids, request);
    }

    /**
     * @explain 设置开盖报警倾角阈值/布防撤防
     * @author Huangjinhao
     * @time 2020-03-16
     */
    @PostMapping("/setThreshold")
    @ApiOperation(value = "设置开盖报警倾角阈值/布防撤防", notes = "设置开盖报警倾角阈值/布防撤防,作者：Huangjinhao")
    @RequiresPermissions(value = "dm:module:same:device:light:control:update")
    @SystemLog(logModul = "设备管理",logType = "编辑",logDesc = "设置开盖报警倾角阈值/布防撤防")
    public Result setThreshold(@ApiParam @RequestBody List<EdReqAshcanPageVO> edReqAshcanPageVOS, HttpServletRequest request) {
        return edAshcanService.setThreshold(edReqAshcanPageVOS, request);
    }


    /**
     * @author wanglijun
     * @date 2020/10/19  10:07
     */
    @PostMapping("/gcReceive")
    @ApiOperation(value = "垃圾桶数据接收", notes = "垃圾桶数据接收,作者：wanglijun")
    public JSONObject orderBack(@RequestBody String resObj) {
        try{
            logger.info("CTWing订阅垃圾桶消息nb"+resObj);
            JSONObject jsonObject = (JSONObject)JSONObject.parse(resObj);
            JSONObject payloadJson = (JSONObject)jsonObject.get("payload");
            String imei = (String)jsonObject.get("IMEI");
            String data = (String) payloadJson.get("APPdata");
            String deviceId = (String)jsonObject.get("deviceId");
            //在redis中添加此设备当前访问的秒数
            long currentTimeMillis = System.currentTimeMillis();
            redisUtil.set(deviceId,currentTimeMillis);
            redisUtil.expire(deviceId,90000);
            //byte[] decode1 = java.util.Base64.getDecoder().decode(resObj);
            Base64 base64 = new Base64();
            byte[] decode = base64.decode(data);
            String message = HexUtil.bytesTohex(decode).replace(" ", "");
            logger.info("接收垃圾桶发送报文"+message);
            TrashVo trashVos = MessageParse.getInformation(message);
            edAshcanService.alarmHandling(trashVos,imei);
            logger.info("垃圾桶,接收参数:{}", trashVos);
            //应答报文
            String start = MessageParse.start(message);
            //发送应答报文
            CTWingApi.sendMessage(deviceId,start);
            //判断上行状态码
            if(message.substring(2, 4).equals("01") || message.substring(2, 4).equals("02")){
                edAshcanService.sendMessage(imei);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}