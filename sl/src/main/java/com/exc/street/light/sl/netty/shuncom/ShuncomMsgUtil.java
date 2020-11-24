package com.exc.street.light.sl.netty.shuncom;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.dto.sl.shuncom.*;
import com.exc.street.light.resource.enums.sl.shuncom.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 顺舟云盒数据解析类
 *
 * @author xiaok
 * @version 1.0
 * @date 2020-08-20
 */
@Slf4j
public class ShuncomMsgUtil {


    /**
     * header长度
     */
    static final int HEADER_LENGTH = 10;

    /**
     * 起始头
     */
    static final String START_HEAD_STR = "AA55";

    public static void main(String[] args) {
        //-86, 85, 0, 55, 4, 0, 2, 0, 73, 64,
        // 123, 34, 105, 100, 34, 58, 34, 48, 48, 102, 102, 50, 99, 50, 99, 50, 99, 54, 97, 54, 102, 48, 48, 56, 49, 102, 55, 34, 44, 34, 109, 97, 99, 34, 58, 34, 50, 99, 58, 54, 97, 58, 54, 102, 58, 48, 48, 58, 56, 49, 58, 102, 55, 34, 125
        byte[] payload = new byte[]{123, 34, 105, 100, 34, 58, 34, 48, 48, 102, 102, 50, 99, 50, 99, 50, 99, 54, 97, 54, 102, 48, 48, 56, 49, 102, 55, 34, 44, 34, 109, 97, 99, 34, 58, 34, 50, 99, 58, 54, 97, 58, 54, 102, 58, 48, 48, 58, 56, 49, 58, 102, 55, 34, 125};
    }

    /**
     * 顺舟云盒自定义的crc16校验
     *
     * @param bytes payload的字节数组
     * @return crc校验值
     */
    public static int crc16(byte[] bytes) {
        int crc = 0xFFFF;
        for (byte aByte : bytes) {
            crc = ((crc >> 8) | (crc << 8));
            crc ^= (aByte & 0x00FF);
            crc = crc & 0xFFFF;
            crc ^= (((crc & 0xFF) >> 4) & 0x00FF);
            crc = crc & 0xFFFF;
            crc ^= crc << 12;
            crc = crc & 0xFFFF;
            crc ^= (crc & 0xFF) << 5;
            crc = crc & 0xFFFF;
        }
        //高位HexString
        //String highHexString = Integer.toString((crc >> 8 & 0x0FF), 16);
        //低位HexString
        //String lowHexString = Integer.toString((crc & 0x0FF), 16);
        return crc;
    }


    /**
     * 顺舟云盒消息 校验方法
     *
     * @param reqBytes
     * @return
     */
    public static boolean validation(byte[] reqBytes) {
        //不带header或小于起始头长度 的数据进行过滤
        if (reqBytes == null || reqBytes.length < HEADER_LENGTH) {
            return false;
        }
        try {
            //判断起始头
            byte[] startHead = new byte[2];
            System.arraycopy(reqBytes, 0, startHead, 0, 2);
            String startHeadStr = HexUtil.encodeHexStr(startHead).toUpperCase();
            if (!START_HEAD_STR.equals(startHeadStr)) {
                return false;
            }
            //获取payload length
            byte[] payloadLengthBytes = new byte[2];
            System.arraycopy(reqBytes, 2, payloadLengthBytes, 0, 2);
            //json消息长度
            int payloadLength = Integer.parseInt(HexUtil.encodeHexStr(payloadLengthBytes), 16);
            //payload长度不够
            if (reqBytes.length - HEADER_LENGTH != payloadLength) {
                return false;
            }
            //获取payload
            byte[] payload = new byte[payloadLength];
            System.arraycopy(reqBytes, 10, payload, 0, payloadLength);
            log.debug("payload:{}", reqBytes);
            //获取header中的crc值
            byte[] crcBytes = new byte[2];
            System.arraycopy(reqBytes, 8, crcBytes, 0, 2);
            int crcHeaderCode = Integer.parseInt(HexUtil.encodeHexStr(crcBytes), 16);
            //根据payload计算crc
            int crcCode = crc16(payload);
            //比对crc
            return crcCode == crcHeaderCode;
        } catch (Exception e) {
            log.info("消息校验失败,msg={},reqBytes = {}", e.getMessage(), reqBytes);
            return false;
        }
    }

    /**
     * 给消息添加包头并合并
     *
     * @param payloadObj 要封装的消息json
     * @return 发送的byte数组
     */
    public static byte[] addHeader(JSONObject payloadObj) throws UnsupportedEncodingException {
        String payload = payloadObj.toJSONString();
        byte[] payloadBytes = payload.getBytes();
        //crc计算值（针对payload计算）
        int crcVal = crc16(payloadBytes);

        //特殊标识
        byte[] magic = HexUtil.decodeHex(START_HEAD_STR);
        //payload 区间的长度
        byte[] payloadLength = HexUtil.decodeHex(com.exc.street.light.resource.utils.HexUtil.padLeft(Integer.toHexString(payloadBytes.length), 4, '0'));
        //版本 默认为4
        byte[] version = HexUtil.decodeHex("04");
        //数据包加密类型 默认为0(不加密)
        byte[] enctype = HexUtil.decodeHex("00");
        //数据包类型 默认为1(json)
        byte[] type = HexUtil.decodeHex("01");
        //暂时保留
        byte[] reserved = HexUtil.decodeHex("00");
        //crc的byte数组
        byte[] crc = HexUtil.decodeHex(com.exc.street.light.resource.utils.HexUtil.padLeft(Integer.toHexString(crcVal), 4, '0'));
        //合并字节数组
        return ArrayUtil.addAll(magic, payloadLength, version, enctype, type, reserved, crc, payloadBytes);
    }

    /**
     * 心跳包处理
     *
     * @param dataObj
     * @return
     */
    public static JSONObject reportHeartBeatHandle(JSONObject dataObj) {
        //网关信息
        JSONObject gwObj = dataObj.getJSONObject("gw");
        if (gwObj != null) {
            //网关信息dto
            try {
                GatewayInfoDTO gatewayInfo = JSONObject.toJavaObject(gwObj, GatewayInfoDTO.class);
                log.info("网关信息:{}", gatewayInfo);
            } catch (Exception e) {
                log.error("心跳包-网关信息转换失败,errMsg={}", e.getMessage());
            }
        }
        //设备信息数组
        JSONArray deviceArr = dataObj.getJSONArray("device");
        if (deviceArr != null && !deviceArr.isEmpty()) {
            for (int i = 0; i < deviceArr.size(); i++) {
                JSONObject deviceObj = deviceArr.getJSONObject(i);
                if (deviceObj == null || deviceObj.isEmpty()) {
                    continue;
                }
                //设备基础信息
                DeviceInfoDTO deviceInfoDTO = JSONObject.toJavaObject(deviceObj, DeviceInfoDTO.class);
                //设备扩展信息
                JSONObject stObj = deviceObj.getJSONObject("st");
                if (stObj == null || stObj.isEmpty()) {
                    continue;
                }
                for (Map.Entry<String, Object> entry : stObj.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    String valueStr = null;
                    DeviceStInfoEnums stInfoEnums = DeviceStInfoEnums.getByCode(key);
                    if (stInfoEnums == null) {
                        continue;
                    }
                    String dataType = stInfoEnums.getDataType();
                    if (value != null) {
                        valueStr = value.toString();
                    }
                    //TODO 对key和valueStr进行保存处理
                }
            }
        }
        JSONObject returnObj = new JSONObject(true);
        returnObj.put("code", CommandTypeEnums.REPORT_HEART_BEAT.orderCode());
        returnObj.put("result", ResultEnums.SUCCESS.code());
        returnObj.put("timestamp", System.currentTimeMillis() / 1000L);
        return returnObj;
    }

    /**
     * 平台下发控制设备命令 返回的消息处理
     */
    public static void reportControlDevice(JSONObject reportObj) {
        if (reportObj == null) {
            return;
        }
        //设备id
        String id = reportObj.getString("id");
        //端口号
        Integer ep = reportObj.getInteger("ep");
        //消息序列号
        Long serial = reportObj.getLong("serial");
        JSONObject control = reportObj.getJSONObject("control");
        if (control != null) {
            //开关状态
            Boolean onStatus = control.getBoolean("on");
        }
        Integer result = reportObj.getInteger("result");
        //TODO 进行处理

    }

    /**
     * 平台下发设备删除命令 返回的消息处理
     *
     * @param reportObj
     */
    public static void reportDeviceDelete(JSONObject reportObj) {
        if (reportObj != null) {
            return;
        }
        //设备id
        String id = reportObj.getString("id");
        //控制端口号数组
        JSONArray loops = reportObj.getJSONArray("loops");
        if (loops != null) {
            for (int i = 0; i < loops.size(); i++) {
                Integer loop = loops.getInteger(i);
            }
        }
        //采集端口号数组
        JSONArray collects = reportObj.getJSONArray("collects");
        if (collects != null) {
            for (int i = 0; i < collects.size(); i++) {
                //采集端口号
                Integer collectEp = collects.getInteger(i);
            }
        }
        //网关控制端口号数组
        JSONArray gwLoops = reportObj.getJSONArray("gwloops");
        if (gwLoops != null) {
            for (int i = 0; i < gwLoops.size(); i++) {
                //网关控制端口号
                Integer gwLoop = gwLoops.getInteger(i);
            }
        }
        //序号
        Long serial = reportObj.getLong("serial");
        //结果
        Integer result = reportObj.getInteger("result");
        //TODO 进行处理
        if (result != null && result.equals(0)) {

        }
    }

    /**
     * 获取设备上报信息返回json
     *
     * @param code             消息类型
     * @param controlType      控制类型
     * @param zigBeeCenterAddr zigbee中心id
     * @param result           结果
     * @return
     */
    private static JSONObject getReportDeviceInfoHandleResult(int code, Integer controlType, int result, String zigBeeCenterAddr) {
        JSONObject resultObj = new JSONObject(true);
        resultObj.put("code", code);
        resultObj.put("control", controlType);
        if (StringUtils.isNotBlank(zigBeeCenterAddr)) {
            resultObj.put("id", zigBeeCenterAddr);
        }
        resultObj.put("result", result);
        //TODO 进行处理
        return resultObj;
    }

    /**
     * 对上报网关信息、上报设备状态改变、子设备添加上报、上报设备告警 进行处理
     *
     * @param reportObj
     * @return
     */
    public static JSONObject reportDeviceInfoHandle(JSONObject reportObj) {
        if (reportObj == null || reportObj.getInteger("control") == null) {
            return null;
        }
        Integer controlType = reportObj.getInteger("control");
        if (controlType.equals(0)) {
            //消息类型为 上报网关信息
            return reportGatewayInfo(reportObj);
        } else if (controlType.equals(2)) {
            //消息类型为 上报设备状态改变
            return reportDeviceStatusChange(reportObj);
        } else if (controlType.equals(4)) {
            //消息类型为 子设备添加
            return reportDeviceAdd(reportObj);
        } else if (controlType.equals(5)) {
            //消息类型为 子设备告警
            return reportDeviceAlarm(reportObj);
        }
        return null;
    }

    /**
     * 解析上报网关信息 104 control:0
     *
     * @return
     */
    private static JSONObject reportGatewayInfo(JSONObject reportObj) {
        //zigbee中心地址
        String zigbeeCenterId = reportObj.getString("id");
        //网关mac地址
        String gatewayMac = reportObj.getString("mac");
        //序列码
        Long serial = reportObj.getLong("serial");
        return getReportDeviceInfoHandleResult(CommandTypeEnums.REPORT_DEVICE_INFO.orderCode(), 0, ResultEnums.SUCCESS.code(), zigbeeCenterId);
    }

    /**
     * 上报设备状态改变 104 control:2
     *
     * @param reportObj
     * @return
     */
    private static JSONObject reportDeviceStatusChange(JSONObject reportObj) {
        //转换为java对象
        DeviceStatusChangeDTO deviceStatusChangeDTO = JSONObject.toJavaObject(reportObj, DeviceStatusChangeDTO.class);
        //设备状态信息
        if (reportObj.getJSONObject("st") != null) {
            JSONObject stObj = reportObj.getJSONObject("st");
            for (Map.Entry<String, Object> stEntry : stObj.entrySet()) {
                String key = stEntry.getKey();
                Object value = stEntry.getValue();
                DeviceStatusEnums deviceStatusEnum = DeviceStatusEnums.getByCode(key);
                if (deviceStatusEnum == null) {
                    continue;
                }
                //todo key和value 入库
            }
        }
        JSONObject returnObj = new JSONObject(true);
        returnObj.put("code", CommandTypeEnums.REPORT_DEVICE_INFO.orderCode());
        returnObj.put("control", 2);
        returnObj.put("id", deviceStatusChangeDTO.getId());
        returnObj.put("ep", deviceStatusChangeDTO.getEp());
        returnObj.put("result", ResultEnums.SUCCESS.code());
        return returnObj;
    }

    /**
     * 上报设备状态添加 104 control:4
     *
     * @param reportObj
     * @return
     */
    private static JSONObject reportDeviceAdd(JSONObject reportObj) {
        //转换为java对象
        DeviceStatusChangeDTO deviceStatusChangeDTO = JSONObject.toJavaObject(reportObj, DeviceStatusChangeDTO.class);
        //设备状态信息
        if (reportObj.getJSONObject("st") != null) {
            JSONObject stObj = reportObj.getJSONObject("st");
            for (Map.Entry<String, Object> stEntry : stObj.entrySet()) {
                String key = stEntry.getKey();
                Object value = stEntry.getValue();
                DeviceStatusEnums deviceStatusEnum = DeviceStatusEnums.getByCode(key);
                if (deviceStatusEnum == null) {
                    continue;
                }
                //todo key和value 入库
            }
        }
        JSONObject returnObj = new JSONObject(true);
        returnObj.put("code", CommandTypeEnums.REPORT_DEVICE_INFO.orderCode());
        returnObj.put("control", 4);
        if (deviceStatusChangeDTO.getPid() != null) {
            returnObj.put("pid", deviceStatusChangeDTO.getPid());
        }
        if (deviceStatusChangeDTO.getDid() != null) {
            returnObj.put("did", deviceStatusChangeDTO.getDid());
        }
        returnObj.put("id", deviceStatusChangeDTO.getId());
        returnObj.put("ep", deviceStatusChangeDTO.getEp());
        returnObj.put("result", ResultEnums.SUCCESS.code());
        return returnObj;
    }

    /**
     * √上报设备状态添加 104 control:5
     *
     * @param reportObj 接收消息
     * @return 返回json
     */
    private static JSONObject reportDeviceAlarm(JSONObject reportObj) {
        //转换为java对象
        DeviceStatusChangeDTO deviceStatusChangeDTO = JSONObject.toJavaObject(reportObj, DeviceStatusChangeDTO.class);
        //设备状态信息
        if (reportObj.getJSONObject("st") != null) {
            JSONObject stObj = reportObj.getJSONObject("st");
            for (Map.Entry<String, Object> stEntry : stObj.entrySet()) {
                String key = stEntry.getKey();
                Object value = stEntry.getValue();
                if (value == null) {
                    continue;
                }
                //告警类型
                if ("alarmType".equals(key)) {
                    int alarmType = Integer.parseInt(value.toString());
                    //获取告警类型  0无告警
                    DeviceAlarmTypeEnums deviceAlarmTypeEnums = DeviceAlarmTypeEnums.getByCode(alarmType);
                    if (deviceAlarmTypeEnums == null) {
                        //找不到对应告警类型
                    } else if (deviceAlarmTypeEnums.equals(DeviceAlarmTypeEnums.NORMAL)) {
                        //正常
                    } else {
                        //告警
                    }
                }
                //实时值 BigDecimal
                if ("realValue".equals(key)) {
                    BigDecimal realValue = new BigDecimal(value.toString());
                }
                //限定值 BigDecimal
                if ("limitValue".equals(key)) {
                    BigDecimal limitValue = new BigDecimal(value.toString());
                }
            }
        }
        JSONObject returnObj = new JSONObject(true);
        returnObj.put("code", CommandTypeEnums.REPORT_DEVICE_INFO.orderCode());
        returnObj.put("id", deviceStatusChangeDTO.getId());
        returnObj.put("control", 5);
        returnObj.put("ep", deviceStatusChangeDTO.getEp());
        returnObj.put("serial", deviceStatusChangeDTO.getSerial());
        returnObj.put("result", ResultEnums.SUCCESS.code());
        return returnObj;
    }

    /**
     * 新设备注册消息处理
     *
     * @param reportObj 接收消息
     * @return 返回json
     */
    public static JSONObject reportNewDeviceRegister(JSONObject reportObj) {
        //新设备注册DTO
        DeviceRegisterDTO deviceRegisterDTO = reportObj.toJavaObject(DeviceRegisterDTO.class);
        //设备所有端口的顺舟设备类型
        JSONArray dTypesArr = reportObj.getJSONArray("dtypes");
        if (dTypesArr != null) {
            for (int i = 0; i < dTypesArr.size(); i++) {
                JSONObject dTypeObj = dTypesArr.getJSONObject(i);
                //端口号
                Integer ep = dTypeObj.getInteger("ep");
                //对应的设备类型
                Integer dType = dTypeObj.getInteger("dtype");
            }
        }
        //扩展信息
        JSONObject stInfo = reportObj.getJSONObject("st");
        if (stInfo != null) {
            //顺舟设备类型
            String dsp = stInfo.getString("dsp");
            //设备所有端口的列表，例如设备有 1、2、3 端 口 ， eplist 为“0010002003”，端口列表没有顺序，也可以为“002001003”
            String epList = stInfo.getString("eplist");
            if (StringUtils.isNotBlank(epList) && epList.length() % 3 == 0) {

            }
        }
        //构造返回消息
        JSONObject returnObj = new JSONObject(true);
        returnObj.put("code", CommandTypeEnums.REPORT_DEVICE_REGISTER.orderCode());
        JSONObject deviceObj = new JSONObject(true);
        deviceObj.put("id", deviceRegisterDTO.getId());
        deviceObj.put("control", 0);
        JSONArray checkListArr = new JSONArray();
        checkListArr.add(deviceObj);
        returnObj.put("check_list", checkListArr);
        returnObj.put("result", ResultEnums.SUCCESS.code());
        return returnObj;
    }


    /**
     * 平台下发 修改/查询配置命令后，返回的消息处理
     *
     * @param reportObj 返回消息
     */
    public static void reportSettingSearchOrModify(JSONObject reportObj) {
        //返回结果类型见ResultEnums
        Integer result = reportObj.getInteger("result");
        //设定类型见SettingOperationTypeEnums
        Integer control = reportObj.getInteger("control");
        //其他属性见 SettingModifyDTO
        Long serial = reportObj.getLong("serial");
        String id = reportObj.getString("id");
        JSONArray maps = reportObj.getJSONArray("maps");
        JSONArray loops = reportObj.getJSONArray("loops");
        String app = reportObj.getString("app");
        JSONObject apInfo = reportObj.getJSONObject("apinfo");
        Long time = reportObj.getLong("time");
        String lal = reportObj.getString("lal");
        //TODO 处理
    }

    /**
     * 平台下发 删除所有子设备命令后，返回的消息处理
     *
     * @param reportObj 返回消息
     */
    public static void reportDeleteAllChildrenDevice(JSONObject reportObj) {
        //序列号
        Long serial = reportObj.getLong("serial");
        //网关处理结果  见ResultEnums
        Integer result = reportObj.getInteger("result");
        //TODO 处理
    }

    /**
     * 平台下发创建分组命令后，对返回的消息进行处理
     *
     * @param reportObj 返回消息
     */
    public static void reportCreateGroup(JSONObject reportObj) {
        //返回结果  见ResultEnums
        Integer result = reportObj.getInteger("result");
        //组ID
        Integer groupId = reportObj.getInteger("gid");
        //组名称
        String groupName = reportObj.getString("gname");
        //序号
        Long serial = reportObj.getLong("serial");
        //设备绑定结果数组
        JSONArray deviceArr = reportObj.getJSONArray("device");
        if (deviceArr != null && !deviceArr.isEmpty()) {
            for (int i = 0; i < deviceArr.size(); i++) {
                JSONObject deviceObj = deviceArr.getJSONObject(i);
                //设备ID
                String deviceId = deviceObj.getString("id");
                //设备端口
                Integer deviceEp = deviceObj.getInteger("ep");
                //是否加入成功
                Boolean status = deviceObj.getBoolean("status");
            }
        }
        //todo 处理
    }

    /**
     * 平台下发修改分组命令后，对返回的消息进行处理
     *
     * @param reportObj 返回消息
     */
    public static void reportModifyGroup(JSONObject reportObj) {
        //返回结果  见ResultEnums
        Integer result = reportObj.getInteger("result");
        //序号
        Long serial = reportObj.getLong("serial");
        //组ID
        Integer groupId = reportObj.getInteger("gid");
        //组名 不为null表明进行了修改
        String groupName = reportObj.getString("gname");
        //设备列表 不为null表明进行了修改
        JSONArray deviceArr = reportObj.getJSONArray("device");
        if (deviceArr != null) {
            for (int i = 0; i < deviceArr.size(); i++) {
                JSONObject deviceObj = deviceArr.getJSONObject(i);
                //设备ID
                String deviceId = deviceObj.getString("id");
                //设备端口
                Integer deviceEp = deviceObj.getInteger("ep");
                //0：增加分组成员；1：删除分组成员
                Integer status = deviceObj.getInteger("status");
            }
        }
        //TODO 进行处理
    }


    /**
     * 平台下发设置分组状态请求命令后，对返回的消息进行处理
     *
     * @param reportObj 返回消息
     */
    public static void reportSetGroupStatus(JSONObject reportObj) {
        //返回结果  见ResultEnums
        Integer result = reportObj.getInteger("result");
        //序号
        Long serial = reportObj.getLong("serial");
        //组ID
        Integer groupId = reportObj.getInteger("gid");
        //组控命令类型 null:由应用程序自身决定是用硬件方式组控还是软件方式,1:硬件组控,其他值:软件组控
        Integer controlType = reportObj.getInteger("ctrltype");
        //控制参数
        JSONObject controlObj = reportObj.getJSONObject("control");
        if (controlObj != null) {
            for (Map.Entry<String, Object> controlObjEntry : controlObj.entrySet()) {
                String key = controlObjEntry.getKey();
                Object value = controlObjEntry.getValue();
            }
        }
        //TODO 进行处理
    }

    /**
     * 平台下发删除分组命令后，对返回的消息进行处理
     *
     * @param reportObj 返回消息
     */
    public static void reportGroupDelete(JSONObject reportObj) {
        //返回结果  见ResultEnums
        Integer result = reportObj.getInteger("result");
        //组ID
        Integer groupId = reportObj.getInteger("gid");
        //消息序号
        Long serial = reportObj.getLong("serial");
        //TODO 进行处理
    }

    /**
     * 网关上报删除分组消息,网关修改分组上报,网关创建分组上报  消息处理
     *
     * @param reportObj 上报json
     * @return 返回json
     */
    public static JSONObject reportGroupDeleteUpload(JSONObject reportObj) {
        //控制类型
        Integer control = reportObj.getInteger("control");
        if (control == null) {
            return null;
        }
        if (control.equals(0)) {
            //上报消息类型：删除分组
            return reportGroupDeleteUploadDelete(reportObj);
        } else if (control.equals(1)) {
            //上报消息类型：修改分组
            return reportGroupDeleteUploadModify(reportObj);
        } else if (control.equals(2)) {
            //上报消息类型：创建分组
            return reportGroupDeleteUploadCreate(reportObj);
        }
        return null;
    }

    /**
     * 网关上报删除分组消息 TODO
     *
     * @param reportObj 上报json
     * @return 返回json
     */
    private static JSONObject reportGroupDeleteUploadDelete(JSONObject reportObj) {
        //删除的分组ID
        Integer groupId = reportObj.getInteger("gid");
        JSONObject resultObj = new JSONObject(true);
        resultObj.put("code", CommandTypeEnums.REPORT_GROUP_DELETE.orderCode());
        resultObj.put("gid", groupId);
        resultObj.put("control", 0);
        resultObj.put("result", ResultEnums.SUCCESS.code());
        return resultObj;
    }

    /**
     * 网关修改分组上报  TODO
     *
     * @param reportObj 上报json
     * @return 返回json
     */
    private static JSONObject reportGroupDeleteUploadModify(JSONObject reportObj) {
        //分组ID
        Integer groupId = reportObj.getInteger("gid");
        //分组名称  不为null表示被修改
        String groupName = reportObj.getString("gname");
        //设备数组
        JSONArray deviceArr = reportObj.getJSONArray("device");
        if (deviceArr != null) {
            //不为null表明被修改过了
            for (int i = 0; i < deviceArr.size(); i++) {
                JSONObject deviceObj = deviceArr.getJSONObject(i);
                //设备ID
                String id = deviceObj.getString("id");
                //设备端口
                Integer ep = deviceObj.getInteger("ep");
                //分组成员类型。1：硬件分组；2：软件分组
                Integer mType = deviceObj.getInteger("mtype");
            }
        }
        JSONObject gstObj = reportObj.getJSONObject("gst");
        if (gstObj != null) {
            //开关状态
            Boolean on = gstObj.getBoolean("on");
            Integer bri = gstObj.getInteger("bri");
            Integer sat = gstObj.getInteger("sat");
            Integer hue = gstObj.getInteger("hue");
            Integer ctp = gstObj.getInteger("ctp");
        }
        JSONObject resultObj = new JSONObject(true);
        resultObj.put("code", CommandTypeEnums.REPORT_GROUP_DELETE.orderCode());
        resultObj.put("gid", groupId);
        resultObj.put("control", 1);
        resultObj.put("result", ResultEnums.SUCCESS.code());
        return resultObj;
    }

    /**
     * 网关上报创建分组消息 TODO
     *
     * @param reportObj 上报json
     * @return 返回json
     */
    private static JSONObject reportGroupDeleteUploadCreate(JSONObject reportObj) {
        //分组ID
        Integer groupId = reportObj.getInteger("gid");
        //设备数组
        JSONArray deviceArr = reportObj.getJSONArray("device");
        if (deviceArr != null) {
            for (int i = 0; i < deviceArr.size(); i++) {
                JSONObject deviceObj = deviceArr.getJSONObject(i);
                //设备ID
                String id = deviceObj.getString("id");
                //设备端口
                Integer ep = deviceObj.getInteger("ep");
                //状态
                Integer status = deviceObj.getInteger("status");
                //分组成员类型。1：硬件分组；2：软件分组
                Integer mType = deviceObj.getInteger("mtype");
            }
        }
        JSONObject gstObj = reportObj.getJSONObject("gst");
        if (gstObj != null) {
            //开关状态
            Boolean on = gstObj.getBoolean("on");
            //亮度
            Integer bri = gstObj.getInteger("bri");
            Integer sat = gstObj.getInteger("sat");
            Integer hue = gstObj.getInteger("hue");
            Integer ctp = gstObj.getInteger("ctp");
        }
        JSONObject resultObj = new JSONObject(true);
        resultObj.put("code", CommandTypeEnums.REPORT_GROUP_DELETE.orderCode());
        resultObj.put("gid", groupId);
        resultObj.put("control", 2);
        resultObj.put("result", ResultEnums.SUCCESS.code());
        return resultObj;
    }

    /**
     * 网关上报 查询的分组信息
     *
     * @param reportObj 消息json
     */
    public static void reportGroupGetInfo(JSONObject reportObj) {
        //消息序号
        Long serial = reportObj.getLong("serial");
        if (serial == null) {
            return;
        }
        //分组列表
        JSONArray groupArr = reportObj.getJSONArray("groups");
        if (groupArr == null) {
            return;
        }
        for (int i = 0; i < groupArr.size(); i++) {
            JSONObject groupObj = groupArr.getJSONObject(i);
            //分组id
            Integer groupId = groupObj.getInteger("gid");
            //分组名称
            String groupName = groupObj.getString("gname");
            //分组内设备列表
            JSONArray deviceArr = groupObj.getJSONArray("device");
            if (deviceArr != null && !deviceArr.isEmpty()) {
                for (int j = 0; j < deviceArr.size(); j++) {
                    //设备json对象
                    JSONObject deviceObj = deviceArr.getJSONObject(j);
                    //设备ID
                    String id = deviceObj.getString("id");
                    //设备端口
                    Integer ep = deviceObj.getInteger("ep");
                    //分组成员类型。1：硬件分组；2：软件分组
                    Integer mType = deviceObj.getInteger("mtype");
                }
            }
            //属性
            JSONObject gstObj = groupObj.getJSONObject("gst");
            if (gstObj != null) {
                //开关状态
                Boolean on = gstObj.getBoolean("on");
                Integer bri = gstObj.getInteger("bri");
                Integer sat = gstObj.getInteger("sat");
                Integer hue = gstObj.getInteger("hue");
                Integer ctp = gstObj.getInteger("ctp");
            }
        }
    }

    /**
     * 接收删除所有分组的返回消息
     *
     * @param reportObj 返回消息json
     */
    public static void reportDeleteAllGroup(JSONObject reportObj) {
        //消息序号
        Long serial = reportObj.getLong("serial");
        //返回结果  见ResultEnums
        Integer result = reportObj.getInteger("result");
    }

    /**
     * 返回创建或修改策略消息
     *
     * @param reportObj 返回消息json
     */
    public static void reportStrategyCreateOrModify(JSONObject reportObj) {
        //消息序号
        Long serialNo = reportObj.getLong("serial");
        //返回结果  见ResultEnums
        Integer result = reportObj.getInteger("result");
        try {
            //TODO 策略信息类
            StrategyInfoDTO strategyInfoDTO = reportObj.toJavaObject(StrategyInfoDTO.class);
        } catch (Exception e) {
            log.error("返回的策略信息转换失败,reportObj = {},errMsg = {}", reportObj, e.getMessage());
        }
    }

    /**
     * 返回策略使能设置消息
     *
     * @param reportObj 策略使能设置 网关返回的json
     */
    public static void reportStrategySetState(JSONObject reportObj) {
        //消息序号
        Long serialNo = reportObj.getLong("serial");
        //返回结果  见ResultEnums
        Integer result = reportObj.getInteger("result");
        //策略ID
        Integer rid = reportObj.getInteger("rid");
        //使能状态
        Integer state = reportObj.getInteger("state");
    }

    /**
     * 返回删除策略消息
     *
     * @param reportObj 删除策略 网关返回的json
     */
    public static void reportStrategyDelete(JSONObject reportObj) {
        //消息序号
        Long serialNo = reportObj.getLong("serial");
        //返回结果  见ResultEnums
        Integer result = reportObj.getInteger("result");
        //策略ID
        Integer rid = reportObj.getInteger("rid");
    }

    /**
     * 网关删除策略上报(control: 0)、网关修改策略上报(control: 1)、网关创建策略上报(control: 2)、网关策略触发上报(control: 3)
     *
     * @param reportObj 消息json
     * @return 返回json
     */
    public static JSONObject reportStrategyMsgUpload(JSONObject reportObj) {
        //消息序号
        Long serialNo = reportObj.getLong("serial");
        //消息类型
        Integer ctrlType = reportObj.getInteger("control");
        //策略ID
        Integer rid = reportObj.getInteger("rid");
        if (serialNo == null || ctrlType == null) {
            return null;
        }
        JSONObject resultObj = new JSONObject(true);
        resultObj.put("code", CommandTypeEnums.REPORT_STRATEGY_DELETE.orderCode());
        resultObj.put("control", ctrlType);
        resultObj.put("result", ResultEnums.SUCCESS.code());
        resultObj.put("rid", rid);
        if (ctrlType.equals(0)) {
            //网关删除策略上报
        } else if (ctrlType.equals(1)) {
            //网关修改策略上报
            StrategyInfoDTO strategyInfoDTO = reportObj.toJavaObject(StrategyInfoDTO.class);
        } else if (ctrlType.equals(2)) {
            //网关创建策略上报
            StrategyInfoDTO strategyInfoDTO = reportObj.toJavaObject(StrategyInfoDTO.class);
        } else if (ctrlType.equals(3)) {
            //网关策略触发上报
            //策略触发次数
            Integer triged = reportObj.getInteger("triged");
            resultObj.put("triged", triged);
        }
        return resultObj;
    }

    /**
     * 处理下发获取策略信息后返回的消息
     *
     * @param reportObj 上报的json
     */
    public static void reportStrategyGetInfo(JSONObject reportObj) {
        //消息序号
        Long serialNo = reportObj.getLong("serial");
        //策略信息数组
        JSONArray ruleArr = reportObj.getJSONArray("rules");
        if (ruleArr == null || ruleArr.isEmpty()) {
            return;
        }
        for (int i = 0; i < ruleArr.size(); i++) {
            JSONObject ruleObj = ruleArr.getJSONObject(i);
            //TODO 策略信息实体类
            StrategyInfoDTO strategyInfoDTO = ruleObj.toJavaObject(StrategyInfoDTO.class);
        }

    }


}
