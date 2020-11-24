package com.exc.street.light.sl.utils;

import com.exc.street.light.resource.entity.sl.LampStrategyAction;
import com.exc.street.light.resource.entity.sl.LampStrategyActionHistory;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.sl.VO.SetStrategyParamVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShuncomMessageGeneration {


    private static final Logger logger = LoggerFactory.getLogger(ShuncomMessageGeneration.class);

    public static void main(String[] args) {


    }

    /*
    * 以下是顺舟灯具方法
    * */

    /**
     * 生成采集信息指令collectinFormation
     * 生成电能查询指令powerQuery
     * 生成组号查询指令groupNumQuery
     * 生成本地时间读取指令timeRead
     * @param adr
     * @param frameHeadMatch
     * @return
     */
    public static String fixedInfo(String adr,FrameHeadMatch frameHeadMatch){
        String info = "FF";
        return MessageOperationUtil.generateMessage(frameHeadMatch,adr,info);
    }
    /**
     * 生成分组采集信息指令
     * @return
     */
    public static String groupRead(String group){
        String adr = "FFFFFFFF";
        String info = group;
        return MessageOperationUtil.generateMessage(FrameHeadMatch.GROUPREAD,adr,info);
    }
    /**
     * 生成设置组号指令
     * @param adr
     * @param group
     * @return
     */
    public static String setGroup(String adr,String group){
        String info = group;
        return MessageOperationUtil.generateMessage(FrameHeadMatch.SETGROUP,adr,info);
    }
    /**
     * 生成控制灯具指令
     * @param adr
     * @param port
     * @param action
     * @param brightness
     * @return
     */
    public static String control(String adr,String port,String action,int brightness){
        String info = port;
        if("05".equals(port)){
            //表示开关灯
            info += action;
        }else if("04".equals(port)){
            //表示调节亮度
            byte[] bytes = HexUtil.intToByteArray(brightness);
            byte[] newBytes = {bytes[3]};
            info += HexUtil.bytesTohex(newBytes).replace(" ", "");
        }else{
            return "";
        }
        return MessageOperationUtil.generateMessage(FrameHeadMatch.CONTROL,adr,info);
    }
    /**
     * 生成分组控制灯具指令
     * @param group
     * @param port
     * @param action
     * @param brightness
     * @return
     */
    public static String groupControl(String group,String port,String action,int brightness){
        String adr = "FFFFFFFF";
        String info = group + port;
        if("05".equals(port)){
            //表示开关灯
            info += action;
        }else if("04".equals(port)){
            //表示调节亮度
            byte[] bytes = HexUtil.intToByteArray(brightness);
            byte[] newBytes = {bytes[3]};
            info += HexUtil.bytesTohex(newBytes).replace(" ", "");
        }
        return MessageOperationUtil.generateMessage(FrameHeadMatch.GROUPCONTROL,adr,info);
    }
    /**
     * 生成电流告警阀值修改指令
     * @param adr
     * @param function
     * @param threshold(单位：A)
     * @return
     */
    public static String currentAlarm(String adr,String function,Integer threshold){
        String thresholdString = HexUtil.intToHexString(threshold*1000);
        String info = "01" + function + thresholdString;
        return MessageOperationUtil.generateMessage(FrameHeadMatch.CURRENTALARM,adr,info);
    }
    /**
     * 生成电压告警阀值修改指令
     * @param adr
     * @param function
     * @param threshold(单位：V)
     * @return
     */
    public static String voltageAlarm(String adr,String function,Integer threshold){
        String thresholdString = HexUtil.intToHexString(threshold*100);
        String info = "02" + function + thresholdString;
        return MessageOperationUtil.generateMessage(FrameHeadMatch.VOLTAGEALARM,adr,info);
    }
    /**
     * 生成本地时间设置指令
     * @param adr
     * @param date
     * @return
     */
    public static String setTime(String adr,Date date){
        String info = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        String specificDate = time.substring(2, 10);
        String specificTime = time.substring(11, 19);
        String[] specificDates = specificDate.split("-");
        String[] specificTimes = specificTime.split(":");
        for (String s : specificDates) {
            String temp = HexUtil.intToHexStringOne(Integer.parseInt(s));
            info += temp;
        }
        for (String s : specificTimes) {
            String temp = HexUtil.intToHexStringOne(Integer.parseInt(s));
            info += temp;
        }
        return MessageOperationUtil.generateMessage(FrameHeadMatch.SETTIME,adr,info);
    }
    /**
     * 生成设置单灯地址指令
     * @param adr
     * @return
     */
    public static String setAddress(String adr,String finallyAdr){
        String info = "D8" + finallyAdr;
        return MessageOperationUtil.generateMessage(FrameHeadMatch.SETADDRESS,adr,info);
    }
    /**
     * 生成设置策略指令
     * @param adr
     * @param setStrategyParamVO
     * @return
     */
    public static String setStrategy(String adr,SetStrategyParamVO setStrategyParamVO){
        String strategyNum = setStrategyParamVO.getStrategyNum();
        String enable = setStrategyParamVO.getEnable();
        String executionTimeOne = setStrategyParamVO.getExecutionTimeOne();
        String executionTypeOne = setStrategyParamVO.getExecutionTypeOne();
        String executionTimeTwo = setStrategyParamVO.getExecutionTimeTwo();
        String executionTypeTwo = setStrategyParamVO.getExecutionTypeTwo();
        String info = "D4" + strategyNum + enable + executionTimeOne + executionTypeOne + executionTimeTwo + executionTypeTwo;
        return MessageOperationUtil.generateMessage(adr,info);
    }
    /**
     * 生成查询策略指令
     * @param adr
     * @param strategyNum
     * @return
     */
    public static String queryStrategy(String adr,String strategyNum){
        String info = "D3" + strategyNum;
        return MessageOperationUtil.generateMessage(adr,info);
    }
    /**
     * 生成策略使能指令
     * @param adr
     * @param scenelal
     * @param sceneTime
     * @return
     */
    public static String scene(String adr,String scenelal,String sceneTime){
        String info = "D2" + scenelal + sceneTime;
        return MessageOperationUtil.generateMessage(adr,info);
    }
    /**
     * 生成查询策略使能指令
     * @param adr
     * @return
     */
    public static String queryScene(String adr){
        String info = "D2";
        return MessageOperationUtil.generateMessage(adr,info);
    }
}
