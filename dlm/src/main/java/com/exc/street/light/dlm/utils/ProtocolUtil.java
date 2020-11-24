package com.exc.street.light.dlm.utils;

import com.exc.street.light.resource.dto.dlm.ControlLoopTimerDTO;
import com.exc.street.light.resource.dto.electricity.Timer;
import com.exc.street.light.resource.dto.electricity.*;
import com.exc.street.light.resource.entity.electricity.CanTiming;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 协议工具类
 *
 * @Author: Xiaok
 * @Date: 2020/10/10 10:02
 */
public class ProtocolUtil {

    /**
     * 协议内容为空
     *
     * @param address 设备地址
     * @param tag     命令信息控制符
     * @return
     */
    public static byte[] getProtocol(String address, byte tag) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 0;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int chksumIndex = getCommonbyte(protocols, address, tag, contentLength);
        check(protocols, chksumIndex);
        return protocols;
    }

    /**
     * 错误响应内容体
     *
     * @param ret
     * @return
     */
    public static String getRet(byte ret) {
        String response = null;
        switch (ret) {
            case (byte) 01:
                response = "ADR不匹配";
                break;
            case (byte) 02:
                response = "CMD无效";
                break;
            case (byte) 03:
                response = "INFO数据错误,CRC校验出错";
                break;
            case (byte) 04:
                response = "命令执行失败";
                break;
            case (byte) 05:
                response = "系统执行结果不对";
                break;
            case (byte) 06:
                response = "解密错误";
                break;
            case (byte) 07:
                response = "数据错误,协议头或者尾错误";
                break;
        }
        return response;
    }

    /**
     * 按设备获取实时数据
     * 自研未使用
     *
     * @param address 设备地址
     * @param tagId   通道id
     * @return 返回协议
     */
    public static byte[] getDataByDevice(String address, int tagId) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 2;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int commandIndex = getCommonbyte(protocols, address,
                ConstantUtil.CONTROL_IDENTIFIER2_12, contentLength);
        //编号高字节
        protocols[commandIndex] = (byte) (tagId >> 8 & 0xff);
        //编号低字节
        protocols[++commandIndex] = (byte) (tagId & 0xff);
        //校验码索引
        int checkIndex = commandIndex + 1;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 删除所有定时
     *
     * @param address 设备地址
     * @return 协议
     */
    public static byte[] deleteTimer(String address) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 1;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int timerIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_8, contentLength);
        //场景编码为0时表示删除
        protocols[timerIndex] = 0;
        //校验码索引
        int checkIndex = timerIndex + 1;
        check(protocols, checkIndex);
        return protocols;
    }

    public static byte[] getScene(String address, Integer sceneNo) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 0;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int timerIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_7, contentLength);
        //场景编码为0时表示删除
        protocols[timerIndex] = (byte) (sceneNo == null ? 0 : sceneNo);
        //校验码索引
        int checkIndex = timerIndex + 1;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 周期类型设定
     *
     * @param timingType  定时类型 1-年月日定时 0-周期执行
     * @param cycleTypes  星期选择
     * @param executeType 执行或不执行 0-年月日传 1-周期传
     * @return
     */
    public static int getCycleType(int[] cycleTypes, int timingType, int executeType) {
        int bits = 0;
        for (int j = 0; j < cycleTypes.length; j++) {
            int cycleType = cycleTypes[j];
            if (cycleType == 1) {
                bits = bits | (executeType << 1);
            }
            if (cycleType == 2) {
                bits = bits | (executeType << 2);
            }
            if (cycleType == 3) {
                bits = bits | (executeType << 3);
            }
            if (cycleType == 4) {
                bits = bits | (executeType << 4);
            }
            if (cycleType == 5) {
                bits = bits | (executeType << 5);
            }
            if (cycleType == 6) {
                bits = bits | (executeType << 6);
            }
            if (cycleType == 7) {
                bits = bits | (executeType << 7);
            }
        }
        bits = timingType | bits;
        return bits;
    }

    public static void main(String[] args) {
        //时间点定时
        System.out.println(getCycleType(new int[]{}, 1, 0));
        //周期定时
        System.out.println(getCycleType(new int[]{1, 2, 3, 7}, 0, 1));
    }

    /**
     * 获取强电网关端口点表数据
     *
     * @param address
     * @param comId
     * @return
     */
    public static byte[] getElectricityTable(String address, int comId) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 1;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int commandIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_29, contentLength);
        //COM端口号 1-5
        protocols[commandIndex] = (byte) comId;
        //校验码索引
        int checkIndex = commandIndex + 1;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 获取自研网关点表数据
     *
     * @param responseBytes 请求返回的byte数组数据
     */
    public static List<GatewayParameter> getModuleInfo(byte[] responseBytes) {
        List<GatewayParameter> gatewayParameters = new ArrayList<>();

        int baseLength = ConstantUtil.BASE_LENGTH + 1;
        int bodyBaseLength = ConstantUtil.GATEWAY_MODULE_ONE_BODY;
        int HighLength = responseBytes[10] & 0xff;
        int lowLength = responseBytes[11] & 0xff;
        int toTleLength = (HighLength << 8) + lowLength;
        int moduleNum = toTleLength / bodyBaseLength;
        StringBuffer stringBuffer;
        int startLength = baseLength - 3;
        int moduleDeviceType;
        int deviceAddress;
        String batchNumber;
        String unitType;
        String deviceName;
        if (moduleNum > 0) {
            for (int i = 0; i < moduleNum; ++i) {
                GatewayParameter gatewayParameter = new GatewayParameter();
                //插入软件版本
                byte aaa = responseBytes[startLength + bodyBaseLength * i + 3];
                String tmpVs = HexUtil.byteToHex(aaa);
                stringBuffer = new StringBuffer(tmpVs);
                stringBuffer.insert(1, ".");
                tmpVs = stringBuffer.toString();
                gatewayParameter.setSoftwareVersion(tmpVs);
                //插入硬件版本
                String tmpHs = HexUtil.byteToHex(responseBytes[(startLength + bodyBaseLength * i + 4)]);
                stringBuffer = new StringBuffer(tmpHs);
                stringBuffer.insert(1, ".");
                tmpHs = stringBuffer.toString();
                gatewayParameter.setHardWareVersion(tmpHs);
                //插入设备类型
                moduleDeviceType = responseBytes[(startLength + bodyBaseLength * i + 5)];
                gatewayParameter.setModuleDeviceType(moduleDeviceType);
                //插入设备地址
                deviceAddress = responseBytes[(startLength + bodyBaseLength * i + 6)];
                gatewayParameter.setDeviceAddress(deviceAddress);
                //插入设备生产批号
                byte[] batchBytes = new byte[12];
                for (int j = 0; j < 12; ++j) {
                    batchBytes[j] = responseBytes[(startLength + bodyBaseLength * i + 7 + j)];
                }

                batchNumber = HexUtil.bytesToString(batchBytes, "ascii");
                gatewayParameter.setBatchNumber(batchNumber);

                //插入设备型号
                byte[] unitTypeBytes = new byte[20];
                for (int k = 0; k < 20; ++k) {
                    unitTypeBytes[k] = responseBytes[(startLength + bodyBaseLength * i + 19 + k)];
                }

                unitType = HexUtil.bytesToString(unitTypeBytes, "ascii");
                gatewayParameter.setUnitType(unitType);

                //插入设备名称
                byte[] deviceNameBytes = new byte[20];
                for (int l = 0; l < 20; ++l) {
                    deviceNameBytes[l] = responseBytes[(startLength + bodyBaseLength * i + 39 + l)];
                }
                deviceName = HexUtil.bytesToString(deviceNameBytes, "utf-8");
                gatewayParameter.setDeviceName(deviceName);
                //插入电流模块绑定信息
                if (responseBytes[(startLength + bodyBaseLength * i + 59)] != 0x00) {
                    List<ElectricityModule> electricityModuleList = new ArrayList<>(4);
                    for (int k = 0; k < 4; ++k) {
                        ElectricityModule electricityModule = new ElectricityModule();
                        int address = responseBytes[(startLength + bodyBaseLength * i + 59 + k * 2)];
                        int channelNum = responseBytes[(startLength + bodyBaseLength * i + 59 + k * 2 + 1)];
                        electricityModule.setChannelNum(channelNum);
                        electricityModule.setDeviceAddress(address);
                        electricityModuleList.add(electricityModule);
                    }
                    gatewayParameter.setElectricityModuleList(electricityModuleList);
                }
                gatewayParameters.add(gatewayParameter);
            }
        }
        return gatewayParameters;
    }

    public static GeographyParameter getGeographyParameter(byte[] protocols) {
        GeographyParameter geographyParameter = new GeographyParameter();
        Double second = Double.valueOf(protocols[10] / 60);
        Double minute = Double.valueOf((protocols[11] + second) / 60);
        Double longitude = protocols[12] + minute;
        geographyParameter.setLongitude(longitude.toString());

        second = Double.valueOf(protocols[13] / 60);
        minute = Double.valueOf((protocols[14] + second) / 60);
        Double latitude = protocols[15] + minute;
        geographyParameter.setLatitude(latitude.toString());

        int hour = protocols[16];
        int minute1 = protocols[17];

        geographyParameter.setSunriseTime((hour < 10 ? "0" + hour : hour) + ":" + (minute1 < 10 ? "0" + minute1 : minute1));

        hour = protocols[18];
        minute1 = protocols[19];
        geographyParameter.setSunsetTime((hour < 10 ? "0" + hour : hour) + ":" + (minute1 < 10 ? "0" + minute1 : minute1));

        return geographyParameter;
    }

    public static byte[] setControlNow(String address, List<ControlObject> controlObjects) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        List<Integer> devices = new ArrayList<>();
        ControlObject controlObject;
        List<ChannelControlDeviceParameter> channelControlDeviceParameters = new ArrayList<>();
        //先找出模块数目
        for (int i = 0; i < controlObjects.size(); ++i) {
            controlObject = controlObjects.get(i);
            int device = controlObject.getDeviceAddress();
            if (!devices.contains(device)) {
                devices.add(device);
                ChannelControlDeviceParameter ch = new ChannelControlDeviceParameter();
                ch.setAddress(device);
                channelControlDeviceParameters.add(ch);
            }
        }
        int size = channelControlDeviceParameters.size();
        int contentLength = size * 9 + 3;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int sceneIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_6, contentLength);
        int sn = ConstantUtil.SCENE_NOW_CONTROL_SN;
        //场景编号
        protocols[sceneIndex] = (byte) sn;
        //场景状态索引
        int statusIndex = sceneIndex + 1;
        protocols[statusIndex] = (byte) ConstantUtil.TIMING_TYPE_3;
        //场景控制对象索引
        int controlIndex = statusIndex + 1;
        //控制对象数量
        protocols[controlIndex] = (byte) size;
        //模块索引
        int moduleIndex = controlIndex + 1;
        //添加模块
        byte[] bytes = SceneUtil.getSceneData(controlObjects, channelControlDeviceParameters);
        for (int i = 0; i < bytes.length; ++i) {
            protocols[moduleIndex] = bytes[i];
            moduleIndex++;
        }
        //校验码索引
        int checkIndex = moduleIndex;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 计算协议校验码
     *
     * @param protocols   协议
     * @param chksumIndex 校验码索引
     */
    public static void check(byte[] protocols, int chksumIndex) {
        int crc16 = Crc16.calcCrc16(Arrays.copyOfRange(protocols, 2, protocols.length - 3));
        //校验码低8位
        protocols[chksumIndex] = (byte) (crc16 & 0xff);
        //校验码高8位
        protocols[++chksumIndex] = (byte) (crc16 >> 8 & 0xff);
        protocols[++chksumIndex] = ConstantUtil.END_OF_INFORMATION;
    }

    /**
     * 设置共有的协议内容，对应自研协议
     *
     * @param protocols     协议
     * @param address       设备地址
     * @param control       控制命令
     * @param contentLength 内容长度
     * @return 返回校验码索引
     */
    public static int getCommonbyte(byte[] protocols, String address, byte control, int contentLength) {
        byte[] startOfInformation = ConstantUtil.START_OF_INFORMATION;
        int i;
        for (i = 0; i < startOfInformation.length; i++) {
            protocols[i] = startOfInformation[i];
        }
        byte[] addresses = StringUtil.getAddress(address);
        for (int j = 0; j < addresses.length; j++) {
            protocols[i] = addresses[j];
            i++;
        }
        protocols[i] = control;
        //数据长度高8位
        protocols[++i] = (byte) (contentLength >> 8 & 0xff);
        //数据长度低8位
        protocols[++i] = (byte) (contentLength & 0xff);
        return ++i;
    }


    /**
     * byte数组类型经纬度转double
     *
     * @param b
     * @return
     */
    public static double LngLatByte2Double(byte[] b) {
        if (b.length != 4) {
            return 0;
        }
        double degrees = Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{b[0], b[1]}), 16);
        double minutes = Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{b[2]}), 16);
        double seconds = Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(new byte[]{b[3]}), 16);
        return (degrees + (minutes / 60) + (seconds / 3600));
    }

    /**
     * 心跳包应答
     *
     * @param address 设备地址
     * @return
     */
    public static byte[] responseHeartbeat(String address) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        byte[] protocols = new byte[baseLength];
        int index = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_5, baseLength);
        int chksumIndex = index;
        check(protocols, chksumIndex);
        return protocols;
    }

    /**
     * 设置网关时间
     *
     * @param address 网关地址
     * @param c       时间
     * @return 消息命令
     */
    public static byte[] setTime(String address, Calendar c) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 7;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int timeIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_3, contentLength);
        //获取年份byte数组
        byte[] yearBytes = cn.hutool.core.util.HexUtil.decodeHex(StringUtil.padRight(Integer.toHexString(c.get(Calendar.YEAR)), 4, '0'));
        protocols[timeIndex++] = yearBytes[0];
        protocols[timeIndex++] = yearBytes[1];
        //获取月份
        protocols[timeIndex++] = cn.hutool.core.util.HexUtil.decodeHex(StringUtil.padRight(Integer.toHexString(c.get(Calendar.MONTH) + 1), 2, '0'))[0];
        //获取日
        protocols[timeIndex++] = cn.hutool.core.util.HexUtil.decodeHex(StringUtil.padRight(Integer.toHexString(c.get(Calendar.DATE)), 2, '0'))[0];
        //获取时
        protocols[timeIndex++] = cn.hutool.core.util.HexUtil.decodeHex(StringUtil.padRight(Integer.toHexString(c.get(Calendar.HOUR_OF_DAY)), 2, '0'))[0];
        //获取分
        protocols[timeIndex++] = cn.hutool.core.util.HexUtil.decodeHex(StringUtil.padRight(Integer.toHexString(c.get(Calendar.MINUTE)), 2, '0'))[0];
        //获取秒
        protocols[timeIndex++] = cn.hutool.core.util.HexUtil.decodeHex(StringUtil.padRight(Integer.toHexString(c.get(Calendar.SECOND)), 2, '0'))[0];
        //进行校验
        check(protocols, timeIndex);
        return protocols;
    }

    /**
     * 设置时间
     *
     * @param address 设备地址
     * @return
     */
    public static byte[] setTime(String address) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 8;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int timeIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER2_2, contentLength);
        byte[] time = TimeUtil.getTime();
        for (int i = 0; i < time.length; i++) {
            protocols[timeIndex] = time[i];
            timeIndex++;
        }
        check(protocols, timeIndex);
        return protocols;
    }

    /**
     * 获取网关时间
     *
     * @param address 网关地址
     * @return 消息命令
     */
    public static byte[] getTime(String address) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 0;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int timeIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_4, contentLength);
        //进行校验
        check(protocols, timeIndex);
        return protocols;
    }

    /**
     * 设置场景
     *
     * @param address 设备地址
     * @param scene   场景对象
     * @return
     */
    public static byte[] setScene(String address, Scene scene) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        List<Integer> devices = new ArrayList<>();
        List<ControlObject> controlObjects = scene.getControlObjects();
        ControlObject controlObject;
        List<ChannelControlDeviceParameter> channelControlDeviceParameters = new ArrayList<>();
        //先找出模块数目
        for (int i = 0; i < controlObjects.size(); ++i) {
            controlObject = controlObjects.get(i);
            int device = controlObject.getDeviceAddress();
            if (!devices.contains(device)) {
                devices.add(device);
                ChannelControlDeviceParameter ch = new ChannelControlDeviceParameter();
                ch.setAddress(device);
                channelControlDeviceParameters.add(ch);
            }
        }
        int size = channelControlDeviceParameters.size();
        int contentLength = size * 9 + 3;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int sceneIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_6, contentLength);
        int sn = scene.getSn();
        //场景编号
        protocols[sceneIndex] = (byte) sn;
        //场景状态索引
        int statusIndex = sceneIndex + 1;
        protocols[statusIndex] = (byte) scene.getStatus();
        //场景控制对象索引
        int controlIndex = statusIndex + 1;
        //控制对象数量
        protocols[controlIndex] = (byte) size;
        //模块索引
        int moduleIndex = controlIndex + 1;
        //添加模块
        byte[] bytes = SceneUtil.getSceneData(controlObjects, channelControlDeviceParameters);
        for (int i = 0; i < bytes.length; ++i) {
            protocols[moduleIndex] = bytes[i];
            moduleIndex++;
        }
        //校验码索引
        int checkIndex = moduleIndex;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 设置定时
     *
     * @param address 设备mac地址
     * @param timer   定时器
     * @return 协议
     */
    public static byte[] setTimer(String address, Timer timer) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 0;
        List<CanTiming> timings = timer.getCanTimings().stream().filter(timing -> timing.getIsExecute() == 1).collect(Collectors.toList());
        int timingParameterNum = timings.size();
        if (timingParameterNum > 0) {
            contentLength += 1;
            for (int i = 0; i < timingParameterNum; i++) {
                contentLength += ConstantUtil.TIMER_BASE_LENGTH;
            }
        } else {
            contentLength = 1;
        }
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int timerIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_8, contentLength);
        if (timingParameterNum > 0) {
            //设置定时数量
            protocols[timerIndex] = (byte) timingParameterNum;
            for (int i = 0; i < timingParameterNum; i++) {
                CanTiming canTiming = timings.get(i);
                Date startDate = canTiming.getStartDate();
                Date endDate = canTiming.getEndDate();
                //开始日期
                Calendar calender = Calendar.getInstance();
                if (startDate == null) {
                    calender.setTime(new Date());
                } else {
                    calender.setTime(startDate);
                }
                byte year = HexUtil.getByte(calender.get(Calendar.YEAR) - 2000);
                byte month = HexUtil.getByte(calender.get(Calendar.MONTH) + 1);
                byte day = HexUtil.getByte(calender.get(Calendar.DAY_OF_MONTH));
                protocols[++timerIndex] = HexUtil.getByte(year);
                protocols[++timerIndex] = HexUtil.getByte(month);
                protocols[++timerIndex] = HexUtil.getByte(day);
                //结束日期
                if (endDate == null) {
                    for (int j = 0; j < 3; ++j) {
                        protocols[++timerIndex] = (byte) 0xFF;
                    }
                } else {
                    calender.setTime(endDate);
                    year = HexUtil.getByte(calender.get(Calendar.YEAR) - 2000);
                    month = HexUtil.getByte(calender.get(Calendar.MONTH) + 1);
                    day = HexUtil.getByte(calender.get(Calendar.DAY_OF_MONTH));
                    protocols[++timerIndex] = HexUtil.getByte(year);
                    protocols[++timerIndex] = HexUtil.getByte(month);
                    protocols[++timerIndex] = HexUtil.getByte(day);
                }
                //执行类型
                int type = canTiming.getType();
                protocols[++timerIndex] = (byte) type;
                //执行周期类型 当执行类型为1(定时),该位有效
                if (type == 1) {
                    int bits = canTiming.getCycleType();
                    protocols[++timerIndex] = (byte) bits;
                } else {
                    protocols[++timerIndex] = (byte) 0;
                }
                //定时执行的场景ID
                int tagId = canTiming.getTagId();
                protocols[++timerIndex] = (byte) tagId;
                //定时参数 执行类型为1（定时）时，3字节分别为时、分、秒；
                //        其它类型：前2字节保留0，第3字节为偏移量，单位：分钟
                if (type == 1) {
                    //定时类型
                    String time = canTiming.getTime();
                    byte[] times = HexUtil.hexStringToBytes(time.replaceAll(" ", ""));
                    //直接由String转byte数组,传入的参数即为String类型的字节数组
                    protocols[++timerIndex] = times[0];
                    protocols[++timerIndex] = times[1];
                    protocols[++timerIndex] = times[2];
                } else {
                    //日出之前、日出之后、日落之前、日落之后 类型
                    int minute = canTiming.getMinuteValue();
                    protocols[++timerIndex] = 0;
                    protocols[++timerIndex] = 0;
                    protocols[++timerIndex] = HexUtil.getByte(minute);
                }
            }
        } else {
            protocols[timerIndex] = 0;
        }
        //校验码索引
        int checkIndex = timerIndex + 1;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 设置网关经纬度
     *
     * @param address   网关地址
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     */
    public static byte[] setLongitudeAndLatitude(String address, Double longitude, Double latitude) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 12;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int commandIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_19, contentLength);

        //设置经度
        protocols[commandIndex] = (byte) (longitude < 0 ? 1 : 0);
        protocols[++commandIndex] = (byte) longitude.intValue();
        int minute = (int) ((longitude - longitude.intValue()) * 60);
        protocols[++commandIndex] = (byte) minute;
        int second = (int) ((((longitude - longitude.intValue()) * 60) - minute) * 60);
        protocols[++commandIndex] = (byte) second;
        //设置纬度
        protocols[++commandIndex] = (byte) (latitude < 0 ? 1 : 0);
        protocols[++commandIndex] = (byte) latitude.intValue();
        minute = (int) ((latitude - latitude.intValue()) * 60);
        protocols[++commandIndex] = (byte) minute;
        second = (int) ((((latitude - latitude.intValue()) * 60) - minute) * 60);
        protocols[++commandIndex] = (byte) second;

        //时间填充位数
        protocols[++commandIndex] = 0;
        protocols[++commandIndex] = 0;
        protocols[++commandIndex] = 0;
        protocols[++commandIndex] = 0;
        //校验码索引
        int checkIndex = commandIndex + 1;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 获取经纬度和日出日落时间
     *
     * @param address 网关地址
     * @return
     */
    public static byte[] getLongitudeAndLatitude(String address) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 1;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int commandIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_20, contentLength);
        //校验码索引
        int checkIndex = commandIndex;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 获取网关电表模块数据 命令
     *
     * @param address 网关地址
     * @return
     */
    public static byte[] getMeterModuleDataCommand(String address) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 0;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int commandIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_2A, contentLength);
        //校验码索引
        int checkIndex = commandIndex;
        check(protocols, checkIndex);
        return protocols;
    }

    public static byte[] get(String address) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        int contentLength = 1;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int commandIndex = getCommonbyte(protocols, address, ConstantUtil.CONTROL_IDENTIFIER_EXC_20, contentLength);
        //校验码索引
        int checkIndex = commandIndex;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 设置控制命令
     *
     * @param address        设备地址
     * @param controlCommand 控制命令对象
     * @return 返回协议
     */
    public static byte[] setControlCommand(String address, ControlCommand controlCommand) {
        int baseLength = ConstantUtil.BASE_LENGTH;  //协议基本开销
        int contentLength = 3;                      //控制内容的长度
        int length = baseLength + contentLength;    //3+14=17
        byte[] protocols = new byte[length];        //protocols长度为17
        int commandIndex = getCommonbyte(protocols, address,
                ConstantUtil.CONTROL_IDENTIFIER_EXC_45, contentLength);
        //模块地址
        protocols[commandIndex] = (byte) (controlCommand.getDeviceAddress());
        //回路通道
        protocols[++commandIndex] = (byte) (controlCommand.getControlId());
        byte[] value = HexUtil.hexStringToBytes(ConstantUtil.CHANNEL_CONTROL_STATUS);
        //控制值
        if (controlCommand.getValue() == 1) {
            protocols[++commandIndex] = value[0];
        } else {
            protocols[++commandIndex] = value[1];
        }
        //校验码索引
        int checkIndex = commandIndex + 1;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 设置组控制命令
     *
     * @param address         设备地址
     * @param controlCommands 控制命令对象集合
     * @return 返回协议
     */
    public static byte[] setGroupControlCommand(String address, List<ControlCommand> controlCommands) {
        int baseLength = ConstantUtil.BASE_LENGTH;
        List<Integer> devices = new ArrayList<>();
        ControlCommand controlCommand;
        List<ChannelControlDeviceParameter> channelControlDeviceParameters = new ArrayList<>();
        //先找出模块数目
        for (int i = 0; i < controlCommands.size(); ++i) {
            controlCommand = controlCommands.get(i);
            int device = controlCommand.getDeviceAddress();
            if (!devices.contains(device)) {
                devices.add(device);
                ChannelControlDeviceParameter ch = new ChannelControlDeviceParameter();
                ch.setAddress(device);
                channelControlDeviceParameters.add(ch);
            }
        }
        int size = channelControlDeviceParameters.size();
        int contentLength = size * 7;
        int length = baseLength + contentLength;
        byte[] protocols = new byte[length];
        int commandIndex = getCommonbyte(protocols, address,
                ConstantUtil.CONTROL_IDENTIFIER_EXC_46, contentLength);
        byte[] bytes = ControlUtil.getControlData(controlCommands, channelControlDeviceParameters);
        for (int i = 0; i < bytes.length; ++i) {
            protocols[commandIndex] = bytes[i];
            commandIndex++;
        }
        //校验码索引
        int checkIndex = commandIndex;
        check(protocols, checkIndex);
        return protocols;
    }

    /**
     * 设置定时参数
     * @param mac 设备mac地址
     * @param timerVO 定时参数
     * @return
     */
    public static byte[] setTimer(String mac, List<ControlLoopTimerDTO> timerVO) {
        if (timerVO == null || timerVO.isEmpty()) {
            return null;
        }
        Timer timer = new Timer();
        List<CanTiming> canTimings = new ArrayList<>();
        for (ControlLoopTimerDTO timerDTO : timerVO) {
            CanTiming timing = new CanTiming();
            timing.setTagId(AnalysisUtil.getSceneIdByLoopNum(timerDTO.getLoopNum(), timerDTO.getIsOpen()));
            timing.setType(timerDTO.getType());
            timing.setStartDate(timerDTO.getStartDate());
            timing.setEndDate(timerDTO.getEndDate());
            timing.setIsExecute(1);
            if (timerDTO.getType().equals(1)) {
                //时间点定时模式
                //时间处理
                String[] times2 = timerDTO.getTime().split(":");
                byte[] time2 = new byte[3];
                time2[0] = HexUtil.getByte(Integer.parseInt(times2[0]));
                time2[1] = HexUtil.getByte(Integer.parseInt(times2[1]));
                time2[2] = HexUtil.getByte(Integer.parseInt(times2[2]));
                timing.setTime(HexUtil.bytesTohex(time2));
                if (timerDTO.getCycleTypes() != null) {
                    //周期定时模式
                    int cycleType = ProtocolUtil.getCycleType(timerDTO.getCycleTypes(), 0, 1);
                    timing.setCycleType(cycleType);
                } else {
                    //年月日定时
                    timing.setCycleType(1);
                }
            } else {
                //经纬度定时
                timing.setCycleType(0);
                timing.setMinuteValue(timerDTO.getMinuteValue() == null ? 0 : timerDTO.getMinuteValue());
            }
            canTimings.add(timing);
        }
        timer.setCanTimings(canTimings);
        return setTimer(mac, timer);
    }
}
