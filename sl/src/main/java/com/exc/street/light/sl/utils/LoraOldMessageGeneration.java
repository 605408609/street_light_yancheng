package com.exc.street.light.sl.utils;

import com.exc.street.light.resource.entity.sl.LampStrategyAction;
import com.exc.street.light.resource.entity.sl.LampStrategyActionHistory;
import com.exc.street.light.resource.utils.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoraOldMessageGeneration {

    private static RedisUtil redisUtil;

    @Autowired
    public void redisUtil(RedisUtil redisUtil){
        LoraOldMessageGeneration.redisUtil = redisUtil;
    }

    private static final Logger logger = LoggerFactory.getLogger(LoraOldMessageGeneration.class);

    public static void main(String[] args) {

    }

    /**
     * 生成设置经纬度的报文
     *
     *
     * @return
     */
    public static String loraOldLongitudeAndLatitude(String longitude, String latitude) {
        String DR = "10";
        String ADD = "2014";
        String LENGTH = "04";
        String INFO = "";
        String[] longitudeSplit = longitude.split(" ");
        String[] latitudeSplit = latitude.split(" ");
        String longitudeDegree = HexUtil.intToHexStringOne(Integer.parseInt(longitudeSplit[0]));
        String longitudeMinute = HexUtil.intToHexStringOne(Integer.parseInt(longitudeSplit[1]));
        String longitudeSecond = HexUtil.intToHexStringOne(Integer.parseInt(longitudeSplit[2]));
        String longitudeSign = longitudeSplit[3];
        String latitudeDegree = HexUtil.intToHexStringOne(Integer.parseInt(latitudeSplit[0]));
        String latitudeMinute = HexUtil.intToHexStringOne(Integer.parseInt(latitudeSplit[1]));
        String latitudeSecond = HexUtil.intToHexStringOne(Integer.parseInt(latitudeSplit[2]));
        String latitudeSign = latitudeSplit[3];
        if("W".equals(longitudeSign)){
            INFO = INFO + "01";
        }else {
            INFO = INFO + "00";
        }
        INFO = INFO + longitudeDegree + longitudeMinute + longitudeSecond;
        if("N".equals(latitudeSign)){
            INFO = INFO + "00";
        }else {
            INFO = INFO + "01";
        }
        INFO = INFO + latitudeDegree + latitudeMinute + latitudeSecond;
        List<List<String>> bodyLists = new ArrayList<>();
        List<String> bodyList = new ArrayList<>();
        bodyList.add(ADD);
        bodyList.add(LENGTH);
        bodyList.add(INFO);
        bodyLists.add(bodyList);
        return MessageOperationUtil.generateMessage("00000000",1,bodyLists);
    }

    /**
     * 生成设置ip的报文
     *
     *
     * @return
     */
    public static String loraOldIpMessage( String ip) {
        String DR = "10";
        String ADD = "2010";
        String LENGTH = "03";
        String[] ipSplit = ip.split("\\.");
        String ip1 = HexUtil.intToHexStringOne(Integer.parseInt(ipSplit[0]));
        String ip2 = HexUtil.intToHexStringOne(Integer.parseInt(ipSplit[1]));
        String ip3 = HexUtil.intToHexStringOne(Integer.parseInt(ipSplit[2]));
        String ipTemp = ipSplit[3];
        String[] split = ipTemp.split(":");
        String ip4 = HexUtil.intToHexStringOne(Integer.parseInt(split[0]));
        String port = split[1];
        String INFO = ip1 + ip2 + ip3 + ip4 + HexUtil.intToHexString(Integer.parseInt(port));
        List<List<String>> bodyLists = new ArrayList<>();
        List<String> bodyList = new ArrayList<>();
        bodyList.add(ADD);
        bodyList.add(LENGTH);
        bodyList.add(INFO);
        bodyLists.add(bodyList);
        return MessageOperationUtil.generateMessage("00000000",1,bodyLists);
    }

    /**
     * 生成ota包信息
     */
    public static List<String> loraOldOta(List<String> messageInfoList){
        List<String> messageList = new ArrayList<>();
        String DR = "10";
        int addBegin = 9984;
        for (int i = 0; i < messageInfoList.size(); i++){
            String INFO = messageInfoList.get(i);
            int AddNum = addBegin+i;
            String ADD = HexUtil.intToHexString(AddNum);
            if(INFO.length()%4!=0){
                return null;
            }
            int length = INFO.length()/4;
            String LENGTH = HexUtil.intToHexStringOne(length);
            //String message = MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
            List<List<String>> bodyLists = new ArrayList<>();
            List<String> bodyList = new ArrayList<>();
            bodyList.add(ADD);
            bodyList.add(LENGTH);
            bodyList.add(INFO);
            bodyLists.add(bodyList);
            String message = MessageOperationUtil.generateMessage("00000000",1,bodyLists);
            messageList.add(message);
        }
        return messageList;
    }

    /**
     * 生成ota准备报文
     *
     * @param INFO
     * @return
     */
    public static String loraOldOtaReady(String ADD,String INFO) {
        String DR = "06";
        String LENGTH = "";
        return MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
    }

    /**
     * 生成ota长度报文
     *
     * @param INFO
     * @return
     */
    public static String loraOldOtaSize(String INFO) {
        String ADD = "200C";
        String DR = "06";
        String LENGTH = "";
        return MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
    }

    /**
     * 生成ota发送完成的报文
     *
     * @param INFO
     * @return
     */
    public static String loraOldOtaSendEnd(String INFO) {
        String ADD = "1008";
        String DR = "06";
        String LENGTH = "";
        return MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
    }

    /**
     * 生成获取设备本地时间的报文
     *
     * @return
     */
    public static String loraOldGetTime() {
        //生成读取当前亮度报文
        String DR = "03";
        String ADD = "1003";
        String LENGTH = "03";
        String INFO = "";
        String message = MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
        return message;
    }


    /**
     * 读取当前亮度(暂时不用)
     *
     *
     * @return
     */
    public static String loraOldReadBrightness() {
        //生成读取当前亮度报文
        String DR = "03";
        String ADD = "1002";
        String LENGTH = "01";
        String INFO = "";
        String message = MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
        return message;
    }

    /**
     * 生成开灯和指定亮度报文（开灯必须指定亮度）
     *
     *
     * @return
     */
    public static String loraOldTurnOnLight( int brightness) {
        String DR = "10";
        String ADD = "1001";
        String LENGTH = "02";
        byte[] bytes = HexUtil.intToByteArray(brightness);
        byte[] newBytes = {bytes[3], bytes[2]};
        String brightnessString = HexUtil.bytesTohex(newBytes).replace(" ", "");
        String INFO = "0100" + brightnessString;
        List<List<String>> bodyLists = new ArrayList<>();
        List<String> bodyList = new ArrayList<>();
        bodyList.add(ADD);
        bodyList.add(LENGTH);
        bodyList.add(INFO);
        bodyLists.add(bodyList);
        return MessageOperationUtil.generateMessage("00000000",1,bodyLists);
        //return MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
    }

    /**
     * 生成关灯报文
     *
     *
     * @return
     */
    public static String loraOldTurnOffLight() {
        String DR = "06";
        String ADD = "1001";
        String LENGTH = "";
        String INFO = "0000";
        return MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
    }


    /**
     * 单灯操作报文
     *
     *
     * @return
     */
    public static String loraOldSingleLampControl( int brightness, String state,Integer routesNum) {
        if (brightness == 0) {
            state = "00";
        }
        String DR = "10";
        //String ADD = "1001";
        String ADD = "";
        if(routesNum==1){
            ADD = "1009";
        }else {
            ADD = "100A";
        }
        //String LENGTH = "02";
        String LENGTH = "01";
        String brightnessString = HexUtil.intToHexStringOne(brightness);
        /*byte[] bytes = HexUtil.intToByteArray(brightness);
        byte[] newBytes = {bytes[3], bytes[2]};
        String brightnessString = HexUtil.bytesTohex(newBytes).replace(" ", "");*/
        /*String INFO = "";
        if(routesNum!=null){
            if (routesNum==1){
                INFO = state + "FF" + brightnessString + "FF";
            }else {
                INFO = "FF" + state + "FF" +brightnessString;
            }
        }else {
            return "";
        }*/
        String INFO = state + brightnessString;
        List<List<String>> bodyLists = new ArrayList<>();
        List<String> bodyList = new ArrayList<>();
        bodyList.add(ADD);
        bodyList.add(LENGTH);
        bodyList.add(INFO);
        bodyLists.add(bodyList);
        return MessageOperationUtil.generateMessage("00000000",1,bodyLists);
        //return MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
    }

    /**
     * 双灯操作报文
     *
     *
     * @return
     */
    public static String loraOldSingleLampControl( int brightnessOne, String stateOne , int brightnessTwo, String stateTwo) {
        if (brightnessOne == 0) {
            stateOne = "00";
        }
        if (brightnessTwo == 0) {
            stateTwo = "00";
        }
        String DR = "10";
        //String ADD = "1001";
        String ADD = "1009";
        String LENGTH = "02";
        String brightnessOneString = HexUtil.intToHexStringOne(brightnessOne);
        String brightnessTwoString = HexUtil.intToHexStringOne(brightnessTwo);
        /*byte[] bytes = HexUtil.intToByteArray(brightness);
        byte[] newBytes = {bytes[3], bytes[2]};
        String brightnessString = HexUtil.bytesTohex(newBytes).replace(" ", "");*/
        String INFO = "";
        //INFO = stateOne + stateTwo + brightnessOneString + brightnessTwoString;
        INFO = stateOne + brightnessOneString + stateTwo + brightnessTwoString;
        List<List<String>> bodyLists = new ArrayList<>();
        List<String> bodyList = new ArrayList<>();
        bodyList.add(ADD);
        bodyList.add(LENGTH);
        bodyList.add(INFO);
        bodyLists.add(bodyList);
        return MessageOperationUtil.generateMessage("00000000",1,bodyLists);
        //return MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
    }

    /**
     * 调整亮度功能（亮度为0为关灯状态（0-100））
     *
     *
     * @return
     */
    public static String loraOldAdjustBrightness( int brightness) {
        //判断调整的亮度是否为0
        String message = "";
        //生成关灯和调整灯光的报文
        String DR = "06";
        String ADD = "1002";
        String LENGTH = "";
        byte[] bytes = HexUtil.intToByteArray(brightness);
        byte[] newBytes = {bytes[3], bytes[2]};
        String INFO = HexUtil.bytesTohex(newBytes).replace(" ", "");
        message = MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
        return message;
    }

    /**
     * 生成设置报警电压的报文（默认260v）
     * 生成设置报警电流的报文（默认10000mA）
     * 生成设置报警温度的报文（默认850 * 0.1°C）
     * 生成设置上报时间间隔的报文（默认300s）
     * @return
     */
    public static String loraOldSetParam(int param, WithParamMatch withParamMatch) {
        String ADD = withParamMatch.getId();
        String DR = "06";
        String LENGTH = "";
        String INFO = HexUtil.intToHexString(param);
        return MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
    }

    /**
     * 生成设置灯杆倾斜角度的报文（°）
     * @param tiltAngle
     * @return
     */
    public static String loraOldTiltAngle(int tiltAngle) {
        String ADD = "200F";
        String DR = "06";
        String LENGTH = "";
        String INFO = "FF" + HexUtil.intToHexStringOne(tiltAngle);
        return MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
    }

    /**
     * 生成设置所有参数清零的报文（恢复出厂设置）
     * @param noParamMatch
     * @return
     */
    public static String loraOldNoParam(NoParamMatch noParamMatch) {
        String ADD = noParamMatch.getId();
        String INFO = noParamMatch.getInfo();
        String DR = "06";
        String LENGTH = "";
        return MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
    }

    /**
     * 生成日时间表（策略）报文集合
     *
     *
     * @param lampStrategyActionList
     * @return
     */
    public static List<String> loraOldDailySchedule2( List<LampStrategyAction> lampStrategyActionList) {

        /*List<String> messageList = new ArrayList<>();
        //时间表总量
        int size  = lampStrategyActionList.size();
        //场景总包数量（1字节）
        int packageSize = (size - 1) / 3 + 1;
        String prefix = HexUtil.intToHexString(packageSize).substring(2, 4);
        //每个包中的时间表数量
        int[] schedulesNum = new int[packageSize];
        for (int i = 0; i < packageSize; i++) {
            schedulesNum[i] = 3;
        }
        if (size % 3 != 0) {
            schedulesNum[packageSize - 1] = size % 3;
        }
        //生成INFO
        for (int i = 0; i < packageSize; i++) {
            //当前包序号（1字节）
            String INFO = prefix + HexUtil.intToHexString(i).substring(2, 4);
            //当前包时间表数量（2字节）
            INFO = INFO + HexUtil.intToHexString(schedulesNum[i]);

            for (int j = 0; j < schedulesNum[i]; j++) {
                LampStrategyAction lampStrategyAction = lampStrategyActionList.get(3 * i + j);
                Integer sceneNum = lampStrategyAction.getScene();
                String scene = HexUtil.intToHexString(sceneNum).substring(2, 4);
                if (lampStrategyAction != null) {
                    //场景值
                    INFO = INFO + scene;
                    INFO = INFO + loraOldScheduleInfo(lampStrategyAction);
                }
            }
            //生成报文
            String DR = "10";
            String ADD = "201E";
            String LENGTH = HexUtil.intToHexString(INFO.length() / 4).substring(2, 4);
            List<List<String>> bodyLists = new ArrayList<>();
            List<String> bodyList = new ArrayList<>();
            bodyList.add(ADD);
            bodyList.add(LENGTH);
            bodyList.add(INFO);
            bodyLists.add(bodyList);
            String message =  MessageOperationUtil.generateMessage("00000000",1,bodyLists);
            //String message = MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
            messageList.add(message);
        }
        //生成报文
        return messageList;*/
        return null;
    }

    /**
     * 生成时间表报文集合（新协议）
     * @param loopNums
     *
     * @param lampStrategyActionList
     * @return
     */
    public static List<String> loraOldDailySchedule(List<Integer> loopNums, List<LampStrategyAction> lampStrategyActionList,Integer scene,List<Integer> strategyHistoryIdList) {

        List<String> messageList = new ArrayList<>();
        Integer strategyId = -1;
        if(lampStrategyActionList!=null&&lampStrategyActionList.size()>0){
            strategyId = lampStrategyActionList.get(0).getStrategyId();
        }else {
            return null;
        }
        //时间表总量
        int size  = lampStrategyActionList.size();
        //场景总包数量（1字节）
        int packageSize = (size - 1) / 3 + 1;
        String prefix = HexUtil.intToHexStringOne(packageSize);
        //每个包中的时间表数量
        int[] schedulesNum = new int[packageSize];
        for (int i = 0; i < packageSize; i++) {
            schedulesNum[i] = 3;
        }
        if (size % 3 != 0) {
            schedulesNum[packageSize - 1] = size % 3;
        }
        //生成INFO
        for (int i = 0; i < packageSize; i++) {
            //当前包序号（1字节）
            String INFO = prefix + HexUtil.intToHexStringOne(i);
            //当前包时间表数量（2字节）
            INFO = INFO + HexUtil.intToHexString(schedulesNum[i]);

            for (int j = 0; j < schedulesNum[i]; j++) {
                LampStrategyAction lampStrategyAction = lampStrategyActionList.get(3 * i + j);
                String sceneString = HexUtil.intToHexString(scene);
                if (lampStrategyAction != null) {
                    //场景值
                    Integer lightModeId = lampStrategyAction.getLightModeId();
                    if(lightModeId!=null){
                        switch (lightModeId){
                            case 1:
                                INFO = INFO + sceneString + HexUtil.intToHexStringOne(4);
                                break;
                            case 2:
                                INFO = INFO + sceneString + HexUtil.intToHexStringOne(5);
                                break;
                            case 3:
                                INFO = INFO + sceneString + HexUtil.intToHexStringOne(2);
                                break;
                            case 4:
                                INFO = INFO + sceneString + HexUtil.intToHexStringOne(3);
                                break;
                            default:
                                return null;
                        }
                    }else {
                        INFO = INFO + sceneString + "01";
                    }
                    INFO = INFO + loraOldScheduleInfo(loopNums,lampStrategyAction);
                }
            }
            //生成报文
            String DR = "10";
            String ADD = "201E";
            String LENGTH = HexUtil.intToHexString(INFO.length() / 4).substring(2, 4);
            List<List<String>> bodyLists = new ArrayList<>();
            List<String> bodyList = new ArrayList<>();
            bodyList.add(ADD);
            bodyList.add(LENGTH);
            bodyList.add(INFO);
            bodyLists.add(bodyList);

            /*String crc16Redis = Crc16.generateCrc16(String.valueOf(System.currentTimeMillis())+deviceIds.get(0)+deviceIds.get(0));
            List<Integer> idList = new ArrayList<>();
            idList.add(strategyId);
            idList.addAll(deviceIds);
            redisUtil.set(crc16Redis,idList);*/
            String s = "";
            for (Integer strategyHistoryId : strategyHistoryIdList) {
                s += strategyHistoryId;
            }
            String crc16Redis = Crc16.generateCrc16(s + String.valueOf(System.currentTimeMillis()) + (int)((Math.random()*9+1)*100000));
            redisUtil.set(crc16Redis,strategyHistoryIdList);
            redisUtil.expire(crc16Redis,86400);
            List<String> bodyListUniqueness = new ArrayList<>();
            bodyListUniqueness.add("2019");
            bodyListUniqueness.add("01");
            bodyListUniqueness.add(crc16Redis);
            bodyLists.add(bodyListUniqueness);

            String message =  MessageOperationUtil.generateMessage("00000000",2,bodyLists);
            //String message = MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
            messageList.add(message);
        }
        //生成报文
        return messageList;
    }


    /**
     * 生成日时间表（策略）报文集合
     *
     *
     * @param lampStrategyActionList
     * @return
     */
    public static List<String> loraOldDailySchedule( List<LampStrategyAction> lampStrategyActionList) {

        List<String> messageList = new ArrayList<>();
        //时间表总量
        int size = lampStrategyActionList.size();
        //场景总包数量（1字节）
        int packageSize = (size - 1) / 3 + 1;
        String prefix = HexUtil.intToHexString(packageSize).substring(2, 4);
        //每个包中的时间表数量
        int[] schedulesNum = new int[packageSize];
        for (int i = 0; i < packageSize; i++) {
            schedulesNum[i] = 3;
        }
        if (size % 3 != 0) {
            schedulesNum[packageSize - 1] = size % 3;
        }

        //生成INFO
        for (int i = 0; i < packageSize; i++) {
            //当前包序号（1字节）
            String INFO = prefix + HexUtil.intToHexString(i).substring(2, 4);
            //当前包时间表数量（2字节）
            INFO = INFO + HexUtil.intToHexString(schedulesNum[i]);

            for (int j = 0; j < schedulesNum[i]; j++) {
                LampStrategyAction lampStrategyAction = lampStrategyActionList.get(3 * i + j);
                if (lampStrategyAction != null) {
                    //场景值
                    INFO = INFO + HexUtil.intToHexString(3 * i + j).substring(2, 4);
                    INFO = INFO + loraOldScheduleInfo(lampStrategyAction);
                }
            }
            //生成报文
            String DR = "10";
            String ADD = "201E";
            String LENGTH = HexUtil.intToHexString(INFO.length() / 4).substring(2, 4);
            List<List<String>> bodyLists = new ArrayList<>();
            List<String> bodyList = new ArrayList<>();
            bodyList.add(ADD);
            bodyList.add(LENGTH);
            bodyList.add(INFO);
            bodyLists.add(bodyList);
            String message =  MessageOperationUtil.generateMessage("00000000",1,bodyLists);
            //String message = MessageOperationUtil.generateMessage("00000000", DR, ADD, LENGTH, INFO);
            messageList.add(message);
        }
        //生成报文
        return messageList;
    }

    /**
     * 取出lampStrategyActionHistory中的策略信息
     *
     * @param lampStrategyAction
     * @return
     */
    public static String loraOldScheduleInfo(LampStrategyAction lampStrategyAction) {

        /*String scheduleInfo = "";
        //执行模式
        Integer executionMode = lampStrategyAction.getExecutionMode();
        if (executionMode == 1) {
            //每天
            scheduleInfo = scheduleInfo + "FE";
        } else if (executionMode == 2) {
            //每周
            Integer weekValue = lampStrategyAction.getWeekValue();
            byte[] bytes = HexUtil.intToByteArray(weekValue);
            byte[] newBytes = {bytes[3]};
            String weekString = HexUtil.bytesTohex(newBytes).replace(" ", "");
            System.out.println(weekString);
            scheduleInfo = scheduleInfo + weekString;
        } else {
            System.out.println("执行模式错误");
        }
        //策略动作类型
        Integer type = lampStrategyAction.getType();
        String typeString = "";
        if (type == 1) {
            //开灯
            typeString = "0100";
            Integer brightness = lampStrategyAction.getBrightness();
            byte[] bytes = HexUtil.intToByteArray(brightness);
            byte[] newBytes = {bytes[3], bytes[2]};
            typeString = typeString + HexUtil.bytesTohex(newBytes).replace(" ", "");
        } else if (type == 0) {
            //关灯
            typeString = "0000";
            Integer brightness = lampStrategyAction.getBrightness();
            byte[] bytes = HexUtil.intToByteArray(brightness);
            byte[] newBytes = {bytes[3], bytes[2]};
            typeString = typeString + HexUtil.bytesTohex(newBytes).replace(" ", "");

        }
        scheduleInfo = scheduleInfo + typeString;
        //执行时间（时分秒）
        scheduleInfo = scheduleInfo + "000000";
        String executionTime = lampStrategyAction.getExecutionTime();
        String[] split = executionTime.split(":");
        for (String s : split) {
            scheduleInfo = scheduleInfo + HexUtil.intToHexString(Integer.parseInt(s)).substring(2, 4);
        }
        return scheduleInfo;*/
        return null;
    }

    /**
     * 取出lampStrategyActionHistory中的策略信息
     *
     * @param lampStrategyAction
     * @return
     */
    public static String loraOldScheduleInfo(List<Integer> loopNums,LampStrategyAction lampStrategyAction) {
        String startDate = lampStrategyAction.getStartDate();
        String endDate = lampStrategyAction.getEndDate();
        String[] startDates = startDate.split("-");
        String[] endDates = endDate.split("-");
        String startYear = HexUtil.intToHexStringOne(Integer.parseInt(startDates[0].substring(2,4)));
        String startMonth = HexUtil.intToHexStringOne(Integer.parseInt(startDates[1]));
        String startDay = HexUtil.intToHexStringOne(Integer.parseInt(startDates[2]));
        String endYear = HexUtil.intToHexStringOne(Integer.parseInt(endDates[0].substring(2,4)));
        String endMonth = HexUtil.intToHexStringOne(Integer.parseInt(endDates[1]));
        String endDay = HexUtil.intToHexStringOne(Integer.parseInt(endDates[2]));
        String scheduleInfo = startYear + startMonth + startDay + endYear + endMonth + endDay;
        //执行模式
        scheduleInfo = scheduleInfo + "FE";
        /*Integer executionMode = lampStrategyAction.getExecutionMode();
        if (executionMode == 1) {
            //每天
            scheduleInfo = scheduleInfo + "FE";
        } else if (executionMode == 2) {
            //每周
            Integer weekValue = lampStrategyAction.getWeekValue();
            byte[] bytes = HexUtil.intToByteArray(weekValue);
            byte[] newBytes = {bytes[3]};
            String weekString = HexUtil.bytesTohex(newBytes).replace(" ", "");
            System.out.println(weekString);
            scheduleInfo = scheduleInfo + weekString;
        } else {
            System.out.println("执行模式错误");
        }*/
        //回路数量及对应动作
        Integer loopTotal = loopNums.get(loopNums.size() - 1);
        scheduleInfo = scheduleInfo + "0" + loopTotal;
        for (int i = 0; i < loopTotal; i++){
            if(loopNums.contains(i+1)){
                //策略动作类型
                Integer type = lampStrategyAction.getIsOpen();
                String typeString = "";
                if (type == 1) {
                    //开灯
                    typeString = "01";
                    Integer brightness = lampStrategyAction.getBrightness();
                    typeString = typeString + HexUtil.intToHexStringOne(brightness);
                } else if (type == 0) {
                    //关灯
                    typeString = "00";
                    Integer brightness = lampStrategyAction.getBrightness();
                    typeString = typeString + HexUtil.intToHexStringOne(brightness);

                }
                scheduleInfo = scheduleInfo + typeString;
            }else {
                scheduleInfo = scheduleInfo + "FFFF";
            }
        }
        Integer lightModeId = lampStrategyAction.getLightModeId();
        if(lightModeId!=null){
            //偏移量
            scheduleInfo = scheduleInfo + "0000" + HexUtil.intToHexStringOne(lampStrategyAction.getDeviation());
        }else {
            //执行时间（时分秒）
            String executionTime = lampStrategyAction.getExecutionTime();
            String[] split = executionTime.split(":");
            for (String s : split) {
                scheduleInfo = scheduleInfo + HexUtil.intToHexStringOne(Integer.parseInt(s));
            }
        }
        return scheduleInfo;
    }

    /**
     * 取出lampStrategyActionHistory中的策略信息
     * @param lampStrategyActionHistory
     * @return
     */
    public static String loraOldScheduleInfo(LampStrategyActionHistory lampStrategyActionHistory){
        String scheduleInfo = "";
        //执行模式
        Integer executionMode = lampStrategyActionHistory.getExecutionMode();
        if (executionMode == 1) {
            //每天
            scheduleInfo = scheduleInfo + "FE";
        } else if (executionMode == 2) {
            //每周
            Integer weekValue = lampStrategyActionHistory.getWeekValue();
            String weekString = WeekMatch.Sunday.getNumById(weekValue);
            scheduleInfo = scheduleInfo + weekString;
        } else {
            System.out.println("执行模式错误");
        }
        //策略动作类型
        Integer type = lampStrategyActionHistory.getType();
        String typeString = "";
        if (type == 1) {
            //开灯
            typeString = "0100";
            Integer brightness = lampStrategyActionHistory.getBrightness();
            byte[] bytes = HexUtil.intToByteArray(brightness);
            byte[] newBytes = {bytes[3], bytes[2]};
            typeString = typeString + HexUtil.bytesTohex(newBytes).replace(" ", "");
        } else if (type == 0) {
            //关灯
            typeString = "0000";
        }
        scheduleInfo = scheduleInfo + typeString;
        //执行时间（时分秒）
        scheduleInfo = scheduleInfo + "000000";
        String executionTime = lampStrategyActionHistory.getExecutionTime();
        String[] split = executionTime.split(":");
        for (String s : split) {
            scheduleInfo = scheduleInfo + HexUtil.intToHexString(Integer.parseInt(s)).substring(2, 4);
        }
        return scheduleInfo;
    }

}
