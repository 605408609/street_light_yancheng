package com.exc.street.light.sl.netty.shuncom;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.exc.street.light.resource.dto.sl.shuncom.*;
import com.exc.street.light.resource.enums.sl.shuncom.CommandTypeEnums;
import com.exc.street.light.resource.enums.sl.shuncom.GroupControlStatusEnums;
import com.exc.street.light.resource.enums.sl.shuncom.SettingOperationTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 顺舟控制信息发送工具类
 *
 * @Author: Xiaok
 * @Date: 2020/8/24 9:42
 */
@Slf4j
public class ShuncomMsgSenderUtil {

    /**
     * 对网关和设备进行控制、查询、设备信息的修改等等操作。
     *
     * @param serialNo
     * @param gwDto
     * @return
     */
    public static JSONObject orderControl(Long serialNo, ControlGatewayDTO gwDto) {
        if (!ObjectUtils.allNotNull(serialNo, gwDto, gwDto.getPort()) || StringUtils.isBlank(gwDto.getId())) {
            return null;
        }
        JSONObject orderObj = JSONObject.parseObject(JSONObject.toJSON(gwDto).toString());
        orderObj.put("code", CommandTypeEnums.REPORT_DEVICE_CONTROL.orderCode());
        orderObj.put("headSerial", serialNo);
        return orderObj;
    }

    /**
     * 批量删除网关下面的子设备。
     *
     * @param serialNo          消息序列号 非空
     * @param deviceId          设备ID 非空
     * @param loopDeleteList    控制端口号数组，数组元素为 int，当想删除回路设备中网关的控制端口时必须传。
     * @param collectDeleteList 采集端口号数组，数组元素为 int，当想删除回路设备中网关的控制端口时必须传。
     * @param gwLoopsDeleteList 网关控制端口号数组，数组元素为 int，当想删除回路设备中网关的控制端口时必须传。
     * @return json
     */
    public static JSONObject orderDeleteChildrenDevice(Long serialNo, String deviceId, List<Integer> loopDeleteList, List<Integer> collectDeleteList, List<Integer> gwLoopsDeleteList) {
        if (serialNo == null || StringUtils.isBlank(deviceId)) {
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.ORDER_DEVICE_DELETE.orderCode());
        orderObj.put("id", deviceId);
        orderObj.put("serial", serialNo);
        orderObj.put("loops", loopDeleteList == null ? new ArrayList<Integer>() : loopDeleteList);
        orderObj.put("collects", collectDeleteList == null ? new ArrayList<Integer>() : collectDeleteList);
        orderObj.put("gwloops", gwLoopsDeleteList == null ? new ArrayList<Integer>() : gwLoopsDeleteList);
        return orderObj;
    }

    /**
     * 修改/查询配置
     *
     * @param modifyDto 修改/查询配置参数类
     * @return json
     */
    public static JSONObject orderChangeSetting(SettingModifyDTO modifyDto) {
        if (modifyDto == null || modifyDto.getTypeEnums() == null) {
            return null;
        }
        //SettingOperationTypeEnums.code() == 0 or 1时id不能为空
        if (StringUtils.isBlank(modifyDto.getId()) && (modifyDto.getTypeEnums().equals(SettingOperationTypeEnums.CREATE_OR_MODIFY_MAPPING) || modifyDto.getTypeEnums().equals(SettingOperationTypeEnums.SET_LOOP_DEVICE_MAPPING))) {
            return null;
        }
        //SettingOperationTypeEnums.code() == 0 时maps不能为空
        if (modifyDto.getMap() == null && (modifyDto.getTypeEnums().equals(SettingOperationTypeEnums.CREATE_OR_MODIFY_MAPPING))) {
            return null;
        }
        //SettingOperationTypeEnums.code() == 1 时loops不能为空
        if (modifyDto.getLoops() == null && (modifyDto.getTypeEnums().equals(SettingOperationTypeEnums.SET_LOOP_DEVICE_MAPPING))) {
            return null;
        }
        //SettingOperationTypeEnums.code() == 2 时app不能为空
        if (StringUtils.isBlank(modifyDto.getApp()) && (modifyDto.getTypeEnums().equals(SettingOperationTypeEnums.SEARCH_GATEWAY_LOOP_DEVICE_MAPPING))) {
            return null;
        }
        //SettingOperationTypeEnums.code() == 4 时setDate不能为空
        if (modifyDto.getSetDate() == null && (modifyDto.getTypeEnums().equals(SettingOperationTypeEnums.SEARCH_GATEWAY_VERSION))) {
            return null;
        }
        //SettingOperationTypeEnums.code() == 4 时loops不能为空
        if (modifyDto.getApInfo() == null && (modifyDto.getTypeEnums().equals(SettingOperationTypeEnums.SEARCH_GATEWAY_LAT_AND_LON))) {
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.ORDER_SETTING_SEARCH_OR_MODIFY.orderCode());
        orderObj.put("serial", modifyDto.getSerial());
        orderObj.put("control", modifyDto.getTypeEnums().code());
        orderObj.put("id", modifyDto.getId());
        orderObj.put("maps", modifyDto.getMap());
        orderObj.put("loops", modifyDto.getLoops());
        orderObj.put("app", modifyDto.getApp());
        Long time = modifyDto.getSetDate() != null ? modifyDto.getSetDate().getTime() / 1000L : null;
        orderObj.put("time", time);
        orderObj.put("apinfo", modifyDto.getApInfo());
        orderObj.put("lal", modifyDto.getLal());
        return orderObj;
    }

    /**
     * 删除所有设备
     *
     * @param serialNo 序列号
     * @return json
     */
    public static JSONObject orderDeleteAllChildrenDevice(Long serialNo) {
        if (serialNo == null) {
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.ORDER_DELETE_ALL_DEVICE.orderCode());
        orderObj.put("control", 0);
        orderObj.put("serial", serialNo);
        return orderObj;
    }

    /**
     * 创建分组
     *
     * @param groupId   分组id 非空
     * @param groupName 分组名称 非空
     * @param serialNo  消息序号 非空
     * @param deviceMap 绑定设备map 可为空  key:设备ID，value:设备端口
     * @return json
     */
    public static JSONObject orderCreateGroup(Integer groupId, String groupName, Long serialNo, Map<String, Integer> deviceMap) {
        if (groupId == null || StringUtils.isBlank(groupName) || serialNo == null) {
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.ORDER_GROUP_CREATE.orderCode());
        orderObj.put("gid", groupId);
        orderObj.put("gname", groupName);
        orderObj.put("serial", serialNo);
        JSONArray deviceArr = new JSONArray();
        if (deviceMap != null && !deviceMap.isEmpty()) {
            for (Map.Entry<String, Integer> deviceEntry : deviceMap.entrySet()) {
                JSONObject deviceObj = new JSONObject(true);
                deviceObj.put("id", deviceEntry.getKey());
                deviceObj.put("ep", deviceEntry.getValue());
                deviceArr.add(deviceObj);
            }
        }
        orderObj.put("device", deviceArr);
        return orderObj;
    }

    /**
     * 创建分组
     *
     * @param groupId         分组id 非空
     * @param groupName       分组名称 可为空
     * @param serialNo        消息序号 非空
     * @param deviceAddMap    新增设备map 可为空  key:设备ID，value:设备端口
     * @param deviceDeleteMap 删除设备map 可为空  key:设备ID，value:设备端口
     * @return json
     */
    public static JSONObject orderModifyGroup(Integer groupId, String groupName, Long serialNo, Map<String, Integer> deviceAddMap, Map<String, Integer> deviceDeleteMap) {
        if (groupId == null || serialNo == null) {
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.ORDER_GROUP_MODIFY.orderCode());
        orderObj.put("gid", groupId);
        if (groupName != null) {
            orderObj.put("gname", groupName);
        }
        orderObj.put("serial", serialNo);
        JSONArray deviceArr = new JSONArray();
        if (deviceAddMap != null && !deviceAddMap.isEmpty()) {
            for (Map.Entry<String, Integer> deviceEntry : deviceAddMap.entrySet()) {
                JSONObject deviceObj = new JSONObject(true);
                deviceObj.put("id", deviceEntry.getKey());
                deviceObj.put("ep", deviceEntry.getValue());
                deviceObj.put("status", 0);
                deviceArr.add(deviceObj);
            }
        }
        if (deviceDeleteMap != null && !deviceDeleteMap.isEmpty()) {
            for (Map.Entry<String, Integer> deviceEntry : deviceDeleteMap.entrySet()) {
                JSONObject deviceObj = new JSONObject(true);
                deviceObj.put("id", deviceEntry.getKey());
                deviceObj.put("ep", deviceEntry.getValue());
                deviceObj.put("status", 1);
                deviceArr.add(deviceObj);
            }
        }
        if (!deviceArr.isEmpty()) {
            orderObj.put("device", deviceArr);
        }
        return orderObj;
    }

    /**
     * 设置分组状态请求命令
     *
     * @param groupId          分组ID
     * @param serialNo         消息序号
     * @param controlType      组控命令类型 null:由应用程序自身决定是用硬件方式组控还是软件方式,1:硬件组控,其他值:软件组控
     * @param controlStatusMap 参数map,key:com.exc.street.light.resource.enums.sl.shuncom.GroupControlStatusEnums.code(),value:对应类型的值
     * @return json
     */
    public static JSONObject orderSetGroupStatus(Integer groupId, Long serialNo, Integer controlType, Map<String, Object> controlStatusMap) {
        if (groupId == null || serialNo == null) {
            return null;
        }
        //构建返回json
        JSONObject orderObj = new JSONObject(true);
        JSONObject controlObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.ORDER_GROUP_REQUEST_SETTING.orderCode());
        orderObj.put("serial", serialNo);
        orderObj.put("gid", groupId);
        orderObj.put("ctrltype", controlType);
        if (controlStatusMap != null && !controlStatusMap.isEmpty()) {
            for (Map.Entry<String, Object> controlStatusEntry : controlStatusMap.entrySet()) {
                String key = controlStatusEntry.getKey();
                Object value = controlStatusEntry.getValue();
                if (GroupControlStatusEnums.getByCode(key) == null) {
                    //未定义的key
                    continue;
                }
                controlObj.put(key, value);
            }
        }
        orderObj.put("control", controlObj);
        return orderObj;
    }

    /**
     * 删除分组
     *
     * @param serialNo 消息序号
     * @param groupId  分组ID
     * @return json
     */
    public static JSONObject orderGroupDelete(Long serialNo, Integer groupId) {
        if (serialNo == null || groupId == null) {
            log.error("顺舟云盒-云平台删除分组失败，参数不全");
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.REPORT_GROUP_DELETE.orderCode());
        orderObj.put("gid", groupId);
        orderObj.put("serial", serialNo);
        return orderObj;
    }

    /**
     * 获取分组信息
     *
     * @param serialNo    序列号
     * @param groupIdList 要查询的分组ID集合，为空则查询全部
     * @return 下发json
     */
    public static JSONObject orderGetGroupInfo(Long serialNo, List<Integer> groupIdList) {
        if (serialNo == null) {
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("serial", serialNo);
        orderObj.put("code", CommandTypeEnums.ORDER_GET_GROUP_INFO.orderCode());
        if (groupIdList != null && !groupIdList.isEmpty()) {
            orderObj.put("gids", groupIdList);
        }
        return orderObj;
    }

    /**
     * 删除所有分组
     *
     * @param serialNo 消息序号
     * @return 下发的json
     */
    public static JSONObject orderDeleteAllGroup(Long serialNo) {
        if (serialNo == null) {
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.ORDER_GROUP_DELETE_ALL.orderCode());
        orderObj.put("control", 0);
        orderObj.put("serial", serialNo);
        return orderObj;
    }

    /**
     * 创建/修改 策略
     *
     * @param serialNo        消息序号
     * @param strategyInfoDto 策略信息实体类
     * @return 下发的json
     */
    public static JSONObject orderCreateOrModifyStrategy(Long serialNo, StrategyInfoDTO strategyInfoDto) {
        if (strategyInfoDto == null || serialNo == null) {
            return null;
        }
        //数据校验
        if (strategyInfoDto.getRid() == null || strategyInfoDto.getState() == null || strategyInfoDto.getName() == null
                || strategyInfoDto.getCreateTime() == null || strategyInfoDto.getTriggerTimes() == null) {
            return null;
        }
        JSONObject orderObj = null;
        JSONArray conditionArr = new JSONArray();
        JSONArray actArr = new JSONArray();
        try {
            orderObj = JSONObject.parseObject(JSONObject.toJSONString(strategyInfoDto), Feature.OrderedField);
            orderObj.put("serial", serialNo);
            orderObj.put("code", CommandTypeEnums.ORDER_STRATEGY_ADD_OR_MODIFY.orderCode());

            //拼接exp条件
            StringBuilder exp = new StringBuilder("function main(");
            if (strategyInfoDto.getConditionList() != null && !strategyInfoDto.getConditionList().isEmpty()) {
                for (int i = 1; i <= strategyInfoDto.getConditionList().size() * 2; i++) {
                    exp.append("a").append(i);
                    if (i != strategyInfoDto.getConditionList().size() * 2) {
                        exp.append(",");
                    }
                }
                exp.append(") ").append("if(");
                for (int i = 1; i <= strategyInfoDto.getConditionList().size(); i++) {
                    exp.append("(").append("a").append(i * 2 - 1).append(" == ").append("a").append(i * 2).append(")");
                    if (i != strategyInfoDto.getConditionList().size()) {
                        exp.append(" and ");
                    }
                }
                exp.append(") then return true else return false end end");
                for (StrategyConditionDTO condition : strategyInfoDto.getConditionList()) {
                    if (condition.getIndex() == null || condition.getConditionType() == null) {
                        log.info("策略条件-条件序号不可为空");
                        return null;
                    }
                    if (Arrays.asList(1, 3).contains(condition.getConditionType()) && condition.getTimeCorn() == null) {
                        log.info("策略条件-策略执行时间不可为空");
                        return null;
                    }
                    if (condition.getConditionType().equals(2) && (condition.getDeviceId() == null || condition.getEp() == null
                            || condition.getCmd() == null || condition.getValue() == null)) {
                        log.info("策略条件-设备id、端口、操作命令类型，对应值不可为空");
                        return null;
                    }
                    JSONObject conditionObj = JSONObject.parseObject(JSONObject.toJSONString(condition));
                    conditionArr.add(conditionObj);
                }
            } else {
                exp = new StringBuilder("function main() return true");
            }
            orderObj.put("exp", exp.toString());
            if (strategyInfoDto.getActList() != null) {
                for (StrategyActDTO act : strategyInfoDto.getActList()) {
                    if (act.getIndex() == null || act.getDelay() == null || act.getTargetType() == null || act.getCmd() == null || act.getValue() == null) {
                        log.info("策略动作-参数校验失败1");
                        return null;
                    }
                    if ((act.getTargetType().equals(1) || act.getTargetType().equals(3)) && act.getId() == null) {
                        log.info("策略动作-参数校验失败2");
                        return null;
                    }
                    if (act.getTargetType().equals(2) && act.getGid() == null) {
                        log.info("策略动作-分组ID不可为空");
                        return null;
                    }
                    if (act.getTargetType().equals(4) && act.getRid() == null) {
                        log.info("策略动作-rule id不可为空");
                        return null;
                    }
                    JSONObject actObj = JSONObject.parseObject(JSONObject.toJSONString(act));
                    if (act.getTargetType().equals(3)) {
                        int id = Integer.parseInt(act.getId());
                        actObj.put("id", id);
                    }
                    actArr.add(actObj);
                }
            }
        } catch (Exception e) {
            log.error("下发创建/修改策略指令错误,errorMsg={}", e.getMessage());
            return null;
        }
        orderObj.put("cond", conditionArr);
        orderObj.put("act", actArr);
        return orderObj;
    }

    /**
     * 设置策略使能
     *
     * @param serialNo 消息序号
     * @param rid      策略ID
     * @param state    使能状态 0：disable 1：enable
     * @return 下发的json
     */
    public static JSONObject orderStrategySetState(Long serialNo, Integer rid, Integer state) {
        if (serialNo == null || rid == null || state == null) {
            log.error("策略使能设置-参数校验失败");
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.ORDER_STRATEGY_SET_STATE.orderCode());
        orderObj.put("rid", rid);
        orderObj.put("serial", serialNo);
        orderObj.put("state", state);
        return orderObj;
    }

    /**
     * 删除策略
     *
     * @param serialNo 消息序号
     * @param rid      策略ID
     * @return 下发的json
     */
    public static JSONObject orderStrategyDelete(Long serialNo, Integer rid) {
        if (serialNo == null || rid == null) {
            log.error("策略删除-参数校验失败");
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.ORDER_STRATEGY_DELETE.orderCode());
        orderObj.put("rid", rid);
        orderObj.put("serial", serialNo);
        return orderObj;
    }

    /**
     * 查询策略信息列表
     *
     * @param serialNo 消息序号
     * @param rids     需查询的策略ID集合 为空查询全部
     * @return 下发的json
     */
    public static JSONObject orderStrategyGetInfo(Long serialNo, List<Integer> rids) {
        if (serialNo == null) {
            return null;
        }
        JSONObject orderObj = new JSONObject(true);
        orderObj.put("code", CommandTypeEnums.ORDER_STRATEGY_GET_INFO.orderCode());
        orderObj.put("serial", serialNo);
        if (rids != null && !rids.isEmpty()) {
            orderObj.put("rids", rids);
        }
        return orderObj;
    }
}
