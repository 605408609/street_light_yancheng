package com.exc.street.light.lj.utils;


import com.exc.street.light.lj.vo.TrashVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MessageParse {

    private static final Logger logger = LoggerFactory.getLogger(MessageParse.class);


    public static void main(String[] args) {
        MessageParse.getInformation("01020100de011800a3b3320110006400a3ffffff00794000525f7f070d00ffff2797");

    }

    /**
     * 解析报文（中星）
     */

    public static List<String> parseMessage(String message) {
        List<String> resultList = new ArrayList<>();
        String VER = "";   //协议版本号
        String FUNC = "";  //功能码
        String ID = "";     //终端 ID
        String NUMBER = "";//消息序号
        String LENGTH = "";//数据长度
        String DATA = "";  //数据

        //先校验Crc16是否正确，直接截取最后两个字节，四个字符，进行校验
        int length = message.length();
        //最后四个字符
        String crc16String = message.substring(length - 4);
        //除最后四个字符外的字符
        String pendingCheckString = message.substring(0, length - 4);

        //进行Crc16校验
        String Crc16Value = Crc16.generateCrc16(pendingCheckString);
        if (Crc16Value.equals(crc16String)) {
            VER = message.substring(0, 2);
            FUNC = message.substring(2, 4);
            ID = message.substring(4, 8);
            NUMBER = message.substring(8, 12);
            String Length1 = message.substring(12, 14);
            String Length2 = message.substring(14, 16);
            //长度高低位
            LENGTH = Length2 + Length1;
            int LengthValue = Integer.valueOf(LENGTH, 16);
            DATA = message.substring(16, 16 + LengthValue * 2);
        } else {
            //System.out.println("Crc16校验不成功，阻止操作");
            logger.info("Crc16校验不成功，阻止操作");
        }
        //添加数据格式
        resultList.add(VER);
        resultList.add(FUNC);
        resultList.add(ID);
        resultList.add(NUMBER);
        resultList.add(LENGTH);
        resultList.add(DATA);
        return resultList;
    }


    /**
     * 通过报文获取数据
     *
     * @param messager
     * @return
     */
    public static TrashVo getInformation(String messager) {

        TrashVo trashVo = new TrashVo();

        //解析报文 获取
        List<String> returnList = MessageParse.parseMessage(messager);
        String VER = returnList.get(0);
        String FUNC = returnList.get(1);
        String ID = returnList.get(2);
        String NUMBER = returnList.get(3);
        String DATA = returnList.get(5);
        //判断协议版本号是否需要应答(0需要应答 1不需要应答)
        if ("00".equals(VER)) {
            VER = VER;
            ID = ID;
            NUMBER = NUMBER;
            DATA = FUNC;
        } else if ("01".equals(VER)) {
            //根据不同功能码 解析报文消息
            switch (FUNC) {
                //开机信息报文
                case "01":
                    try {
                        //终端id
                        int id = Integer.parseInt(MessageParse.convert(ID), 16);
                        //产品SN
                        String Sn = messager.substring(8 * 2, 12 * 2);
                        int sn = Integer.parseInt(MessageParse.convert4(Sn), 16);
                        //设备类型
                        String typeStr = messager.substring(12 * 2, 13 * 2);
                        int type = Integer.parseInt(typeStr, 16);
                        //硬件版本 HW
                        String HwStr = messager.substring(13 * 2, 14 * 2);
                        String hw = MessageParse.convertHexToString(HwStr);
                        //软件版本
                        String SwStr = messager.substring(14 * 2, 18 * 2);
                        //定时上报时间间隔
                        String timingTimeStr = messager.substring(20 * 2, 24 * 2);
                        int timingTime = Integer.parseInt(MessageParse.convert(timingTimeStr), 16);
                        //采样时间间隔
                        String SamplingTimeStr = messager.substring(24 * 2, 26 * 2);
                        int samplingTime = Integer.parseInt(MessageParse.convert(SamplingTimeStr), 16);
                        //报警深度阈值
                        String alarmDepthStr = messager.substring(58 * 2, 60 * 2);
                        int alarmDepth = Integer.parseInt(MessageParse.convert(alarmDepthStr), 16);
                        //添加到VO类中
                        trashVo.setId(id);
                        trashVo.setSn(sn);
                        trashVo.setHw(hw);
                        trashVo.setType(type);
                        trashVo.setTimingTime(timingTime);
                        trashVo.setSamplingTime(samplingTime);
                        trashVo.setAlarmDepth(alarmDepth);

                        System.out.println(trashVo);
                    } catch (Exception e) {
                        //System.out.println("解析开机上报出错");
                        logger.info("解析开机上报出错");
                    }
                    break;
                //定时上报报文
                case "02":

                    try {
                        //终端id
                        int id = Integer.parseInt(MessageParse.convert(ID), 16);
                        trashVo.setId(id);
                        //产品SN
                        String Sn2 = messager.substring(8*2, 12*2);
                        int sn = Integer.parseInt(MessageParse.convert4(Sn2), 16);
                        trashVo.setSn(sn);
                        //终端状态
                        String terminalStr = messager.substring(12 * 2, 14 * 2);
                        if (!"0000".equals(terminalStr)) {
                            //报警时间
                            Date date = new Date();
                            trashVo.setDate(date);
                            //将十六进制转为二进制
                            String str = MessageParse.hexString2binaryString(MessageParse.convert(terminalStr));
                            //System.out.println(str);
                            int i = str.indexOf("1");
                            String substring = str.substring(i);
                            int length = substring.length();
                            if (length == 6) {
                                //垃圾桶B报警
                                trashVo.setTrashType(6);
                            } else if (length == 5) {
                                //垃圾桶A报警
                                trashVo.setTrashType(5);
                            } else if (length == 4) {
                                // 设备撤防
                                trashVo.setTrashType(4);
                            } else if (length == 3) {
                                // 无线模块状态异常
                                trashVo.setTrashType(3);
                            } else if (length == 2) {
                                //云端应答错误
                                trashVo.setTrashType(2);
                            } else if (length == 1) {
                                //电池欠压
                                trashVo.setTrashType(1);
                            }
                        } else {
                            //终端设备正常 默认为0
                            Date date = new Date();
                            trashVo.setTrashType(0);
                        }
                        //电池剩余容量
                        String electricStr = messager.substring(14 * 2, 15 * 2);
                        //保留字节
                        String retain = messager.substring(15 * 2, 16 * 2);
                        //信号强度
                        String signalStr = messager.substring(16 * 2, 20 * 2);
                        //桶 A 垃圾深度
                        String trashAStr = messager.substring(28 * 2, 30 * 2);
                        if(!"FFFF".equals(trashAStr)){

                            int trashA = Integer.parseInt(MessageParse.convert(trashAStr), 16);
                            //添加A桶深度
                            trashVo.setAlarmDepthA(trashA);
                        } else {
                            logger.info("垃圾桶A没有连接传感器");
                        }
                        //桶 B 垃圾深度
                        String trashBStr = messager.substring(30 * 2, 32 * 2);
                        if (!"FFFF".equals(trashBStr)) {

                            int trashB = Integer.parseInt(MessageParse.convert(trashBStr), 16);
                            //添加B桶深度
                            trashVo.setAlarmDepthB(trashB);
                        } else {
                            logger.info("垃圾桶B没有连接传感器");
                        }

                    } catch (Exception e) {
                        //System.out.println("解析定时上报出错");
                        logger.info("解析定时上报出错");
                    }
                    break;
                //终端返回应答报文
                default:
                    try {
                        TrashVo trashVoa = new TrashVo();
                        //错误码
                        String Cw = messager.substring(8 * 2, 9 * 2);
                        int cw = Integer.parseInt(Cw, 16);
                        if(cw == 0 ){
                            logger.info("正常");
                        } else if(cw == 1){
                            logger.info("内部错误");
                        } else if(cw == 2){
                            logger.info("CRC校验错误");
                        } else if(cw == 3){
                            logger.info("参数错误");
                        }
                    } catch (Exception e) {
                        //System.out.println("应答报文格式错误");
                        logger.info("应答报文格式错误");
                    }
            }
        }
        return trashVo;
    }


    /**
     * 转换高低位(限2字节)
     *
     * @param str
     * @return
     */
    public static String convert(String str) {
        String str1 = str.substring(0, 2);
        String str2 = str.substring(2, 4);
        return str2 + str1;
    }


    /**
     *
     * @param str
     * @return
     */
    public static String convert4(String str){
        String str1 = str.substring(0, 2);
        String str2 = str.substring(2, 4);
        String str3 = str.substring(4, 6);
        String str4 = str.substring(6, 8);

        return str4+str3+str2+str1;
    }


    /**
     * 生成报文（中星）
     *
     * @param VER    协议版本号
     * @param FUNC   功能码
     * @param ID     终端 ID
     * @param NUMBER 消息序号
     * @param LENGTH 数据长度
     * @param DATA   数据
     * @return
     */
    public static String generateMessage(String VER, String FUNC, String ID, String NUMBER, String LENGTH, String DATA) {

        String message = "";
        message = VER + FUNC + ID + NUMBER + LENGTH + DATA;
        String Crc16Value = Crc16.generateCrc16(message);
        message = message + Crc16Value;
        return message;
    }


    /**
     * 生成开机信息应答报文(应答报文通用)
     */
    public static String start(String message) {
        String VER = "01";
        String FUNCA = message.substring(2, 4);
        String  ID = message.substring(4, 8);
        String  NUMBER = message.substring(8, 12);
        String FUNC = "AA";
        String DATA = "";
        //先校验Crc16是否正确，直接截取最后两个字节，四个字符，进行校验
        int length = message.length();
        //最后四个字符
        String crc16String = message.substring(length - 4);
        //除最后四个字符外的字符
        String pendingCheckString = message.substring(0, length - 4);
        //进行Crc16校验
        String Crc16Value = Crc16.generateCrc16(pendingCheckString);
        if (Crc16Value.equals(crc16String)) {
            String code = "00";
            DATA = code+FUNCA;
        } else {
          String code = "02";
            DATA = code+FUNCA;
        }

        //转为16进制
        String LENGTH = MessageParse.getString(2);
        return MessageParse.generateMessage(VER, FUNC, ID, NUMBER, LENGTH, DATA);
    }


    /**
     * 生成配置命令报文
     * @param termId               终端Id  1-65534
     * @param newId                新的id  用于修改id  传FFFF表示不修改
     * @param reportTimeInterval   上报时间设置 1-1440  单位 min（0为不修改）
     * @param samplingTimeInterval 采样时间间隔 1-1440 单位min
     * @param limitVal             报警深度阈值 10-80cm
     * @param msgNo                消息序号（消息 MID号。自动累加，去重）
     * @return
     */
    public static String order(Integer termId, Integer newId, Integer reportTimeInterval, Integer samplingTimeInterval, Integer limitVal,Integer msgNo) {
        String VER = "01";
        String FUNC = "03";
        String ID = "";
        if (termId >= 0 && termId <= 65534) {
            ID = MessageParse.getString(termId);
        } else {
            System.out.println("终端id超出取值范围");
        }
        String NUMBER = MessageParse.getString(msgNo);
        String LENGTH = MessageParse.getString(18);
        //判断上报时间是否修改
        String reportTimeIntervalStr = "";
        if (reportTimeInterval.equals(0)) {
            reportTimeIntervalStr = "FFFF";
        } else if(reportTimeInterval>=1 && reportTimeInterval<=1440){
            reportTimeIntervalStr = MessageParse.getString(reportTimeInterval);
        } else {
            logger.info("上报时间范围设置不正确");
            return "";
        }
        //判断采样时间间隔是否修改
        String samplingTimeIntervalStr = "";
        if (samplingTimeInterval.equals(0)) {
            samplingTimeIntervalStr = "FFFF";
        } else if(samplingTimeInterval >=1 && samplingTimeInterval<=1440){
            samplingTimeIntervalStr = MessageParse.getString(samplingTimeInterval);
        } else {
            logger.info("采样时间范围设置不正确");
            return "";
        }
        String limitValStr = "";
        //判断报警深度阈值
        if(limitVal>=10 && limitVal<=80){
            limitValStr = MessageParse.getString(limitVal);
        }else {
            logger.info("报警阈值范围设置不正确");
            return "";
        }
        String DATA = "FFFF" + reportTimeIntervalStr + "FFFF" + samplingTimeIntervalStr + "FFFF" + "FFFFFFFF" + limitValStr + "FFFF";
        return MessageParse.generateMessage(VER, FUNC, ID, NUMBER, LENGTH, DATA);
    }

    /**
     * 生成复位命令报文
     *
     * @return
     */
    public static String reset(Integer termId) {
        String VER = "01";
        String FUNC = "04";
        String ID = "";
        if (termId >= 0 && termId <= 65534) {
            ID = MessageParse.getString(termId);
        } else {
            System.out.println("终端id超出取值范围");
        }
        String NUMBER = "0000";
        String LENGTH = "0000";
        String message = VER + FUNC + ID + NUMBER + LENGTH;
        String Crc16Value = Crc16.generateCrc16(message);
        message = message + Crc16Value;
        return message;
    }

    /**
     * 生成布防撤防命令报文
     * @param termId  终端id
     * @param data    布防撤防状态，数据 0 表示布防，数据 1 表示撤防
     * @param msgNo   消息序号（消息 MID号。自动累加，去重）
     * @return
     */
    public static String defence(Integer termId, Integer data,Integer msgNo) {
        String VER = "01";
        String FUNC = "05";
        String ID = "";
        if (termId >= 0 && termId <= 65534) {
            ID = MessageParse.getString(termId);
        } else {
            System.out.println("终端id超出取值范围");
        }
        String NUMBER = MessageParse.getString(msgNo);
        String LENGTH = MessageParse.getString(2);
        String DATA = MessageParse.getString(data);
        return MessageParse.generateMessage(VER, FUNC, ID, NUMBER, LENGTH, DATA);
    }

    /**
     * 生成读取开机信息命令报文
     * @param termId 终端id
     * @param msgNo  消息序号（消息 MID号。自动累加，去重）
     * @return
     */
    public static String information(Integer termId,Integer msgNo) {
        String VER = "01";
        String FUNC = "07";
        String ID = "";
        if (termId >= 0 && termId <= 65534) {
            ID = MessageParse.getString(termId);
        } else {
            System.out.println("终端id超出取值范围");
        }
        String NUMBER = MessageParse.getString(msgNo);
        String LENGTH = "0000";
        String message = VER + FUNC + ID + NUMBER + LENGTH;
        String Crc16Value = Crc16.generateCrc16(message);
        message = message + Crc16Value;
        return message;
    }

    /**
     * 生成恢复出厂设置报文
     *
     * @param termId 终端id
     * @param msgNo  消息序号（消息 MID号。自动累加，去重）
     * @return
     */
    public static String recover(Integer termId,Integer msgNo) {
        String VER = "01";
        String FUNC = "09";
        String ID = "";
        if (termId >= 0 && termId <= 65534) {
            ID = MessageParse.getString(termId);
        } else {
            System.out.println("终端id超出取值范围");
        }
        String NUMBER = MessageParse.getString(msgNo);
        String LENGTH = "0000";
        String message = VER + FUNC + ID + NUMBER + LENGTH;
        String Crc16Value = Crc16.generateCrc16(message);
        message = message + Crc16Value;
        return message;
    }



    /**
     * 将int转为16进制串  并转换位置（仅限2个字节）
     */
    public static String getString(int num) {
        String s = Integer.toHexString(num);

            if (s.length() == 1) {
                s = "000" + s;
            } else if (s.length() == 2) {
                s = "00" + s;
            } else if (s.length() == 3) {
                s = "0" + s;
            }
            String s1 = s.substring(0, 2);
            String s2 = s.substring(2, 4);
            s = s2 + s1;
            return s;

    }


    /**
     * 将16进制转换为二进制
     *
     * @param hexString
     * @return
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0){
            return null;
        }
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * 字符串转换为十六进制字符串
     *
     * @param s
     * @return
     */
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 字符串转换为Ascii
     * @param hex
     * @return
     */
    public static String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){
            //截取字符串
            String output = hex.substring(i, (i + 2));
            //将十六进制转为十进制
            int decimal = Integer.parseInt(output, 16);
            //将十进制转为字符
            sb.append((char)decimal);
        }
        return sb.toString();
    }
}
