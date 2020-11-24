package com.exc.street.light.sl.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.dto.sl.ht.HtCommandDTO;
import com.exc.street.light.resource.enums.sl.ht.HtCommandTypeEnum;
import com.exc.street.light.resource.enums.sl.ht.HtLoopOutputTypeEnum;
import com.exc.street.light.resource.enums.sl.ht.HtResultEnum;
import com.exc.street.light.resource.enums.sl.ht.HtSingleLampBranchOutputEnum;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.vo.req.htLamp.*;
import com.exc.street.light.sl.config.HtLampConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 华体消息发送类
 *
 * @Author: Xiaok
 * @Date: 2020/8/13 11:15
 */
@Slf4j
@Component
public class HtMessageSender {

    @Autowired
    private HtLampConfig htLampConfig;

    private static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 华体接口登录后的token
     */
    private static String token;

    public void login() {
        JSONObject paramObj = new JSONObject(true);
        paramObj.put("account", htLampConfig.getUsername());
        paramObj.put("password", htLampConfig.getPassword());
        String resultStr = sendRequest(paramObj, HtCommandTypeEnum.LOGIN);
        JSONObject loginResObj = JSONObject.parseObject(resultStr);
        if (loginResObj != null && loginResObj.get("rcode") != null && String.valueOf(HtResultEnum.SUCCESS.code()).equals(loginResObj.getString("rcode"))) {
            token = loginResObj.getString("token");
        } else {
            log.error("华体-登录失败,url:{}", htLampConfig.getLoginUrl());
        }
    }


    /**
     * 查询集中控制器接入单灯集中器通讯地址
     *
     * @return
     */
    public HtCommandDTO searchAccessDeviceByLampPost() {
        //下发命令
        return new HtCommandDTO(null, HtCommandTypeEnum.SEARCH_ACCESS_DEVICE_BY_LAMP_POST);
    }

    /**
     * 设置集中控制器接入单灯集中器通讯地址
     *
     * @param slcId 单灯集中器通讯地址
     * @return
     */
    public HtCommandDTO setAccessDeviceByLampPost(String slcId) {
        JSONObject paramObj = new JSONObject(true);
        paramObj.put("slcid", slcId);
        //下发命令
        return new HtCommandDTO(paramObj, HtCommandTypeEnum.SET_LAMP_POST);
    }

    /**
     * 获取特定通讯地址的集中控制器归属设置
     *
     * @return
     */
    public HtCommandDTO searchRelegation() {
        //下发命令
        return new HtCommandDTO(null, HtCommandTypeEnum.SEARCH_RELEGATION);
    }

    /**
     * 设置特定通讯地址的集中控制器归属
     *
     * @param ro
     * @return
     */
    public HtCommandDTO setRelegation(HtSetRelegationRequestVO ro) {
        if (ro == null || ro.getIo() == null || ro.getIo().length != 8 || ro.getAnalog() == null || ro.getAnalog().length != 20) {
            log.info("华体-设置特定通讯地址的集中控制器归属-参数缺失");
            return null;
        }
        JSONObject paramObj = new JSONObject(true);
        paramObj.put("io", ro.getIo());
        paramObj.put("analog", ro.getAnalog());
        //下发命令
        return new HtCommandDTO(paramObj, HtCommandTypeEnum.SET_RELEGATION);
    }

    /**
     * 查询集中控制器下的单灯控制器设备列表
     *
     * @return
     */
    public HtCommandDTO searchSingleLampInfo() {
        //下发命令
        return new HtCommandDTO(null, HtCommandTypeEnum.SEARCH_SINGLE_LAMP_INFO);
    }

    /**
     * 新增、编辑、删除集控器下的单灯控制器列表
     *
     * @param ro
     * @return
     */
    public HtCommandDTO modifySingleLampInfo(HtSingleLampModifyRequestVO ro) {
        if (ro.getDeviceMap() == null || ro.getDeviceMap().isEmpty() || ro.getTypeEnum() == null || HtResultEnum.getMsgByCode(ro.getTypeEnum().code()) == null) {
            log.info("华体-新增、编辑、删除集控器下的单灯控制器列表-参数缺失");
            return null;
        }
        //组装json命令
        JSONObject dataObj = new JSONObject(true);
        JSONArray nodeArr = new JSONArray();
        JSONArray infosArr = new JSONArray();
        JSONObject paramObj = new JSONObject(true);
        //单灯控制器配置信息编辑类型 HtSingleLampModifyTypeEnum
        paramObj.put("kind", ro.getTypeEnum().code());
        Set<Map.Entry<Integer, String>> entries = ro.getDeviceMap().entrySet();
        for (Map.Entry<Integer, String> entry : entries) {
            JSONObject infoObj = new JSONObject(true);
            infoObj.put("index", entry.getKey());
            infoObj.put("address", entry.getValue());
            infosArr.add(infoObj);
        }
        paramObj.put("infos", infosArr);
        nodeArr.add(paramObj);
        dataObj.put("node", nodeArr);
        //下发命令
        return new HtCommandDTO(dataObj, HtCommandTypeEnum.SET_SINGLE_LAMP_INFO);
    }

    /**
     * 查询集中控制器运行数据
     *
     * @return
     */
    public HtCommandDTO searchLampPostRealData() {
        //下发命令
        return new HtCommandDTO(null, HtCommandTypeEnum.SEARCH_LAMP_POST_REAL);
    }

    /**
     * 查询单灯控制器运行数据
     *
     * @return
     */
    public HtCommandDTO searchSingleLampRealData() {
        //下发命令
        return new HtCommandDTO(null, HtCommandTypeEnum.SEARCH_SINGLE_LAMP_REAL);
    }


    /**
     * 查询集中控制器回路策略
     *
     * @return
     */
    public HtCommandDTO searchLampPostLoopPlan() {
        //下发命令
        return new HtCommandDTO(null, HtCommandTypeEnum.SEARCH_LAMP_POST_PLAN);
    }

    /**
     * 设置集中控制器回路策略
     *
     * @param roList 回路策略
     * @return
     */
    public HtCommandDTO setLocationControlLoopPlan(List<HtLampPostPlanRequestVO> roList) {
        if (roList == null || roList.isEmpty()) {
            log.info("华体-设置集中控制器回路策略-参数缺失");
            return null;
        }
        //组装json命令
        JSONObject dataObj = new JSONObject(true);
        JSONObject planObj = new JSONObject(true);
        JSONArray pointArr = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (HtLampPostPlanRequestVO ro : roList) {
            if (ro.getTime() == null || ro.getTypeEnum() == null) {
                log.info("华体-设置集中控制器回路策略-参数time||type缺失");
                return null;
            }
            JSONObject pointObj = new JSONObject(true);
            Date startDate = ro.getStartDate();
            Date endDate = ro.getEndDate();
            //设定时间
            Date timeByPoint = getTimeByPoint(ro.getTime());
            if (timeByPoint == null) {
                continue;
            }
            try {
                if (startDate != null && endDate != null && !isDateRange(startDate, endDate, timeByPoint)) {
                    //设定了开始时间和结束时间,判断是否在区间内
                    continue;
                }
            } catch (ParseException e) {
                log.error("华体-设置集中控制器回路策略-时间转换失败");
                continue;
            }
            pointObj.put("time", sdf.format(timeByPoint));
            pointObj.put("act", ro.getTypeEnum().code());
            pointObj.put("out", new int[]{ro.isLoop1() ? 1 : 0, ro.isLoop2() ? 1 : 0, ro.isLoop3() ? 1 : 0, ro.isLoop4() ? 1 : 0});
            pointArr.add(pointObj);
        }
        planObj.put("point", pointArr);
        dataObj.put("plan", planObj);
        return new HtCommandDTO(dataObj, HtCommandTypeEnum.SET_LAMP_POST_PLAN);
    }

    /**
     * 查询单灯控制器支路输出策略
     */
    public HtCommandDTO searchSingleLampLoopPlan() {
        return new HtCommandDTO(null, HtCommandTypeEnum.SEARCH_SINGLE_LAMP_PLAN);
    }

    /**
     * 设置单灯控制器支路输出策略
     *
     * @param roList 支路输出策略
     * @return
     */
    @Deprecated
    public HtCommandDTO setSingleLampLoopPlan(List<HtSingleLampPlanRequestVO> roList) {
        if (roList == null || roList.isEmpty()) {
            log.info("华体-设置单灯控制器支路输出策略-参数缺失");
            return null;
        }
        //校验数据
        JSONObject dataObj = new JSONObject(true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (HtSingleLampPlanRequestVO ro : roList) {
            if (ro.getNode() == null || ro.getNode().isEmpty() || ro.getPlan() == null || ro.getPlan().isEmpty()) {
                return null;
            }
            for (HtSingleLampPlanChildRequestVo childRo : ro.getPlan()) {
                if (childRo.getTime() == null || childRo.getAct() == null || childRo.getAct().isEmpty()) {
                    log.info("华体-设置单灯控制器支路输出策略-act和time不可为空");
                    return null;
                }
            }
        }
        dataObj.put("plan", JSON.toJSON(roList));
        //下发命令
        return new HtCommandDTO(dataObj, HtCommandTypeEnum.SET_SINGLE_LAMP_PLAN);
    }

    /**
     * 设置单灯控制器支路输出策略-根据时间段和时间点
     *
     * @param roList 支路输出策略
     * @return
     */
    public HtCommandDTO setSingleLampLoopPlanByDateRange(List<HtSingleLampPlanRequestVO> roList) {
        if (roList == null || roList.isEmpty()) {
            log.info("华体-设置单灯控制器支路输出策略-参数缺失");
            return null;
        }
        JSONObject paramObj = new JSONObject(true);
        JSONArray planArr = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (HtSingleLampPlanRequestVO ro : roList) {
            JSONObject planObj = new JSONObject(true);
            if (ro.getNode() == null || ro.getNode().isEmpty() || ro.getPlan() == null || ro.getPlan().isEmpty()) {
                continue;
            }
            planObj.put("node", ro.getNode());
            JSONArray childrenPlanArr = new JSONArray();
            for (HtSingleLampPlanChildRequestVo childRo : ro.getPlan()) {
                JSONObject childrenPlanObj = new JSONObject(true);
                if (childRo.getTime() == null || childRo.getAct() == null || childRo.getAct().isEmpty()) {
                    log.info("华体-设置单灯控制器支路输出策略-act和time不可为空");
                    continue;
                }
                Date startDate = childRo.getStartDate();
                Date endDate = childRo.getEndDate();
                //设定时间
                Date timeByPoint = getTimeByPoint(childRo.getTime());
                if (timeByPoint == null) {
                    continue;
                }
                try {
                    if (startDate != null && endDate != null && !isDateRange(startDate, endDate, timeByPoint)) {
                        //设定了开始时间和结束时间,判断是否在区间内
                        continue;
                    }
                } catch (ParseException e) {
                    log.error("华体-设置单灯控制器支路输出策略-时间转换失败");
                    continue;
                }
                //未设定开始时间和结束时间,则为当天
                childrenPlanObj.put("time", sdf.format(timeByPoint));
                childrenPlanObj.put("act", childRo.getAct());
                childrenPlanArr.add(childrenPlanObj);
            }
            planObj.put("plan", childrenPlanArr);
            planArr.add(planObj);
        }
        paramObj.put("plan", planArr);
        //下发命令
        return new HtCommandDTO(paramObj, HtCommandTypeEnum.SET_SINGLE_LAMP_PLAN);
    }

    /**
     * 获取时间点
     *
     * @return
     */
    private Date getTimeByPoint(Date timePoint) {
        if (timePoint == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, timePoint.getHours());
        c.set(Calendar.MINUTE, timePoint.getMinutes());
        c.set(Calendar.SECOND, timePoint.getSeconds());
        //如果已经过了时间点 设置下一天的
        if (c.getTime().getTime() < System.currentTimeMillis()) {
            c.add(Calendar.DATE, 1);
        }
        return c.getTime();
    }

    /**
     * 判断时间是否在区间内
     *
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @param compareDate 需要判断的时间
     * @return
     */
    private boolean isDateRange(Date startDate, Date endDate, Date compareDate) throws ParseException {
        if (startDate == null || endDate == null) {
            return true;
        }
        Date startTime = longSdf.parse(shortSdf.format(startDate) + " 00:00:00");
        Date endTime = longSdf.parse(shortSdf.format(endDate) + " 23:59:59");
        long startTimeMillis = startTime.getTime();
        long endTimeMillis = endTime.getTime();
        long time = compareDate.getTime();
        return time <= endTimeMillis && time >= startTimeMillis;
    }

    /**
     * 华体集中控制器回路手动输出
     *
     * @param actList 输出类型集合 长度：4
     * @return
     */
    public HtCommandDTO setLocationControlLoopOutPut(List<HtLoopOutputTypeEnum> actList) {
        if (actList == null || actList.isEmpty()) {
            log.info("华体-设置单灯控制器支路输出策略-参数缺失");
            return null;
        }
        //设置拼接参数
        JSONObject dataObj = new JSONObject(true);
        JSONArray actArr = new JSONArray();
        for (HtLoopOutputTypeEnum typeEnum : actList) {
            actArr.add(typeEnum.code());
        }
        dataObj.put("act", actArr);
        return new HtCommandDTO(dataObj, HtCommandTypeEnum.MANUAL_OUTPUT_LAMP_POST);
    }

    /**
     * 单灯控制器手动输出
     *
     * @param nodeList 单灯控制器索引集合
     * @param actList  输出类型集合 长度：3  HtSingleLampBranchOutputEnum
     * @return
     */
    public HtCommandDTO setSingleLampOutput(List<Integer> nodeList, List<Integer> actList) {
        if (nodeList == null || nodeList.isEmpty() || actList == null || actList.isEmpty()) {
            log.info("华体-设置单灯控制器支路输出策略-参数缺失");
            return null;
        }
        //设置拼接参数
        JSONObject dataObj = new JSONObject(true);
        dataObj.put("act", actList);
        dataObj.put("node", nodeList);
        return new HtCommandDTO(dataObj, HtCommandTypeEnum.MANUAL_OUTPUT_SINGLE_LAMP);
    }


    /**
     * 发送请求
     *
     * @param param
     * @param commandType
     * @return
     */
    public String sendRequest(JSONObject param, HtCommandTypeEnum commandType) {
        String result = null;
        String url = commandType.code() == HtCommandTypeEnum.LOGIN.code() ? htLampConfig.getLoginUrl() : htLampConfig.getSendUrl();
        String body = null;
        if (param != null && !param.isEmpty()) {
            body = param.toJSONString();
        }
        Map<String, String> headerMap = new HashMap<>();
        if (commandType.code() != HtCommandTypeEnum.LOGIN.code()) {
            headerMap.put("token", token);
        }
        if ("POST".equals(commandType.requestMethod().toUpperCase())) {
            headerMap.put("Content-Type", "application/json;charset=UTF-8");
            return HttpUtil.post(url, body, headerMap);
        } else if ("GET".equals(commandType.requestMethod().toUpperCase())) {
            return HttpUtil.get(url, param, headerMap);
        }
        return null;
    }
}



