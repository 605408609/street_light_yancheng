package com.exc.street.light.sl.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.enums.sl.ht.HtCommandTypeEnum;
import com.exc.street.light.sl.service.LampHtMessageService;
import com.exc.street.light.sl.utils.HtMsgAnalyzeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 华体集中控制器消息回送类
 *
 * @Author: Xiaok
 * @Date: 2020/8/14 16:08
 */
@ApiIgnore
@Slf4j
@RestController
@RequestMapping("/wxserver")
public class HtLampReceiveController {

    @Autowired
    private HtMsgAnalyzeUtil htMsgAnalyzeUtil;

    @Autowired
    private LampHtMessageService lampHtMessageService;

    /**
     * TODO 命令回送接收
     *
     * @return
     */
    @PostMapping("/order")
    public JSONObject orderBack(@RequestBody JSONObject resObj) {
        htMsgAnalyzeUtil.msgAnalyze(resObj);
        return null;
    }

    /**
     * TODO 集中控制器运行数据接收
     *
     * @return
     */
    @PostMapping("/focusInfo")
    public JSONObject focusInfo(@RequestBody JSONObject resObj) {
        //集中控制器通讯地址
        String id = resObj.getString("id");
        //命令类型
        Integer orderKind = resObj.getInteger("orderkind");
        if (StringUtils.isBlank(id) || orderKind == null || !orderKind.equals(HtCommandTypeEnum.DATA_UPLOAD_LAMP_POST.code())) {
            return null;
        }
        JSONObject dataObj = resObj.getJSONObject("data");
        if (dataObj == null) {
            return null;
        }
        //时间 格式
        String time = dataObj.getString("time");
        //控制输出列表 int[4]
        JSONArray ioOut = dataObj.getJSONArray("ioout");
        //IO输入检测列表 0-false 1-true int[8]
        JSONArray ioIn = dataObj.getJSONArray("ioin");
        //模拟量列表 前三为三相电压 float[20]
        JSONArray analog = dataObj.getJSONArray("analog");
        //模拟量转io列表 0-false 1-true int[20]
        JSONArray analogIo = dataObj.getJSONArray("analogio");
        //漏电流列表 float[4]
        JSONArray leak = dataObj.getJSONArray("leak");
        //信号质量
        Integer signal = dataObj.getInteger("signal");
        //温度
        Integer tem = dataObj.getInteger("tem");
        //供电情况(1-市电 2-电池)
        Integer power = dataObj.getInteger("power");
        //告警信息数组
        JSONArray warn = dataObj.getJSONArray("warn");
        if (warn == null || warn.isEmpty()) {
            return null;
        }
        for (int i = 0; i < warn.size(); i++) {
            JSONObject warnObj = warn.getJSONObject(i);
            //告警信息
            String info = warnObj.getString("info");
        }
        return null;
    }

    /**
     * 单灯集中器运行数据接收
     *
     * @return
     */
    @PostMapping("/aloneInfo")
    public JSONObject aloneInfo(@RequestBody JSONObject resObj) {
        lampHtMessageService.aloneInfo(resObj);
        return null;
    }
}
