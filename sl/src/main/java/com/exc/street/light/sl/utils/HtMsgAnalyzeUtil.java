package com.exc.street.light.sl.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.entity.sl.LampHtMessage;
import com.exc.street.light.resource.enums.sl.ht.HtCommandTypeEnum;
import com.exc.street.light.sl.service.LampHtMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 华体灯具消息解析类
 *
 * @Author: Xiaok
 * @Date: 2020/8/17 9:40
 */
@Slf4j
@Component
public class HtMsgAnalyzeUtil {
    @Autowired
    private LampHtMessageService lampHtMessageService;

    /**
     * 消息解析
     *
     * @param dataObj
     */
    public void msgAnalyze(JSONObject dataObj) {
        if (dataObj == null) {
            return;
        }
        //命令唯一标识 与下发命令标识相同
        Integer orderIndex = dataObj.getInteger("orderindex");
        if (orderIndex == null) {
            return;
        }
        //TODO 根据命令唯一标识，获取对应操作的集中控制器信息，传入解析方法里进行处理
        LampHtMessage htMessage = lampHtMessageService.getById(orderIndex);
        if (htMessage == null || htMessage.getLocationControlId() == null) {
            log.info("数据库不存在此条消息序号记录:{}", dataObj);
            return;
        }
        dataObj.put("locationControlId", htMessage.getLocationControlId());
        //下发命令类型 HtCommandTypeEnum
        Integer orderKind = dataObj.getInteger("orderkind");
        //返回命令类型 HtCommandTypeEnum
        Integer dataKind = dataObj.getInteger("datakind");
        //返回状态 HtResultEnum
        String rCode = dataObj.getString("rcode");
        //返回错误信息
        String errMsg = dataObj.getString("errmsg");

        //保存返回消息
        htMessage.setReturnCmdType(dataKind);
        htMessage.setReturnTime(new Date());
        htMessage.setReturnCmd(dataObj.toJSONString());
        try {
            if(StringUtils.isNotBlank(rCode)){
                htMessage.setReturnResult(Integer.parseInt(rCode));
            }
        } catch (NumberFormatException e) {
            log.error("消息返回结果错误:{}",dataObj.toString());
            return;
        }
        //保存返回消息
        lampHtMessageService.save(htMessage);
        switch (HtCommandTypeEnum.getByCode(dataKind)) {
            case SEARCH_ACCESS_DEVICE_BY_LAMP_POST:
                searchAccessDeviceByLampPostReturn(dataObj);
                break;
            case SET_LAMP_POST:
                setLampPostReturn(dataObj);
                break;
            case SEARCH_RELEGATION:
                searchRelegationReturn(dataObj);
                break;
            case SET_RELEGATION:
                setRelegationReturn(dataObj);
                break;
            case SEARCH_SINGLE_LAMP_INFO:
                searchSingleLampInfoReturn(dataObj);
                break;
            case SET_SINGLE_LAMP_INFO:
                setSingleLampInfoReturn(dataObj);
                break;
            case SEARCH_LAMP_POST_REAL:
                searchLampPostRealReturn(dataObj);
                break;
            case SEARCH_SINGLE_LAMP_REAL:
                searchSingleLampRealReturn(dataObj);
                break;
            case SEARCH_LAMP_POST_PLAN:
                searchLampPostPlanReturn(dataObj);
                break;
            case SET_LAMP_POST_PLAN:
                setLampPostPlanReturn(dataObj);
                break;
            case SEARCH_SINGLE_LAMP_PLAN:
                searchSingleLampPlanReturn(dataObj);
                break;
            case SET_SINGLE_LAMP_PLAN:
                setSingleLampPlanReturn(dataObj);
                break;
            case MANUAL_OUTPUT_LAMP_POST:
                manualOutputLampPostReturn(dataObj);
                break;
            case MANUAL_OUTPUT_SINGLE_LAMP:
                manualOutputSingleLampReturn(dataObj);
                break;
            default:
                break;
        }
    }

    /**
     * 解析 查询集中控制器接入的单灯集中器通讯地址
     */
    public void searchAccessDeviceByLampPostReturn(JSONObject dataObj) {
        //单灯集中器通讯地址
        String data = dataObj.getString("data");
    }

    /**
     * 解析 设置路灯集中器的单灯集中器通讯地址
     */
    public void setLampPostReturn(JSONObject dataObj) {
        String data = dataObj.getString("data");
    }

    /**
     * 解析 查询归属
     *
     * @param dataObj
     */
    public void searchRelegationReturn(JSONObject dataObj) {
        JSONObject data = dataObj.getJSONObject("data");
        //int[8] HtBranchOwnershipEnum io支路与回路的对应关系
        JSONArray io = data.getJSONArray("io");
        //int[20] HtBranchOwnershipEnum io检测和模拟量检测支路与回路的对应关系
        JSONArray analog = data.getJSONArray("analog");
    }

    /**
     * 解析 设置归属
     *
     * @param dataObj
     */
    public void setRelegationReturn(JSONObject dataObj) {
        String data = dataObj.getString("data");
    }

    /**
     * 解析 查询单灯控制器信息
     *
     * @param dataObj
     */
    public void searchSingleLampInfoReturn(JSONObject dataObj) {
        //单灯控制器数组
        JSONArray singleLampArr = dataObj.getJSONArray("data");
        if (singleLampArr == null || singleLampArr.isEmpty()) {
            return;
        }
        for (int i = 0; i < singleLampArr.size(); i++) {
            JSONObject singleLampObj = singleLampArr.getJSONObject(i);
            //单灯控制器序号
            Integer index = singleLampObj.getInteger("index");
            //单灯控制器通讯地址
            String address = singleLampObj.getString("address");
        }
    }

    /**
     * 解析 设置单灯控制器信息
     *
     * @param dataObj
     */
    public void setSingleLampInfoReturn(JSONObject dataObj) {
        JSONObject data = dataObj.getJSONObject("data");
        if (data == null) {
            return;
        }
        //设置失败的单灯控制器序号数组
        JSONArray faultArr = data.getJSONArray("fault");
        if (faultArr == null || faultArr.isEmpty()) {
            return;
        }
        for (int i = 0; i < faultArr.size(); i++) {
            //设置失败的单灯控制器序号
            Integer singleLampIndex = faultArr.getInteger(i);
        }
    }

    /**
     * 解析 查询单灯控制器运行数据及报警状态
     *
     * @param jsonObject
     */
    public void searchLampPostRealReturn(JSONObject jsonObject) {
        JSONObject dataObj = jsonObject.getJSONObject("data");
        if (dataObj == null) {
            return;
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
            return;
        }
        for (int i = 0; i < warn.size(); i++) {
            JSONObject warnObj = warn.getJSONObject(i);
            //告警信息
            String info = warnObj.getString("info");
        }
    }

    /**
     * 解析 查询单灯控制器运行数据
     *
     * @param jsonObject
     */
    public void searchSingleLampRealReturn(JSONObject jsonObject) {
        //单灯控制器数据
        JSONArray dataArr = jsonObject.getJSONArray("data");
        if (dataArr == null || dataArr.isEmpty()) {
            return;
        }
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject dataObj = dataArr.getJSONObject(i);
            //单灯控制器索引
            Integer index = dataObj.getInteger("index");
            //通讯状态　1-在线,2-离线
            Integer state = dataObj.getInteger("state");
            //通讯状态变更时间 yyyy-MM-dd HH:mm:ss
            String time = dataObj.getString("time");
            //漏电流
            Float leakc = dataObj.getFloat("leakc");
            //电压频率
            Float vf = dataObj.getFloat("vf");
            //单灯控制器支路数据
            JSONArray loopDataArr = dataObj.getJSONArray("data");
            if (loopDataArr == null || loopDataArr.isEmpty()) {
                continue;
            }
            for (int j = 0; j < loopDataArr.size(); j++) {
                JSONObject loopDataObj = loopDataArr.getJSONObject(j);
                //输出类型 HtSingleLampBranchOutputEnum
                Integer act = loopDataObj.getInteger("act");
                //功率
                Float gl = loopDataObj.getFloat("gl");
                //功率因数
                Float gly = loopDataObj.getFloat("gly");
                //电压
                Float v = loopDataObj.getFloat("v");
                //电流
                Float c = loopDataObj.getFloat("c");
            }
        }
        return;
    }

    /**
     * 解析 查询单灯控制器回路输出计划
     *
     * @param dataObj
     */
    public void searchLampPostPlanReturn(JSONObject dataObj) {
        JSONObject data = dataObj.getJSONObject("data");
        if (data == null) {
            return;
        }
        //回路输出计划
        JSONArray pointArr = data.getJSONArray("point");
        if (pointArr == null || pointArr.isEmpty()) {
            return;
        }
        for (int i = 0; i < pointArr.size(); i++) {
            JSONObject pointDataObj = pointArr.getJSONObject(i);
            //设定时间
            String time = pointDataObj.getString("time");
            //回路输出类型 HtLoopOutputTypeEnum
            Integer act = pointDataObj.getInteger("act");
            //四个回路的动作 0不启用 1启用
            JSONArray out = pointDataObj.getJSONArray("out");
        }
    }

    /**
     * 解析 设置集中控制器回路输出计划
     *
     * @param dataObj
     */
    public void setLampPostPlanReturn(JSONObject dataObj) {
        String data = dataObj.getString("data");
    }

    /**
     * 解析 查询单灯控制器定时计划
     *
     * @param resObj
     */
    public void searchSingleLampPlanReturn(JSONObject resObj) {
        JSONArray dataArr = resObj.getJSONArray("data");
        if (dataArr == null || dataArr.isEmpty()) {
            return;
        }
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject dataObj = dataArr.getJSONObject(i);
            //单灯控制器索引
            Integer index = dataObj.getInteger("node");
            //计划数组
            JSONArray planArr = dataObj.getJSONArray("plan");
            if (planArr == null || planArr.isEmpty()) {
                continue;
            }
            for (int j = 0; j < planArr.size(); j++) {
                JSONObject planObj = planArr.getJSONObject(j);
                //设定时间
                String time = planObj.getString("time");
                //输出类型 HtSingleLampBranchOutputEnum[3]  分别对应单灯控制器三条支路
                JSONArray act = planObj.getJSONArray("act");
            }
        }
    }

    /**
     * 解析 设置单灯控制器定时计划
     *
     * @param resObj
     */
    public void setSingleLampPlanReturn(JSONObject resObj) {
        JSONObject dataObj = resObj.getJSONObject("data");
        if (dataObj == null) {
            return;
        }
        JSONArray faultArr = dataObj.getJSONArray("fault");
        if (faultArr == null || faultArr.isEmpty()) {
            return;
        }
        for (int i = 0; i < faultArr.size(); i++) {
            //设置失败的单灯控制器索引
            Integer integer = faultArr.getInteger(i);
        }
    }

    /**
     * 解析 单灯控制器回路手动输出
     *
     * @param resObj
     */
    public void manualOutputLampPostReturn(JSONObject resObj) {
        String data = resObj.getString("data");
    }

    /**
     * 解析 单灯控制器回路手动输出
     *
     * @param resObj
     */
    public void manualOutputSingleLampReturn(JSONObject resObj) {
        JSONObject dataObj = resObj.getJSONObject("data");
        if (dataObj == null) {
            return;
        }
        JSONArray faultArr = dataObj.getJSONArray("fault");
        if (faultArr == null || faultArr.isEmpty()) {
            return;
        }
        for (int i = 0; i < faultArr.size(); i++) {
            //设置失败的单灯控制器索引
            Integer integer = faultArr.getInteger(i);
        }
    }
}
