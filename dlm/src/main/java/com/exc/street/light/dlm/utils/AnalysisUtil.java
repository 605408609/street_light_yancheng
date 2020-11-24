package com.exc.street.light.dlm.utils;

import com.exc.street.light.resource.dto.electricity.ElectricityMeterModuleDataDTO;
import com.exc.street.light.resource.entity.electricity.CanAlarmData;
import com.exc.street.light.resource.entity.electricity.CanChangeData;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 协议解析
 *
 * @author Linshiwen
 * @date 2018/5/26
 */
@Slf4j
public class AnalysisUtil {
    /**
     * 解析按设备获取实时数据
     *
     * @param protocol 协议
     * @return 测点数据
     */
    public static int getValue(byte[] protocol) {
        int valueIndex = 19;
        int value = protocol[valueIndex];
        return value;
    }

    /**
     * 解析按设备获取驱动模块实时数据
     *
     * @param protocol 协议
     * @return 测点数据
     */
    public static List<Double> getDeviceData(byte[] protocol) {
        List<Double> list = new ArrayList<>(2);
        //开关值开始索引
        int valueIndex = 21;
        list.add((double) protocol[21]);
        //电流值开始
        int currentIndex = 23;
        //电流值结束索引
        int endIndex = 27;
        for (int i = currentIndex; i < endIndex; i++) {
            double current = FloatUtil.byte2float(protocol[i], protocol[++i], protocol[++i], protocol[++i]);
            list.add(current);
        }
        return list;
    }

    /**
     * 解析按设备获取驱动模块实时数据
     *
     * @param protocol 协议
     * @return 测点数据
     */
    public static List<Double> getData(byte[] protocol) {
        List<Double> list = new ArrayList<>(2);
        //开关值开始索引
        int valueIndex = 21;
        list.add((double) protocol[21]);
        //电流值开始
        int currentIndex = 23;
        //电流值接收索引
        int endIndex = 27;
        for (int i = currentIndex; i < endIndex; i++) {
            double current = FloatUtil.byte2float(protocol[i], protocol[++i], protocol[++i], protocol[++i]);
            list.add(current);
        }
        return list;
    }

    /**
     * 解析响应数据的返回码
     * 合广 11
     * 自研 9
     *
     * @param protocol 协议
     * @return 返回码
     */
    public static byte getRtn(byte[] protocol) {
        if (protocol == null) {
            return -1;
        }
        int rtnIndex = 9;
        byte trn = 0;
        if (protocol != null) {
            trn = protocol[rtnIndex];
        }
        return trn;
    }

    /**
     * 获取网关版本号
     *
     * @param protocol 协议
     * @return 返回码
     */
    public static String getVersion(byte[] protocol) {
        int versionIndex = 12;
        String version = null;
        if (protocol != null) {
            int high = protocol[versionIndex] >> 4 & 0xFF;
            int low = protocol[versionIndex] & 0x0F;
            version = high + "." + low;
        }
        return version;
    }

    /**
     * 解析响应数据的控制命令
     * 合广:11
     * 自研:8
     *
     * @param protocol 协议
     * @return 控制命令
     */
    public static byte getControlId(byte[] protocol) {
        int rtnIndex = 8;
        return protocol[rtnIndex];
    }

    /**
     * 解析响应数据的RET返回码
     *
     * @param protocol 协议
     * @return 返回码
     */
    public static byte getRet(byte[] protocol) {
        int rtnIndex = 9;
        return protocol[rtnIndex];
    }

    /**
     * 获取设备物理地址
     * 合广:下面
     * 自研: 2-8
     */
    public static String getAddress(byte[] protocol) {
        StringBuilder sb = new StringBuilder(12);
        //物理地址开始索引
        int addressBeginIndex = 2;
        //物理地址结束索引
        int addressEndIndex = 8;
        for (int i = addressBeginIndex; i < addressEndIndex; i++) {
            int b = protocol[i];
            b = b & 0xff;
            String hexString = Integer.toHexString(b);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            sb.append(hexString.toUpperCase());
            if (i < 7) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    /**
     * 网关主动上传变化数据协议解析
     *
     * @param data 协议
     * @return 返回变化数据集合
     */
    public static Map<String, List<CanChangeData>> getChangeData(byte[] data) {
        //内容区长度
        int contentLen = ArrayUtil.getContentLen(data);
        //内容区开始索引
        int dataIndex = 12;
        List<CanChangeData> list = new ArrayList<>();
        //设备地址
        String address = ArrayUtil.getAddress(data);
        //内容区数据内容条数
        int contentNum = ArrayUtil.getContentNum(data);
        //内容区固定长度
        int i = 11;
        byte[] value1 = new byte[4];
        for (int tmp = 0; tmp < contentNum; ++tmp) {
            int j = 0;
            CanChangeData changeData = new CanChangeData();
            //获取模块地址
            byte addr = data[++dataIndex];
            changeData.setAddress((int) addr);
            //获取通道(此处需要+1)
            byte channel = data[++dataIndex];
            changeData.setControlId((int) channel + 1);
            //获取通道类型
            byte channelType = data[++dataIndex];
            changeData.setType((int) channelType);

            //获取通道值
            //float 4个字节
            value1[0] = data[++dataIndex];
            value1[1] = data[++dataIndex];
            value1[2] = data[++dataIndex];
            value1[3] = data[++dataIndex];
            int val = HexUtil.bytesToInt(value1);
            changeData.setValue(Float.valueOf(Integer.toString(val)));

            //获取上传时间
            //采集时间
            byte date1 = data[++dataIndex];
            byte date2 = data[++dataIndex];
            byte date3 = data[++dataIndex];
            byte date4 = data[++dataIndex];
            Date time = ArrayUtil.getTime(date1, date2, date3, date4);
            changeData.setTime(time);
            list.add(changeData);
        }
        Map<String, List<CanChangeData>> map = new HashMap<>(1);
        map.put(address, list);
        return map;
    }

    /**
     * 网关主动上传告警数据协议解析
     *
     * @param protocol 协议
     * @return 返回变化数据集合
     */
    public static List<CanAlarmData> getAlarmData(byte[] protocol) {
        //内容区长度
        int contentLen = ArrayUtil.getContentLen(protocol);
        //内容区tagid开始索引
        int dataIndex = 17;
        List<CanAlarmData> list = new ArrayList<CanAlarmData>();
        //设备地址
        String address = ArrayUtil.getAddress(protocol);
        //内容区固定长度
        int i = 4;
        while (i < contentLen) {
            CanAlarmData alarmData = new CanAlarmData();
            alarmData.setAddress(address);
            //获取tagId
            byte tagId1 = protocol[++dataIndex];
            byte tagId2 = protocol[++dataIndex];
            int tagId = ArrayUtil.getValue(tagId1, tagId2);
            alarmData.setTagId(tagId);
            //测点数值
            byte value1 = protocol[++dataIndex];
            byte value2 = protocol[++dataIndex];
            byte value3 = protocol[++dataIndex];
            byte value4 = protocol[++dataIndex];
            float value = FloatUtil.byte2float(value1, value2, value3, value4);
            alarmData.setTagValue(value);
            //告警限制值
            byte alarm1 = protocol[++dataIndex];
            byte alarm2 = protocol[++dataIndex];
            byte alarm3 = protocol[++dataIndex];
            byte alarm4 = protocol[++dataIndex];
            float alarmValue = FloatUtil.byte2float(alarm1, alarm2, alarm3, alarm4);
            alarmData.setAlarmValue(alarmValue);
            //告警类型
            byte type1 = protocol[++dataIndex];
            byte type2 = protocol[++dataIndex];
            int alarmType = ArrayUtil.getValue(type1, type2);
            alarmData.setAlarmType(alarmType);
            //告警动作
            byte action = protocol[++dataIndex];
            alarmData.setAlarmAction((int) action);
            //采集时间
            byte date1 = protocol[++dataIndex];
            byte date2 = protocol[++dataIndex];
            byte date3 = protocol[++dataIndex];
            byte date4 = protocol[++dataIndex];
            Date time = ArrayUtil.getTime(date1, date2, date3, date4);
            alarmData.setTime(time);
            list.add(alarmData);
            //一个数据的长度:14个字节
            //内容区当前长度
            i += 14;
        }
        return list;
    }

    /**
     * 获取网关电表数据响应
     *
     * @param protocol
     * @return
     */
    public static List<ElectricityMeterModuleDataDTO> getMeterModuleDataCommandResp(byte[] protocol) {
        //内容区长度
        int contentLen = ArrayUtil.getElectricMeterContentLen(protocol);
        //内容区tagid开始索引
        int dataIndex = 12;
        //设备地址
        String address = ArrayUtil.getAddress(protocol);
        //单个电表数据块长度
        int singleMeterDataLength = 94;
        if (contentLen % singleMeterDataLength != 0) {
            log.error("获取网关电表数据响应,数据长度有误(!=94*n),长度={},protocol={}", contentLen, protocol);
            return new ArrayList<>();
        }
        //电表数据条数        
        int meterDataNum = contentLen / singleMeterDataLength;
        List<ElectricityMeterModuleDataDTO> dataDtoList = new ArrayList<>();
        for (int i = 0; i < meterDataNum; i++) {
            ElectricityMeterModuleDataDTO data = new ElectricityMeterModuleDataDTO();
            data.setGatewayMac(address);
            //电表所在485端口号 范围1~3；
            data.setElectricMeterPort(HexUtil.bytesToInteger(HexUtil.getByteArr(protocol, dataIndex++, 1)));
            //电表地址 范围1~3；
            data.setElectricMeterAddr(HexUtil.bytesToInteger(HexUtil.getByteArr(protocol, dataIndex++, 1)));
            //当前组合有功总电能
            data.setCurrentCombinedActiveTotalEnergy(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //当前正向有功总电能
            data.setCurrentTotalPositiveActiveEnergy(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //当前反向有功总电能
            data.setCurrentTotalReverseActiveEnergy(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //当前正向无功总电能
            data.setCurrentTotalPositiveReactivePower(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //当前反向视在总电能
            data.setCurrentReverseApparentTotalEnergy(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  A相电压
            data.setAPhaseVoltage(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  B相电压
            data.setBPhaseVoltage(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  C相电压
            data.setCPhaseVoltage(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  A相电流
            data.setAPhaseCurrent(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  B相电流
            data.setBPhaseCurrent(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  C相电流
            data.setCPhaseCurrent(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  A相有功功率
            data.setAPhaseActivePower(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  A相无功功率
            data.setAPhaseReactivePower(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  A相视在功率
            data.setAPhaseApparentPower(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  A相功率因数字
            data.setAPhasePowerFactorNumber(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  B相有功功率
            data.setBPhaseActivePower(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  B相无功功率
            data.setBPhaseReactivePower(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  B相视在功率
            data.setBPhaseApparentPower(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  B相功率因数字
            data.setBPhasePowerFactorNumber(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  C相有功功率
            data.setCPhaseActivePower(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  C相无功功率
            data.setCPhaseReactivePower(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  C相视在功率
            data.setCPhaseApparentPower(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            //  C相功率因数字
            data.setCPhasePowerFactorNumber(HexUtil.bytesToFloat(HexUtil.getByteArr(protocol, dataIndex, 4), 2));
            dataIndex += 4;
            dataDtoList.add(data);
        }
        return dataDtoList;
    }

    /**
     * 解析网关返回的时间
     *
     * @param protocol
     * @return
     */
    public static Date getTime(byte[] protocol) {
        //内容开始下标
        int timeIndex = 12;
        //年
        byte yearByte1 = protocol[timeIndex++];
        byte yearByte2 = protocol[timeIndex++];
        //月
        byte month = protocol[timeIndex++];
        //日
        byte date = protocol[timeIndex++];
        //时
        byte hour = protocol[timeIndex++];
        //分
        byte minute = protocol[timeIndex++];
        //秒
        byte second = protocol[timeIndex++];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{yearByte1, yearByte2}), 16));
        c.set(Calendar.MONTH, Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{month}), 16) - 1);
        c.set(Calendar.DATE, Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{date}), 16));
        c.set(Calendar.HOUR, Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{hour}), 16));
        c.set(Calendar.MINUTE, Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{minute}), 16));
        c.set(Calendar.SECOND, Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{second}), 16));
        return c.getTime();
    }

    /**
     * 解析网关返回的经纬度
     *
     * @param protocol
     * @return [0]：经度坐标 [1]:纬度坐标
     */
    public static double[] getLngAndLat(byte[] protocol) {
        //内容开始下标
        int timeIndex = 10;
        //坐标内容长度下表
        int contentLength = 4;
        //经度数组
        byte[] lngBytes = new byte[4];
        System.arraycopy(protocol, timeIndex, lngBytes, 0, contentLength);
        //纬度数组
        byte[] latBytes = new byte[4];
        System.arraycopy(protocol, timeIndex + contentLength, latBytes, 0, contentLength);
        double[] coordArr = new double[2];
        coordArr[0] = lngLatByte2Double(lngBytes);
        coordArr[1] = lngLatByte2Double(latBytes);
        return coordArr;
    }


    /**
     * 经纬度byte数组转double坐标
     *
     * @param b
     * @return
     */
    public static double lngLatByte2Double(byte[] b) {
        if (b.length != 4) {
            return 0;
        }
        double degrees = Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{b[0], b[1]}), 16);
        double minutes = Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{b[2]}), 16);
        double seconds = Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{b[3]}), 16);
        return (degrees + (minutes / 60) + (seconds / 3600));
    }

    /**
     * 获取集控回路对应开关的场景id
     * @param loopNum 集控回路编号 1-8
     * @param isOpen 是否 开场景
     *               开场景 3,5,7,9,11,13,15,17
     *               关场景 4,6,8,10,12,14,16,18
     * @return
     */
    public static int getSceneIdByLoopNum(int loopNum, boolean isOpen) {
        if (isOpen) {
            return 2 + loopNum * 2 - 1;
        } else {
            return 2 + loopNum * 2;
        }
    }
}
