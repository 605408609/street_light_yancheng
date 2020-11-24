package com.exc.street.light.sl.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.WebsocketQuery;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.sl.*;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.HttpsUtil;
import com.exc.street.light.resource.vo.SingleLampParamTempVO;
import com.exc.street.light.resource.vo.sl.*;
import com.exc.street.light.sl.VO.ControlMachineVO;
import com.exc.street.light.sl.VO.SetStrategyParamVO;
import com.exc.street.light.sl.VO.TempParamVO;
import com.exc.street.light.sl.config.MqttConfiguration;
import com.exc.street.light.sl.config.parameter.CtwingApi;
import com.exc.street.light.sl.config.parameter.LoraApi;
import com.exc.street.light.sl.config.parameter.LoraNewApi;
import com.exc.street.light.sl.po.KafkaMessage;
import com.exc.street.light.sl.po.RefreshVO;
import com.exc.street.light.sl.sender.MqttSender;
import com.exc.street.light.sl.service.*;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class MessageOperationUtil {
    private static final Logger log = LoggerFactory.getLogger(MessageOperationUtil.class);
    private static List<String> ADDLIST;

    @Autowired
    public void singleLampService(){
        List<String> addList = new ArrayList<>();
        for (int i = 9984; i <= 12288; i++) {
            addList.add(HexUtil.intToHexString(i));
        }
        MessageOperationUtil.ADDLIST = addList;
    }

    private static SingleLampParamService singleLampParamService;

    @Autowired
    public void singleLampService(SingleLampParamService singleLampParamService){
        MessageOperationUtil.singleLampParamService = singleLampParamService;
    }

    private static KafkaMessageService kafkaMessageService;

    @Autowired
    public void kafkaMessageService(KafkaMessageService kafkaMessageService){
        MessageOperationUtil.kafkaMessageService = kafkaMessageService;
    }


    private static LampDeviceService lampDeviceService;

    @Autowired
    public void lampDeviceService(LampDeviceService lampDeviceService){
        MessageOperationUtil.lampDeviceService = lampDeviceService;
    }

    private static RedisUtil redisUtil;

    @Autowired
    public void redisUtil(RedisUtil redisUtil){
        MessageOperationUtil.redisUtil = redisUtil;
    }

    private static LampEnergyService lampEnergyService;

    @Autowired
    public void lampDeviceService(LampEnergyService lampEnergyService){
        MessageOperationUtil.lampEnergyService = lampEnergyService;
    }

    private static LoraApi loraApi;

    @Autowired
    public void loraApi(LoraApi loraApi){
        MessageOperationUtil.loraApi = loraApi;
    }


    private static MqttConfiguration mqttConfiguration;

    @Autowired
    public void mqttConfiguration(MqttConfiguration mqttConfiguration){
        MessageOperationUtil.mqttConfiguration = mqttConfiguration;
    }

    private static LoraNewApi loraNewApi;

    @Autowired
    public void loraNewApi(LoraNewApi loraNewApi){
        MessageOperationUtil.loraNewApi = loraNewApi;
    }

    private static LampDeviceParameterService lampDeviceParameterService;

    @Autowired
    public void lampDeviceParameterService(LampDeviceParameterService lampDeviceParameterService){
        MessageOperationUtil.lampDeviceParameterService = lampDeviceParameterService;
    }

    private static SystemDeviceService systemDeviceService;

    @Autowired
    public void systemDeviceService(SystemDeviceService systemDeviceService){
        MessageOperationUtil.systemDeviceService = systemDeviceService;
    }

    private static SystemDeviceParameterService systemDeviceParameterService;

    @Autowired
    public void systemDeviceParameterService(SystemDeviceParameterService systemDeviceParameterService){
        MessageOperationUtil.systemDeviceParameterService = systemDeviceParameterService;
    }

    private static AlarmService alarmService;

    @Autowired
    public void alarmService(AlarmService alarmService){
        MessageOperationUtil.alarmService = alarmService;
    }

    private static LampStrategyService lampStrategyService;

    @Autowired
    public void systemDeviceParameterService(LampStrategyService lampStrategyService){
        MessageOperationUtil.lampStrategyService = lampStrategyService;
    }

    private static DeviceStrategyHistoryService deviceStrategyHistoryService;

    @Autowired
    public void deviceStrategyHistoryService(DeviceStrategyHistoryService deviceStrategyHistoryService){
        MessageOperationUtil.deviceStrategyHistoryService = deviceStrategyHistoryService;
    }

    private static CtwingApi ctwingApi;

    @Autowired
    public void ctwingApi(CtwingApi ctwingApi){
        MessageOperationUtil.ctwingApi = ctwingApi;
    }

    private static DeviceUpgradeLogStatusService deviceUpgradeLogStatusService;

    @Autowired
    public void deviceUpgradeLogStatusService(DeviceUpgradeLogStatusService deviceUpgradeLogStatusService){
        MessageOperationUtil.deviceUpgradeLogStatusService = deviceUpgradeLogStatusService;
    }


    /**
     * 生成报文
     * @param ADR
     * @param DR
     * @param ADD
     * @param LENGTH
     * @param INFO
     * @return
     */
    public static String generateMessage(String ADR,String DR,String ADD,String LENGTH,String INFO){
        //String message = "6300000001000903100101FAB1";
        String message = "";
        if(ADR.length()!=8){

            System.out.println("ADR不符合规范，阻止操作");
        }
        if(DR!="03"&&DR!="06"&&DR!="10"){
            System.out.println("DR不符合规范，阻止操作");
        }
        message = "63" + ADR + DR + ADD + LENGTH + INFO;
        String Crc16Value = Crc16.generateCrc16(message);
        message = message + Crc16Value;
        return message;
    }

    /**
     * 生成报文（10报文生成）
     * @param ADR
     * @param arrayNum
     * @param bodyLists
     * @return
     */
    public static String generateMessage(String ADR,Integer arrayNum,List<List<String>> bodyLists){
        //String message = "6300000001000903100101FAB1";
        String message = "";
        if(ADR.length()!=8){
            log.info("ADR不符合规范，阻止操作");
            return "";
        }
        if(arrayNum<0||arrayNum>10||arrayNum!=bodyLists.size()){
            log.info("寄存器组数量不符合规范，阻止操作");
            return "";
        }
        message = "63" + ADR + "10" + HexUtil.intToHexStringOne(arrayNum);
        for (List<String> bodyList : bodyLists) {
            String ADD = bodyList.get(0);
            String LENGTH = bodyList.get(1);
            String INFO = bodyList.get(2);
            message = message + ADD + LENGTH + INFO;
        }
        String Crc16Value = Crc16.generateCrc16(message);
        message = message + Crc16Value;
        return message;
    }

    /**
     * 解析报文
     * @param message
     * @return
     */
    public static String getDrString(String message) {
        String DrString = "";
        //先校验Crc16是否正确，直接截取最后两个字节，四个字符，进行校验
        int length = message.length();
        //最后四个字符
        String crc16String = message.substring(length - 4);
        //除最后四个字符外的字符
        String pendingCheckString = message.substring(0, length - 4);
        //进行Crc16校验
        String Crc16Value = Crc16.generateCrc16(pendingCheckString);
        if (Crc16Value.equals(crc16String)) {
            //判断起始标识符是否为63
            String startIdentifier = message.substring(0, 2);
            if ("63".equals(startIdentifier)) {
                //读取ADR
                DrString = message.substring(10, 12);
            }else {
                System.out.println("返回报文起始标识符错误，阻止操作");
            }
        }else {
            System.out.println("Crc16校验不成功，阻止操作");
        }
        return DrString;
    }

    /**
     * 解析报文
     * @param message
     * @return
     */
    public static List<String> parseMessage(String message){
        List<String> resultList = new ArrayList<>();
        String ADR = "";
        String ADD = "";
        String LENGTH = "";
        String INFO = "";


        //先校验Crc16是否正确，直接截取最后两个字节，四个字符，进行校验
        int length = message.length();
        //最后四个字符
        String crc16String = message.substring(length - 4);
        //除最后四个字符外的字符
        String pendingCheckString = message.substring(0, length - 4);
        //进行Crc16校验
        String Crc16Value = Crc16.generateCrc16(pendingCheckString);
        if(Crc16Value.equals(crc16String)){
            //判断起始标识符是否为63
            String startIdentifier = message.substring(0, 2);
            if("63".equals(startIdentifier)){
                //读取ADR
                ADR = message.substring(2, 10);
                //读取DR，单字节，判断为03/06/10（无变量地址）
                String DrString = message.substring(10, 12);
                switch (DrString){
                    case "03":
                        //如果是03，只能是回包
                        /*String LengthString = message.substring(12, 14);
                        int LengthValue = Integer.valueOf(LengthString,16);
                        INFO = message.substring(14,14+LengthValue*4);*/
                    case "06":
                        //如果是06，只能是回包
                        ADD = message.substring(12,16);
                        INFO = message.substring(16,20);
                        LENGTH = "01";
                        break;
                    case "10":
                        /*//分两种情况，回包跟主动上报的报文
                        if(length == 22){
                            //判断为10回包
                            ADD = message.substring(12,16);
                            if("1001".equals(ADD)){
                                LENGTH = "01";
                                INFO = "0100";
                            }else {
                                ADD = "";
                            }
                        }else {*/
                        //判断为主动上报的报文

                        /*//读取ADD
                        ADD = message.substring(12,16);
                        //读取LENGTH
                        LENGTH = message.substring(16,18);
                        int LengthValue10 = Integer.valueOf(LENGTH,16);
                        //根据LENGTH读取INFO
                        INFO = message.substring(18,18+LengthValue10*4);*/
                        resultList.add(ADR);
                        String ARRAYNUM = message.substring(12,14);
                        Integer arrayNum = Integer.parseInt(ARRAYNUM, 16);
                        int basicNum = 14;
                        for (int i = 0; i < arrayNum; i++){
                            //读取ADD
                            ADD = message.substring(basicNum,basicNum+4);
                            //读取LENGTH
                            LENGTH = message.substring(basicNum+4,basicNum+6);
                            int LengthValue10 = Integer.valueOf(LENGTH,16);
                            //根据LENGTH读取INFO
                            INFO = message.substring(basicNum+6,basicNum+6+LengthValue10*4);
                            resultList.add(ADD);
                            resultList.add(LENGTH);
                            resultList.add(INFO);
                            basicNum = basicNum+6+LengthValue10*4;
                        }
                        return resultList;
                    default:
                        System.out.println("返回报文DR错误，阻止操作");
                        break;
                }
            }else {
                System.out.println("返回报文起始标识符错误，阻止操作");
            }
        }else {
            System.out.println("Crc16校验不成功，阻止操作");
        }
        resultList.add(ADR);
        resultList.add(ADD);
        resultList.add(LENGTH);
        resultList.add(INFO);
        return resultList;
    }

    /**
     * 检查版本号
     * @param message
     * @param ADR
     * @param systemDevice
     * @param frequency
     * @param version
     * @param INFO
     */
    public static void checkVersion(String message,String ADR,SystemDevice systemDevice,int frequency,String version,String INFO){
        Long dateTime = System.currentTimeMillis() + 40000;
        Date date = new Date();
        date.setTime(dateTime);
        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String DateString = dateFormatJustDay.format(date);
        try {
            Timer timer = new Timer(DateString);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("定时任务：检查版本"+version+" 第"+frequency+"次");
                    //检查版本
                    String versionNow = (String)redisUtil.get(INFO + "version");
                    System.out.println("Redis当前版本：" + versionNow);
                    //启动定时任务判断30秒后版本号是否为当前版本,是重发信息，不是不管
                    Integer deviceTypeId = 0;
                    if(version.equals(versionNow)){
                        //重发消息
                        deviceTypeId = systemDevice.getDeviceTypeId();
                        sendMessageByAdr(message,systemDevice);
                        //重发次数+1
                        if(frequency<=2){
                            int frequencyNew = frequency + 1;
                            checkVersion(message,ADR,systemDevice,frequencyNew,version,INFO);
                        }
                    }
                    Date date1 = new Date();
                    if(date1.getTime()+1000>date.getTime()){
                        timer.cancel();
                    }else {
                        String sendEnd = "";

                        if(deviceTypeId==1||deviceTypeId==2){
                            sendEnd = MessageGeneration.nbOtaSendEnd(ADR, "0003");
                        }else if (deviceTypeId==5||deviceTypeId==6){
                            sendEnd = CatOneMessageGeneration.catOneOtaSendEnd(ADR, "0003");
                        }else if(deviceTypeId==3||deviceTypeId==4){
                            sendEnd = LoraOldMessageGeneration.loraOldOtaSendEnd("0003");
                        }else if(deviceTypeId==9||deviceTypeId==10){
                            sendEnd = LoraNewMessageGeneration.loraNewOtaSendEnd("0003");
                        }else if(deviceTypeId==7||deviceTypeId==8) {
                            sendEnd = DxnbMessageGeneration.dxnbOtaSendEnd("0003");
                        }else if(deviceTypeId.equals(14)||deviceTypeId.equals(15)){
                            sendEnd = DxCatOneMessageGeneration.dxCatOneOtaSendEnd("0003");
                        }
                        sendMessageByAdr(sendEnd,systemDevice);
                    }
                }
            }, date,200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下发ota信息
     * @param message
     */
    public static void sendMessageByAdr(String message,SystemDevice systemDevice){
        Integer deviceTypeId = systemDevice.getDeviceTypeId();
        String sendId = systemDevice.getReserveOne();
        //发送消息
        MessageOperationUtil.sendByMode(message,deviceTypeId,sendId);
    }

    /**
     * 延时下发ota信息
     * @param message
     */
    public static void sendMessageDelayed(String message,LampDevice lampDevice){
        String model = lampDevice.getModel();
        String sendId = lampDevice.getSendId();
        Long dateTime = System.currentTimeMillis() + 500;
        Date date = new Date();
        date.setTime(dateTime);
        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormatJustDay.format(date);
        MessageOperationUtil.sendTimingMessage(format,message,model,sendId);
    }

    public static boolean especialInformationHandle(String flag, String ADD, String INFO, Integer deviceTypeFlag){
        List<SystemDevice> systemDeviceList = systemDeviceService.getByFlag(flag, deviceTypeFlag);
        if("1009".equals(ADD)||"100A".equals(ADD)){
            return false;
        }else if("1007".equals(ADD)){
            SystemDevice systemDevice = systemDeviceList.get(0);
            if(systemDevice!=null){
                List<String> list = (List<String>)redisUtil.get(INFO);
                String lastMessage = list.get(list.size() - 1);
                int lastLength = (lastMessage.length() - 24)/2;
                int available = 0;
                /*List<Integer> specialDeviceTypeList = Arrays.asList(1, 2, 5, 6);
                List<Integer> dxSpecialDeviceTypeList = Arrays.asList(7, 8, 14, 15);*/
                if(deviceTypeFlag==1||deviceTypeFlag==3){
                    available = (list.size()-1) * 100 + lastLength;
                }else if(deviceTypeFlag==4||deviceTypeFlag==6){
                    available = (list.size()-1) * 200 + lastLength;
                }else {
                    available = (list.size()-1) * 34 + lastLength;
                }
                String info = HexUtil.intToHexString(available);
                String otaSizeMessage = "";
                if(deviceTypeFlag==1){
                    otaSizeMessage = MessageGeneration.nbOtaSize(flag, info);
                }else if (deviceTypeFlag==3){
                    otaSizeMessage = CatOneMessageGeneration.catOneOtaSize(flag, info);
                }else if(deviceTypeFlag==2){
                    otaSizeMessage = LoraOldMessageGeneration.loraOldOtaSize(info);
                }else if(deviceTypeFlag==5){
                    otaSizeMessage = LoraNewMessageGeneration.loraNewOtaSize(info);
                }else if(deviceTypeFlag==4) {
                    otaSizeMessage = DxnbMessageGeneration.dxnbOtaSize(info);
                }else if(deviceTypeFlag==6){
                    otaSizeMessage = DxCatOneMessageGeneration.dxCatOneOtaSize(info);
                }
                sendMessageByAdr(otaSizeMessage,systemDevice);
            }
        }else if("200C".equals(ADD)){
            SystemDevice systemDevice = systemDeviceList.get(0);
            if(systemDevice!=null){
                List<String> messageList = (List<String>)redisUtil.get(INFO);
                if(!messageList.isEmpty()){
                    String msa = messageList.get(0);
                    sendMessageByAdr(msa,systemDevice);
                    //创建版本
                    String version = "2700";
                    redisUtil.set(INFO+"version",version);
                    redisUtil.expire(INFO+"version",1800);
                    System.out.println("当前版本：" + version);
                    //checkVersion(msa,flag,systemDevice,1,version,INFO);
                }else {
                    System.out.println("ota报文生成错误");
                }
            }
        }else if(ADDLIST.contains(ADD)){
            /*if("dxnb".equals(model)){
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            SystemDevice systemDevice = systemDeviceList.get(0);
            if(systemDevice!=null){
                //修改版本号
                List<String> messageList = new ArrayList<>();
                String version = HexUtil.intToHexString(Integer.parseInt(ADD,16) + 1);
                try {
                    redisUtil.set(INFO+"version",version);
                    redisUtil.expire(INFO+"version",1800);
                    System.out.println("当前版本号："+version);
                    //发送消息
                    messageList = (List<String>)redisUtil.get(INFO);
                }catch (Exception e){
                    e.printStackTrace();
                }
                int num = Integer.parseInt(ADD,16) + 1;
                if(!messageList.isEmpty()){
                    if(num-9984==messageList.size()){
                        String sendEnd = "";
                        if(deviceTypeFlag==1){
                            sendEnd = MessageGeneration.nbOtaSendEnd(flag, "0003");
                        }else if (deviceTypeFlag==3){
                            sendEnd = CatOneMessageGeneration.catOneOtaSendEnd(flag, "0003");
                        }else if(deviceTypeFlag==2){
                            sendEnd = LoraOldMessageGeneration.loraOldOtaSendEnd("0003");
                        }else if(deviceTypeFlag==5){
                            sendEnd = LoraNewMessageGeneration.loraNewOtaSendEnd("0003");
                        }else if(deviceTypeFlag==4) {
                            sendEnd = DxnbMessageGeneration.dxnbOtaSendEnd("0003");
                        }else if(deviceTypeFlag==6){
                            sendEnd = DxCatOneMessageGeneration.dxCatOneOtaSendEnd("0003");
                        }
                        sendMessageByAdr(sendEnd,systemDevice);
                    }else {
                        String msa = messageList.get(num-9984);
                        sendMessageByAdr(msa,systemDevice);
                        //checkVersion(msa,flag,systemDevice,1,version,INFO);
                        log.info("ota信息下发：" + msa);
                    }
                }else {
                    log.error(flag + "ota报文生成错误");
                }
            }
        }else if("1008".equals(ADD)){
            if("0004".equals(INFO)){
                log.info(flag + "固件包下发完毕");
                SystemDevice systemDevice = systemDeviceList.get(0);
                Integer deviceTypeId = systemDevice.getDeviceTypeId();
                if(systemDevice!=null){
                    String otaBegin = "";
                    if(deviceTypeFlag==1){
                        otaBegin = MessageGeneration.nbOtaSendEnd(flag, "0006");
                    }else if (deviceTypeFlag==3){
                        otaBegin = CatOneMessageGeneration.catOneOtaSendEnd(flag, "0006");
                    }else if(deviceTypeFlag==2){
                        otaBegin = LoraOldMessageGeneration.loraOldOtaSendEnd("0006");
                    }else if(deviceTypeFlag==5){
                        otaBegin = LoraNewMessageGeneration.loraNewOtaSendEnd("0006");
                    }else if(deviceTypeFlag==4) {
                        otaBegin = DxnbMessageGeneration.dxnbOtaSendEnd("0006");
                    }else if(deviceTypeFlag==6){
                        otaBegin = DxCatOneMessageGeneration.dxCatOneOtaSendEnd("0006");
                    }
                    String sendId = systemDevice.getReserveOne();
                    MessageOperationUtil.sendByMode(otaBegin,deviceTypeId,sendId);
                }
            }else {
                String logIdString = (String)redisUtil.get(INFO+"logId");
                Integer logId = Integer.parseInt(logIdString);
                /*String Crc16Value = Crc16.generateCrc16(INFO);
                Integer logId = Integer.parseInt(Crc16Value.substring(28, INFO.length()));*/
                List<Integer> idList = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                QueryWrapper<DeviceUpgradeLogStatus> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("log_id",logId);
                queryWrapper.in("device_id",idList);
                List<DeviceUpgradeLogStatus> deviceUpgradeLogStatusList = deviceUpgradeLogStatusService.list(queryWrapper);
                deviceUpgradeLogStatusList = deviceUpgradeLogStatusList.stream()
                        .map(e->{
                            e.setIsSuccess(1);
                            return e;
                        }).collect(Collectors.toList());
                deviceUpgradeLogStatusService.updateBatchById(deviceUpgradeLogStatusList);
                log.error(flag + "ota升级成功");
            }
        }else if("3001".equals(ADD)){
            String alarmString = HexUtil.hexStringToBinaryString(INFO);
            for (SystemDevice systemDevice : systemDeviceList) {
                Integer deviceId = systemDevice.getId();
                SlLampPost slLampPost = systemDeviceService.selectLampPostByDeviceId(deviceId);
                if(slLampPost!=null){
                    alarmService.addLampAlarm(slLampPost,alarmString,deviceId);
                }
            }
        }
        return true;
    }



    /**
     * 单个信息组处理
     * @param ADR
     * @param ADD
     * @param LENGTH
     * @param INFO
     * @return
     */
    public static List<SingleLampParamTempVO> informationHandle(String ADR,String ADD,String LENGTH,String INFO){
        List<SingleLampParamTempVO> singleLampParamList = new ArrayList<>();
        if(!ADD.isEmpty()){

            //通过ADD获取索引
            Integer index = ADDMatch.STATEONE.getIndexByADD(ADD);
            if(index == null){
                return singleLampParamList;
            }
            //将LENGTH转化为整形
            int LengthValue = Integer.valueOf(LENGTH,16);


            if(index != -1&&(!INFO.isEmpty())){
                //解析报文中的INFO信息,转化为byte[]
                byte[] infoBytes = HexUtil.hexStringToBytes(INFO);
                System.out.println(infoBytes);
                singleLampParamList = setData(LengthValue, index, infoBytes,ADR);
            }
        }
        return singleLampParamList;
    }

    /**
     * 通过报文获取信息
     * @param message
     * @return
     */
    public static List<SingleLampParamTempVO> nbGetInformation(String message){
        log.info("nb上报报文解析：{}",message);
        //解析报文，获取ADD,LENGTH和INFO
        String drString = MessageOperationUtil.getDrString(message);
        List<String> resultList = MessageOperationUtil.parseMessage(message);
        String ADR = resultList.get(0);
        /*if("01010101".equals(ADR)){
            System.out.println("ota准备失败");
            return null;
        }*/
        List<SingleLampParamTempVO> singleLampParamList = new ArrayList<>();
        List<SystemDevice> systemDeviceList = systemDeviceService.getByFlag(ADR, 1);
        if(systemDeviceList!=null&&systemDeviceList.size()>0) {
            //处理06信息
            if ("06".equals(drString)) {
                String ADD = resultList.get(1);
                String INFO = resultList.get(3);
                boolean b = especialInformationHandle(ADR, ADD, INFO, 1);
                if (b) {
                    return singleLampParamList;
                }
            }
            if ("201E".equals(resultList.get(1))) {
                String ADD = resultList.get(4);
                if ("2019".equals(ADD)) {
                    String INFO = resultList.get(6);
                    List<Integer> deviceStrategyHistoryIdList = (List<Integer>) redisUtil.get(INFO);
                    /*List<Integer> deviceIds = new ArrayList<>();
                    for (int i = 0; i < idList.size(); i++) {
                        if (i != 0) {
                            deviceIds.add(idList.get(i));
                        }
                    }
                    Integer strategyId = idList.get(0);*/
                    lampStrategyService.updateDeviceAndStrategy(deviceStrategyHistoryIdList);
                }
            } else if("1008".equals(resultList.get(1))){
                String ADD = resultList.get(4);
                if ("1007".equals(ADD)) {
                    String INFO = resultList.get(6);
                    log.error(drString + "ota升级失败");
                    String logIdString = (String)redisUtil.get(INFO+"logId");
                    Integer logId = Integer.parseInt(logIdString);
                    List<Integer> idList = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                    QueryWrapper<DeviceUpgradeLogStatus> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("log_id",logId);
                    queryWrapper.in("device_id",idList);
                    List<DeviceUpgradeLogStatus> deviceUpgradeLogStatusList = deviceUpgradeLogStatusService.list(queryWrapper);
                    deviceUpgradeLogStatusList = deviceUpgradeLogStatusList.stream()
                            .map(e->{
                                e.setIsSuccess(2);
                                return e;
                            }).collect(Collectors.toList());
                    deviceUpgradeLogStatusService.updateBatchById(deviceUpgradeLogStatusList);
                }
            }else {
                //处理10信息
                for (int i = 0; i < resultList.size() / 3; i++) {
                    String ADD = resultList.get(i * 3 + 1);
                    String LENGTH = resultList.get(i * 3 + 2);
                    String INFO = resultList.get(i * 3 + 3);
                    List<SingleLampParamTempVO> singleLampParamListTemp = informationHandle(ADR, ADD, LENGTH, INFO);
                    singleLampParamList.addAll(singleLampParamListTemp);
                }
            }
        }
        /*if(!ADD.isEmpty()){

            //通过ADD获取索引
            Integer index = ADDMatch.STATE.getIndexByADD(ADD);
            if(index == null){
                return singleLampParamList;
            }
            //将LENGTH转化为整形
            int LengthValue = Integer.valueOf(LENGTH,16);


            if(index != -1&&(!INFO.isEmpty())){
                //解析报文中的INFO信息,转化为byte[]
                byte[] infoBytes = HexUtil.hexStringToBytes(INFO);
                System.out.println(infoBytes);
                singleLampParamList = setData(LengthValue, index, infoBytes,ADR);
            }
        }*/

        return singleLampParamList;
    }

    /**
     * 通过报文获取信息
     * @param message
     * @return
     */
    public static List<SingleLampParamTempVO> catOneGetInformation(String message){
        log.info("cat1上报报文解析：{}",message);
        //解析报文，获取ADD,LENGTH和INFO
        String drString = MessageOperationUtil.getDrString(message);
        List<String> resultList = MessageOperationUtil.parseMessage(message);
        String ADR = resultList.get(0);
        /*if("01010101".equals(ADR)){
            System.out.println("ota准备失败");
            return null;
        }*/
        List<SingleLampParamTempVO> singleLampParamList = new ArrayList<>();
        List<SystemDevice> systemDeviceList = systemDeviceService.getByFlag(ADR, 3);
        if(systemDeviceList!=null&&systemDeviceList.size()>0) {
            //处理06信息
            if ("06".equals(drString)) {
                String ADD = resultList.get(1);
                String INFO = resultList.get(3);
                boolean b = especialInformationHandle(ADR, ADD, INFO, 3);
                if (b) {
                    return singleLampParamList;
                }
            }
            if ("201E".equals(resultList.get(1))) {
                String ADD = resultList.get(4);
                if ("2019".equals(ADD)) {
                    String INFO = resultList.get(6);
                    List<Integer> deviceStrategyHistoryIdList = (List<Integer>) redisUtil.get(INFO);
                    /*List<Integer> deviceIds = new ArrayList<>();
                    for (int i = 0; i < idList.size(); i++) {
                        if (i != 0) {
                            deviceIds.add(idList.get(i));
                        }
                    }
                    Integer strategyId = idList.get(0);*/
                    lampStrategyService.updateDeviceAndStrategy(deviceStrategyHistoryIdList);
                }
            } else if("1008".equals(resultList.get(1))){
                String ADD = resultList.get(4);
                if ("1007".equals(ADD)) {
                    String INFO = resultList.get(6);
                    log.error(drString + "ota升级失败");
                    String logIdString = (String)redisUtil.get(INFO+"logId");
                    Integer logId = Integer.parseInt(logIdString);
                    List<Integer> idList = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                    QueryWrapper<DeviceUpgradeLogStatus> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("log_id",logId);
                    queryWrapper.in("device_id",idList);
                    List<DeviceUpgradeLogStatus> deviceUpgradeLogStatusList = deviceUpgradeLogStatusService.list(queryWrapper);
                    deviceUpgradeLogStatusList = deviceUpgradeLogStatusList.stream()
                            .map(e->{
                                e.setIsSuccess(2);
                                return e;
                            }).collect(Collectors.toList());
                    deviceUpgradeLogStatusService.updateBatchById(deviceUpgradeLogStatusList);
                }
            } else {
                //处理10信息
                for (int i = 0; i < resultList.size() / 3; i++) {
                    String ADD = resultList.get(i * 3 + 1);
                    String LENGTH = resultList.get(i * 3 + 2);
                    String INFO = resultList.get(i * 3 + 3);
                    List<SingleLampParamTempVO> singleLampParamListTemp = informationHandle(ADR, ADD, LENGTH, INFO);
                    singleLampParamList.addAll(singleLampParamListTemp);
                }
            }
        }
        /*if(!ADD.isEmpty()){

            //通过ADD获取索引
            Integer index = ADDMatch.STATE.getIndexByADD(ADD);
            if(index == null){
                return singleLampParamList;
            }
            //将LENGTH转化为整形
            int LengthValue = Integer.valueOf(LENGTH,16);


            if(index != -1&&(!INFO.isEmpty())){
                //解析报文中的INFO信息,转化为byte[]
                byte[] infoBytes = HexUtil.hexStringToBytes(INFO);
                System.out.println(infoBytes);
                singleLampParamList = setData(LengthValue, index, infoBytes,ADR);
            }
        }*/

        return singleLampParamList;
    }

    /**
     * 通过报文获取信息
     * @param message
     * @return
     */
    public static List<SingleLampParamTempVO> dxnbGetInformation(String imei,String message){
        log.info("电信nb上报报文解析：{},{}",imei,message);
        //解析报文，获取ADD,LENGTH和INFO
        String drString = MessageOperationUtil.getDrString(message);
        List<String> resultList = MessageOperationUtil.parseMessage(message);
        String ADR = resultList.get(0);
        /*if("01010101".equals(ADR)){
            System.out.println("ota准备失败");
            return null;
        }*/
        List<SingleLampParamTempVO> singleLampParamList = new ArrayList<>();
        List<SystemDevice> systemDeviceList = systemDeviceService.getByFlag(imei, 4);
        if(systemDeviceList!=null&&systemDeviceList.size()>0) {
            //处理06信息
            if ("06".equals(drString)) {
                String ADD = resultList.get(1);
                String INFO = resultList.get(3);
                boolean b = especialInformationHandle(imei, ADD, INFO, 4);
                if (b) {
                    return singleLampParamList;
                }
            }
            if ("201E".equals(resultList.get(1))) {
                String ADD = resultList.get(4);
                if ("2019".equals(ADD)) {
                    String INFO = resultList.get(6);
                    List<Integer> deviceStrategyHistoryIdList = (List<Integer>) redisUtil.get(INFO);
                    /*List<Integer> deviceIds = new ArrayList<>();
                    for (int i = 0; i < idList.size(); i++) {
                        if (i != 0) {
                            deviceIds.add(idList.get(i));
                        }
                    }
                    Integer strategyId = idList.get(0);*/
                    lampStrategyService.updateDeviceAndStrategy(deviceStrategyHistoryIdList);
                }
            } else if("1008".equals(resultList.get(1))){
                String ADD = resultList.get(4);
                if ("1007".equals(ADD)) {
                    String INFO = resultList.get(6);
                    log.error(imei + "ota升级失败");
                    String logIdString = (String)redisUtil.get(INFO+"logId");
                    Integer logId = Integer.parseInt(logIdString);
                    List<Integer> idList = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                    QueryWrapper<DeviceUpgradeLogStatus> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("log_id",logId);
                    queryWrapper.in("device_id",idList);
                    List<DeviceUpgradeLogStatus> deviceUpgradeLogStatusList = deviceUpgradeLogStatusService.list(queryWrapper);
                    deviceUpgradeLogStatusList = deviceUpgradeLogStatusList.stream()
                            .map(e->{
                                e.setIsSuccess(2);
                                return e;
                            }).collect(Collectors.toList());
                    deviceUpgradeLogStatusService.updateBatchById(deviceUpgradeLogStatusList);
                }
            }else {
                //处理10信息
                for (int i = 0; i < resultList.size() / 3; i++) {
                    String ADD = resultList.get(i * 3 + 1);
                    String LENGTH = resultList.get(i * 3 + 2);
                    String INFO = resultList.get(i * 3 + 3);
                    List<SingleLampParamTempVO> singleLampParamListTemp = informationHandle(imei, ADD, LENGTH, INFO);
                    singleLampParamList.addAll(singleLampParamListTemp);
                }
            }
        }
        /*if(!ADD.isEmpty()){

            //通过ADD获取索引
            Integer index = ADDMatch.STATE.getIndexByADD(ADD);
            if(index == null){
                return singleLampParamList;
            }
            //将LENGTH转化为整形
            int LengthValue = Integer.valueOf(LENGTH,16);


            if(index != -1&&(!INFO.isEmpty())){
                //解析报文中的INFO信息,转化为byte[]
                byte[] infoBytes = HexUtil.hexStringToBytes(INFO);
                System.out.println(infoBytes);
                singleLampParamList = setData(LengthValue, index, infoBytes,ADR);
            }
        }*/

        return singleLampParamList;
    }

    /**
     * 通过报文获取信息
     * @param message
     * @return
     */
    public static List<SingleLampParamTempVO> dxCatOneGetInformation(String deviceId,String message){
        log.info("电信cat1上报报文解析：{},{}",deviceId,message);
        //解析报文，获取ADD,LENGTH和INFO
        String drString = MessageOperationUtil.getDrString(message);
        List<String> resultList = MessageOperationUtil.parseMessage(message);
        String ADR = resultList.get(0);
        /*if("01010101".equals(ADR)){
            System.out.println("ota准备失败");
            return null;
        }*/
        List<SingleLampParamTempVO> singleLampParamList = new ArrayList<>();
        List<SystemDevice> systemDeviceList = systemDeviceService.getByFlag(deviceId, 6);
        if(systemDeviceList!=null&&systemDeviceList.size()>0) {
            //处理06信息
            if ("06".equals(drString)) {
                String ADD = resultList.get(1);
                String INFO = resultList.get(3);
                boolean b = especialInformationHandle(deviceId, ADD, INFO, 6);
                if (b) {
                    return singleLampParamList;
                }
            }
            if ("201E".equals(resultList.get(1))) {
                String ADD = resultList.get(4);
                if ("2019".equals(ADD)) {
                    String INFO = resultList.get(6);
                    List<Integer> deviceStrategyHistoryIdList = (List<Integer>) redisUtil.get(INFO);
                    /*List<Integer> deviceIds = new ArrayList<>();
                    for (int i = 0; i < idList.size(); i++) {
                        if (i != 0) {
                            deviceIds.add(idList.get(i));
                        }
                    }
                    Integer strategyId = idList.get(0);*/
                    lampStrategyService.updateDeviceAndStrategy(deviceStrategyHistoryIdList);
                }
            } else if("1008".equals(resultList.get(1))){
                String ADD = resultList.get(4);
                if ("1007".equals(ADD)) {
                    String INFO = resultList.get(6);
                    log.error(deviceId + "ota升级失败");
                    String logIdString = (String)redisUtil.get(INFO+"logId");
                    Integer logId = Integer.parseInt(logIdString);
                    List<Integer> idList = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                    QueryWrapper<DeviceUpgradeLogStatus> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("log_id",logId);
                    queryWrapper.in("device_id",idList);
                    List<DeviceUpgradeLogStatus> deviceUpgradeLogStatusList = deviceUpgradeLogStatusService.list(queryWrapper);
                    deviceUpgradeLogStatusList = deviceUpgradeLogStatusList.stream()
                            .map(e->{
                                e.setIsSuccess(2);
                                return e;
                            }).collect(Collectors.toList());
                    deviceUpgradeLogStatusService.updateBatchById(deviceUpgradeLogStatusList);
                }
            }else {
                //处理10信息
                if (systemDeviceList != null && systemDeviceList.size() > 0) {
                    String num = systemDeviceList.get(0).getNum();
                    for (int i = 0; i < resultList.size() / 3; i++) {
                        String ADD = resultList.get(i * 3 + 1);
                        String LENGTH = resultList.get(i * 3 + 2);
                        String INFO = resultList.get(i * 3 + 3);
                        List<SingleLampParamTempVO> singleLampParamListTemp = informationHandle(num, ADD, LENGTH, INFO);
                        singleLampParamList.addAll(singleLampParamListTemp);
                    }
                }
            }
        }

        /*if(!ADD.isEmpty()){

            //通过ADD获取索引
            Integer index = ADDMatch.STATE.getIndexByADD(ADD);
            if(index == null){
                return singleLampParamList;
            }
            //将LENGTH转化为整形
            int LengthValue = Integer.valueOf(LENGTH,16);


            if(index != -1&&(!INFO.isEmpty())){
                //解析报文中的INFO信息,转化为byte[]
                byte[] infoBytes = HexUtil.hexStringToBytes(INFO);
                System.out.println(infoBytes);
                singleLampParamList = setData(LengthValue, index, infoBytes,ADR);
            }
        }*/

        return singleLampParamList;
    }

    /**
     * 通过报文获取信息
     * @param message
     * @return
     */
    public static List<SingleLampParamTempVO> loraOldGetInformation(String devEui,String message){
        log.info("lora旧平台上报报文解析：{},{}",devEui,message);
        //解析报文，获取ADD,LENGTH和INFO
        String drString = MessageOperationUtil.getDrString(message);
        List<String> resultList = MessageOperationUtil.parseMessage(message);
        String ADR = resultList.get(0);
        /*if("01010101".equals(ADR)){
            System.out.println("ota准备失败");
            return null;
        }*/
        List<SingleLampParamTempVO> singleLampParamList = new ArrayList<>();
        List<SystemDevice> systemDeviceList = systemDeviceService.getByFlag(devEui, 2);
        if(systemDeviceList!=null&&systemDeviceList.size()>0) {
            //处理06信息
            if ("06".equals(drString)) {
                String ADD = resultList.get(1);
                String INFO = resultList.get(3);
                boolean b = especialInformationHandle(devEui, ADD, INFO, 2);
                if (b) {
                    return singleLampParamList;
                }
            }
            if ("201E".equals(resultList.get(1))) {
                String ADD = resultList.get(4);
                if ("2019".equals(ADD)) {
                    String INFO = resultList.get(6);
                    List<Integer> deviceStrategyHistoryIdList = (List<Integer>) redisUtil.get(INFO);
                    /*List<Integer> deviceIds = new ArrayList<>();
                    for (int i = 0; i < idList.size(); i++) {
                        if (i != 0) {
                            deviceIds.add(idList.get(i));
                        }
                    }
                    Integer strategyId = idList.get(0);*/
                    lampStrategyService.updateDeviceAndStrategy(deviceStrategyHistoryIdList);
                }
            } else if("1008".equals(resultList.get(1))){
                String ADD = resultList.get(4);
                if ("1007".equals(ADD)) {
                    String INFO = resultList.get(6);
                    log.error(devEui + "ota升级失败");
                    String logIdString = (String)redisUtil.get(INFO+"logId");
                    Integer logId = Integer.parseInt(logIdString);
                    List<Integer> idList = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                    QueryWrapper<DeviceUpgradeLogStatus> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("log_id",logId);
                    queryWrapper.in("device_id",idList);
                    List<DeviceUpgradeLogStatus> deviceUpgradeLogStatusList = deviceUpgradeLogStatusService.list(queryWrapper);
                    deviceUpgradeLogStatusList = deviceUpgradeLogStatusList.stream()
                            .map(e->{
                                e.setIsSuccess(2);
                                return e;
                            }).collect(Collectors.toList());
                    deviceUpgradeLogStatusService.updateBatchById(deviceUpgradeLogStatusList);
                }
            }else {
                //处理10信息
                for (int i = 0; i < resultList.size() / 3; i++) {
                    String ADD = resultList.get(i * 3 + 1);
                    String LENGTH = resultList.get(i * 3 + 2);
                    String INFO = resultList.get(i * 3 + 3);
                    List<SingleLampParamTempVO> singleLampParamListTemp = informationHandle(devEui, ADD, LENGTH, INFO);
                    singleLampParamList.addAll(singleLampParamListTemp);
                }
            }
        }
        /*if(!ADD.isEmpty()){

            //通过ADD获取索引
            Integer index = ADDMatch.STATE.getIndexByADD(ADD);
            if(index == null){
                return singleLampParamList;
            }
            //将LENGTH转化为整形
            int LengthValue = Integer.valueOf(LENGTH,16);


            if(index != -1&&(!INFO.isEmpty())){
                //解析报文中的INFO信息,转化为byte[]
                byte[] infoBytes = HexUtil.hexStringToBytes(INFO);
                System.out.println(infoBytes);
                singleLampParamList = setData(LengthValue, index, infoBytes,ADR);
            }
        }*/

        return singleLampParamList;
    }

    /**
     * 通过报文获取信息
     * @param message
     * @return
     */
    public static List<SingleLampParamTempVO> loraNewGetInformation(String devEui,String message){
        log.info("lora新平台上报报文解析：{},{}",devEui,message);
        //解析报文，获取ADD,LENGTH和INFO
        String drString = MessageOperationUtil.getDrString(message);
        List<String> resultList = MessageOperationUtil.parseMessage(message);
        String ADR = resultList.get(0);
        /*if("01010101".equals(ADR)){
            System.out.println("ota准备失败");
            return null;
        }*/
        List<SingleLampParamTempVO> singleLampParamList = new ArrayList<>();
        List<SystemDevice> systemDeviceList = systemDeviceService.getByFlag(devEui, 5);
        if(systemDeviceList!=null&&systemDeviceList.size()>0) {
            //处理06信息
            if ("06".equals(drString)) {
                String ADD = resultList.get(1);
                String INFO = resultList.get(3);
                boolean b = especialInformationHandle(devEui, ADD, INFO, 5);
                if (b) {
                    return singleLampParamList;
                }
            }
            if ("201E".equals(resultList.get(1))) {
                String ADD = resultList.get(4);
                if ("2019".equals(ADD)) {
                    String INFO = resultList.get(6);
                    List<Integer> deviceStrategyHistoryIdList = (List<Integer>) redisUtil.get(INFO);
                    /*List<Integer> deviceIds = new ArrayList<>();
                    for (int i = 0; i < idList.size(); i++) {
                        if (i != 0) {
                            deviceIds.add(idList.get(i));
                        }
                    }
                    Integer strategyId = idList.get(0);*/
                    lampStrategyService.updateDeviceAndStrategy(deviceStrategyHistoryIdList);
                }
            } else if("1008".equals(resultList.get(1))){
                String ADD = resultList.get(4);
                if ("1007".equals(ADD)) {
                    String INFO = resultList.get(6);
                    log.error(devEui + "ota升级失败");
                    String logIdString = (String)redisUtil.get(INFO+"logId");
                    Integer logId = Integer.parseInt(logIdString);
                    List<Integer> idList = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                    QueryWrapper<DeviceUpgradeLogStatus> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("log_id",logId);
                    queryWrapper.in("device_id",idList);
                    List<DeviceUpgradeLogStatus> deviceUpgradeLogStatusList = deviceUpgradeLogStatusService.list(queryWrapper);
                    deviceUpgradeLogStatusList = deviceUpgradeLogStatusList.stream()
                            .map(e->{
                                e.setIsSuccess(2);
                                return e;
                            }).collect(Collectors.toList());
                    deviceUpgradeLogStatusService.updateBatchById(deviceUpgradeLogStatusList);
                }
            }else {
                //处理10信息
                if (systemDeviceList != null && systemDeviceList.size() > 0) {
                    String num = systemDeviceList.get(0).getNum();
                    for (int i = 0; i < resultList.size() / 3; i++) {
                        String ADD = resultList.get(i * 3 + 1);
                        String LENGTH = resultList.get(i * 3 + 2);
                        String INFO = resultList.get(i * 3 + 3);
                        List<SingleLampParamTempVO> singleLampParamListTemp = informationHandle(num, ADD, LENGTH, INFO);
                        singleLampParamList.addAll(singleLampParamListTemp);
                    }
                }
            }
        }
        /*if(!ADD.isEmpty()){

            //通过ADD获取索引
            Integer index = ADDMatch.STATE.getIndexByADD(ADD);
            if(index == null){
                return singleLampParamList;
            }
            //将LENGTH转化为整形
            int LengthValue = Integer.valueOf(LENGTH,16);


            if(index != -1&&(!INFO.isEmpty())){
                //解析报文中的INFO信息,转化为byte[]
                byte[] infoBytes = HexUtil.hexStringToBytes(INFO);
                System.out.println(infoBytes);
                singleLampParamList = setData(LengthValue, index, infoBytes,ADR);
            }
        }*/

        return singleLampParamList;
    }

    /**
     * 设置数据到信息类中
     * @param LengthValue
    //* @param attributeList
     * @param index
     * @param infoBytes
     * @return
     */
    public static List<SingleLampParamTempVO> setData(int LengthValue, int index, byte[] infoBytes, String deviceNum){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        List<SingleLampParamTempVO> singleLampParamList = new ArrayList<>();
        //创建单灯信息类
        SingleLampParamTempVO singleLampParamOne = new SingleLampParamTempVO();
        SingleLampParamTempVO singleLampParamTwo = new SingleLampParamTempVO();
        singleLampParamOne.setDeviceNum(deviceNum);
        singleLampParamTwo.setDeviceNum(deviceNum);
        singleLampParamOne.setLoopNum(1);
        singleLampParamTwo.setLoopNum(2);

        //根据下标和LENGTH在涉及属性List中查找
        String factorySerialNumString = "";
        String cardNumberString = "";
        String electricEnergyOneString = "";
        String powerTimeString = "";
        String moduleATimeString = "";
        String lampATimeOneString = "";
        String lampTimeOneString = "";
        String electricEnergyTwoString = "";
        String lampATimeTwoString = "";
        String lampTimeTwoString = "";
        for (int i = 0; i<LengthValue; i++){
            String attribute = ADDMatch.STATEONE.getAttributeByIndex(index + i);
            //String attribute = attributeList.get(index + i);
            byte[] newBytes = {infoBytes[i*2],infoBytes[i*2+1]};
            String attributeValue = HexUtil.bytesTohex(newBytes).replace(" ", "");
            switch (attribute){
                //开关状态
                //亮度
                case "stateOne":
                    String state1 = attributeValue.substring(0, 2);
                    String brightness1 = attributeValue.substring(2, 4);
                    int brightnessNum1 = Integer.parseInt(brightness1, 16);
                    if(!"FF".equals(state1)){
                        singleLampParamOne.setBrightState(Integer.parseInt(state1));
                    }
                    if(!"FF".equals(brightnessNum1)){
                        singleLampParamOne.setBrightness(brightnessNum1);
                    }
                    if(brightnessNum1 == 0){
                        singleLampParamOne.setBrightState(0);
                    }
                    /*String state1 = attributeValue.substring(0, 2);
                    String state2 = attributeValue.substring(2, 4);
                    singleLampParamOne.setBrightState(Integer.parseInt(state1));
                    if(!"FF".equals(state2)){
                        singleLampParamTwo.setBrightState(Integer.parseInt(state2));
                    }*/
                    /*if("01".equals(state1)){
                        SingleLampParam singleLampParamNow = singleLampParamService.getSingleLampById(id);
                        Integer brightnessOne = singleLampParamNow.getBrightnessOne();
                        if(brightnessOne == 0){
                            singleLampParam.setBrightnessOne(10);
                        }else{
                            singleLampParam.setBrightnessOne(brightnessOne);
                        }
                    }*/
                    break;
                case "stateTwo":
                    String state2 = attributeValue.substring(0, 2);
                    String brightness2 = attributeValue.substring(2, 4);
                    int brightnessNum2 = Integer.parseInt(brightness2, 16);
                    if(!"FF".equals(state2)){
                        singleLampParamTwo.setBrightState(Integer.parseInt(state2));
                    }
                    if(!"FF".equals(brightnessNum2)){
                        singleLampParamTwo.setBrightness(brightnessNum2);
                    }
                    if(brightnessNum2 == 0){
                        singleLampParamTwo.setBrightState(0);
                    }

                    /*String brightness1 = attributeValue.substring(0,2);
                    String brightness2 = attributeValue.substring(2,4);
                    int brightnessNum1 = Integer.parseInt(brightness1, 16);
                    int brightnessNum2 = Integer.parseInt(brightness2, 16);
                    singleLampParamOne.setBrightness(brightnessNum1);
                    singleLampParamTwo.setBrightness(brightnessNum2);
                    if(brightnessNum1 == 0){
                        singleLampParamOne.setBrightState(0);
                    }
                    if(brightnessNum2 == 0){
                        singleLampParamTwo.setBrightState(0);
                    }*/
                    break;
                //模块温度
                //当前电压有效值
                case "moduleTemperature":
                    Integer moduleTemperature = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setModuleTemperature(moduleTemperature.doubleValue()/10);
                    singleLampParamTwo.setModuleTemperature(moduleTemperature.doubleValue()/10);
                    break;
                case "voltage":
                    Integer voltage = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setVoltage(voltage.doubleValue()/10);
                    singleLampParamTwo.setVoltage(voltage.doubleValue()/10);
                    break;

                //通道1当前电流有效值
                //通道1当前有功功率
                //通道1有功电能（累计）高字节
                //通道1有功电能（累计）低字节
                case "electricCurrentOne":
                    Integer electricCurrentOne = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setElectricCurrent(electricCurrentOne.doubleValue());
                    break;
                case "powerOne":
                    Integer powerOne = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setPower(powerOne.doubleValue());
                    break;
                case "electricEnergyOneHigh":
                    electricEnergyOneString = electricEnergyOneString + attributeValue;
                    break;
                case "electricEnergyOneLow":
                    electricEnergyOneString = electricEnergyOneString + attributeValue;
                    Integer electricEnergyOne = Integer.parseInt(electricEnergyOneString, 16);
                    singleLampParamOne.setElectricEnergy(electricEnergyOne.doubleValue()/10);
                    break;

                //模块上电时长高字节
                //模块上电时长低字节
                //模块累计工作时长高字节
                //模块累计工作时长低字节
                case "powerTimeHigh":
                    powerTimeString = powerTimeString + attributeValue;
                    break;
                case "powerTimeLow":
                    powerTimeString = powerTimeString + attributeValue;
                    Integer powerTime = Integer.parseInt(powerTimeString, 16);
                    singleLampParamOne.setPowerTime(powerTime.doubleValue());
                    singleLampParamTwo.setPowerTime(powerTime.doubleValue());
                    break;
                case "moduleATimeHigh":
                    moduleATimeString = moduleATimeString + attributeValue;
                    break;
                case "moduleATimeLow":
                    moduleATimeString = moduleATimeString + attributeValue;
                    Integer moduleATime = Integer.parseInt(moduleATimeString, 16);
                    singleLampParamOne.setModuleATime(moduleATime.doubleValue());
                    singleLampParamTwo.setModuleATime(moduleATime.doubleValue());
                    break;

                //通道1灯具累计工作时长高字节
                //通道1灯具累计工作时长低字节
                //通道1灯具工作时长高字节
                //通道1灯具工作时长低字节
                case "lampATimeOneHigh":
                    lampATimeOneString = lampATimeOneString + attributeValue;
                    break;
                case "lampATimeOneLow":
                    lampATimeOneString = lampATimeOneString + attributeValue;
                    Integer lampATimeOne = Integer.parseInt(lampATimeOneString, 16);
                    singleLampParamOne.setLampATime(lampATimeOne.doubleValue());
                    break;
                case "lampTimeOneHigh":
                    lampTimeOneString = lampTimeOneString + attributeValue;
                    break;
                case "lampTimeOneLow":
                    lampTimeOneString = lampTimeOneString + attributeValue;
                    Integer lampTimeOne = Integer.parseInt(lampTimeOneString, 16);
                    singleLampParamOne.setLampTime(lampTimeOne.doubleValue());
                    break;

                //告警
                //电压告警次数
                //电流告警次数
                //温度告警次数
                case "alarm":
                    String alarm = HexUtil.hexStringToBinaryString(attributeValue);
                    singleLampParamOne.setAlarm(alarm);
                    singleLampParamTwo.setAlarm(alarm);
                    break;
                case "voltageAlarm":
                    int voltageAlarm = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setVoltageAlarm(voltageAlarm);
                    singleLampParamTwo.setVoltageAlarm(voltageAlarm);
                    break;
                case "electricCurrentAlarm":
                    int electricCurrentAlarm = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setElectricCurrentAlarm(electricCurrentAlarm);
                    singleLampParamTwo.setElectricCurrentAlarm(electricCurrentAlarm);
                    break;
                case "temperatureAlarm":
                    int temperatureAlarm = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setTemperatureAlarm(temperatureAlarm);
                    singleLampParamTwo.setTemperatureAlarm(temperatureAlarm);
                    break;

                //出厂序列号(6字节)
                case "factorySerialNum":
                    factorySerialNumString = factorySerialNumString + attributeValue;
                    break;
                case "factorySerialNum1":
                    factorySerialNumString = factorySerialNumString + attributeValue;
                    break;
                case "factorySerialNum2":
                    factorySerialNumString = factorySerialNumString + attributeValue;
                    break;
                case "factorySerialNum3":
                    factorySerialNumString = factorySerialNumString + attributeValue;
                    break;
                case "factorySerialNum4":
                    factorySerialNumString = factorySerialNumString + attributeValue;
                    singleLampParamOne.setFactorySerialNum(factorySerialNumString);
                    singleLampParamTwo.setFactorySerialNum(factorySerialNumString);
                    break;

                //出厂序列号(6字节)
                case "cardNumber":
                    cardNumberString = cardNumberString + attributeValue;
                    break;
                case "cardNumber1":
                    cardNumberString = cardNumberString + attributeValue;
                    break;
                case "cardNumber2":
                    cardNumberString = cardNumberString + attributeValue;
                    break;
                case "cardNumber3":
                    cardNumberString = cardNumberString + attributeValue;
                    break;
                case "cardNumber4":
                    cardNumberString = cardNumberString + attributeValue;
                    singleLampParamOne.setCardNumber(cardNumberString);
                    singleLampParamTwo.setCardNumber(cardNumberString);
                    break;

                //通道2当前电流2有效值
                //通道2当前有功功率2
                //通道2有功电能（累计）高字节
                //通道2有功电能（累计）低字节
                case "electricCurrentTwo":
                    Integer electricCurrentTwo = Integer.parseInt(attributeValue, 16);
                    singleLampParamTwo.setElectricCurrent(electricCurrentTwo.doubleValue());
                    break;
                case "powerTwo":
                    Integer powerTwo = Integer.parseInt(attributeValue, 16);
                    singleLampParamTwo.setPower(powerTwo.doubleValue());
                    break;
                case "electricEnergyTwoHigh":
                    electricEnergyTwoString = electricEnergyTwoString + attributeValue;
                    break;
                case "electricEnergyTwoLow":
                    electricEnergyTwoString = electricEnergyTwoString + attributeValue;
                    Integer electricEnergyTwo = Integer.parseInt(electricEnergyTwoString, 16);
                    singleLampParamTwo.setElectricEnergy(electricEnergyTwo.doubleValue()/10);
                    break;

                //通道2灯具累计工作时长高字节
                //通道2灯具累计工作时长低字节
                //通道2灯具工作时长高字节
                //通道2灯具工作时长低字节
                case "lampATimeTwoHigh":
                    lampATimeTwoString = lampATimeTwoString + attributeValue;
                    break;
                case "lampATimeTwoLow":
                    lampATimeTwoString = lampATimeTwoString + attributeValue;
                    Integer lampATimeTwo = Integer.parseInt(lampATimeTwoString, 16);
                    singleLampParamTwo.setLampATime(lampATimeTwo.doubleValue());
                    break;
                case "lampTimeTwoHigh":
                    lampTimeTwoString = lampTimeTwoString + attributeValue;
                    break;
                case "lampTimeTwoLow":
                    lampTimeTwoString = lampTimeTwoString + attributeValue;
                    Integer lampTimeTwo = Integer.parseInt(lampTimeTwoString, 16);
                    singleLampParamTwo.setLampTime(lampTimeTwo.doubleValue());
                    break;

                //新增参数
                case "signalIntensity":
                    int signalIntensity = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setSignalIntensity(signalIntensity);
                    singleLampParamTwo.setSignalIntensity(signalIntensity);
                    break;
                case "voltageFrequency":
                    int voltageFrequency = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setVoltageFrequency(voltageFrequency);
                    singleLampParamTwo.setVoltageFrequency(voltageFrequency);
                    break;
                case "powerFactorOne":
                    int powerFactorOne = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setPowerFactor(powerFactorOne);
                    break;
                case "powerFactorTwo":
                    int powerFactorTwo = Integer.parseInt(attributeValue, 16);
                    singleLampParamTwo.setPowerFactor(powerFactorTwo);
                    break;
                case "poleOffsetXY":
                    int poleOffsetXY = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setPoleOffsetXy(poleOffsetXY);
                    singleLampParamTwo.setPoleOffsetXy(poleOffsetXY);
                    break;
                case "XYAxisInformation":
                    int XYAxisInformation = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setXyAxisInformation(XYAxisInformation);
                    singleLampParamTwo.setXyAxisInformation(XYAxisInformation);
                    break;
                case "ZAxisInformation":
                    int ZAxisInformation = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setZAxisInformation(ZAxisInformation);
                    singleLampParamTwo.setZAxisInformation(ZAxisInformation);
                    break;
                case "leakageValue":
                    int leakageValue = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setLeakageValue(leakageValue);
                    singleLampParamTwo.setLeakageValue(leakageValue);
                    break;
                case "softwareVersion":
                    Integer softwareVersion = Integer.parseInt(attributeValue, 16);
                    singleLampParamOne.setSoftwareVersion(String.valueOf(softwareVersion));
                    singleLampParamTwo.setSoftwareVersion(String.valueOf(softwareVersion));
                    break;
                case "atSunrise":
                    int atHourNum = Integer.parseInt(attributeValue.substring(0, 2), 16);
                    int atMinuteNum = Integer.parseInt(attributeValue.substring(2, 4),16);
                    String atTime = atHourNum + ":" + atMinuteNum;
                    try {
                        Date parse = simpleDateFormat.parse(atTime);
                        singleLampParamOne.setAtSunrise(parse);
                        singleLampParamTwo.setAtSunrise(parse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "downSunrise":
                    int downHourNum = Integer.parseInt(attributeValue.substring(0, 2), 16);
                    int downMinuteNum = Integer.parseInt(attributeValue.substring(2, 4),16);
                    String downTime = downHourNum + ":" + downMinuteNum;
                    try {
                        Date parse = simpleDateFormat.parse(downTime);
                        singleLampParamOne.setDownSunrise(parse);
                        singleLampParamTwo.setDownSunrise(parse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        singleLampParamList.add(singleLampParamOne);
        singleLampParamList.add(singleLampParamTwo);
        return singleLampParamList;
    }

    /**
     * 发送nb报文
     * @return
     */
    public static boolean sendMessage(String message, String topic){
        boolean flag = false;
        //System.out.println("Hex:"+Hex.encodeHexString(message.getBytes()));
        try {
            MqttSender mqttSender = new MqttSender();
            mqttSender.send(topic,message);
            flag = true;
        }catch (Exception e){
            System.out.println("发送失败，阻止操作");
        }
        return flag;
    }

    /**
     * 判断最新的消息是否改变，并提醒前台
     * @param information
     */
    /*public static void remind(SingleLampParam information,Integer topicNum){
        String deviceNum = information.getDeviceNum();
        Integer routesNum = information.getLoopNum();
        //获取数据库中的当前信息
        SingleLampParam singleLampParamOld = singleLampParamService.getSingleLampOne(deviceNum,routesNum,topicNum);
        log.info("解析报文更新数据库信息参数：" + singleLampParamOld);
        if(singleLampParamOld!=null){
            //复制一份singleLampOld为singleLampParamNew
            SingleLampParam singleLampParamNew = new SingleLampParam();
            CompareUtil.copyPropertiesIgnoreNull(singleLampParamOld, singleLampParamNew);
            //将information的非null属性设置到singleLampParamNew中
            CompareUtil.copyPropertiesIgnoreNull(information, singleLampParamNew);
            //对比singleLampOld和singleLampNew
            Map<String, List<Object>> stringListMap = CompareUtil.compareFields(singleLampParamOld, singleLampParamNew,new String[]{"id"});
            System.out.println("stringListMap : " + stringListMap);

            if(information.getElectricEnergy()!=null){
                //本次上传信息中的累计能耗数据
                Double electricEnergyNew = information.getElectricEnergy();
                //获取当前数据库的能耗数据
                Double electricEnergy = singleLampParamOld.getElectricEnergy();
                LampEnergy lampEnergy = new LampEnergy();
                lampEnergy.setCreateTime(new Date());
                lampEnergy.setDeviceId(singleLampParamOld.getId());
                lampEnergy.setCumulativeEnergy(information.getElectricEnergy());
                Double energyDouble = electricEnergyNew-electricEnergy;
                lampEnergy.setEnergy(energyDouble.floatValue());
                lampEnergyService.save(lampEnergy);
                lampEnergyService.cumulativeEnergyByTime(singleLampParamOld.getId(),energyDouble.floatValue());
            }

            LampDevice lampDevice = new LampDevice();
            lampDevice.setLastOnlineTime(new Date());
            UpdateWrapper<LampDevice> updateWrapper = new UpdateWrapper();
            if (!stringListMap.isEmpty()){

                boolean state = false;
                boolean brightness = false;

                try {
                    //不同，判断是否为状态不同
                    Set<String> keySet = stringListMap.keySet();
                    for (String key : keySet) {
                        if("brightState".equals(key)){
                            //状态不同
                            List<Object> stateList = stringListMap.get("brightState");
                            //获取最新状态
                            Integer stateOneNew = (Integer)stateList.get(1);
                            if (stateOneNew!=null){
                                //往前台推
                                System.out.println("状态更改" + stateOneNew);
                                state = true;
                            }
                        }
                        if("brightness".equals(key)){
                            //亮度不同
                            List<Object> stateList = stringListMap.get("brightness");
                            //获取最新亮度
                            Integer brightnessOneNew = (Integer)stateList.get(1);
                            if(brightnessOneNew!=null){
                                System.out.println("亮度更改：" + brightnessOneNew + "%");
                                brightness = true;
                            }
                        }
                    }
                    singleLampParamNew.setCreateTime(new Date());
                }catch (Exception e){
                    System.out.println("上报出错");
                }

                //修改数据
                try {
                    if(singleLampParamOld !=null){
                        singleLampParamService.updateOne(singleLampParamNew);
                        updateWrapper.eq("id",singleLampParamNew.getDeviceId());
                        *//*if(state||brightness){
                            if(state){
                                lampDevice.setBrightState(Integer.parseInt(singleLampParamNew.getStateOne()));
                            }
                            if(brightness){
                                lampDevice.setBrightness(singleLampParamNew.getBrightnessOne());
                            }
                        }
                    KafkaMessage kafkaMessage = new KafkaMessage();
                    kafkaMessage.setType(1);
                    kafkaMessage.setIs2All(2);
                    kafkaMessage.setUserIds(null);
                    WebsocketQuery<RefreshVO> websocketQuery = new WebsocketQuery<>();
                    websocketQuery.setType(16);
                    RefreshVO refreshVO = new RefreshVO();
                    refreshVO.setRefresh(1);
                    websocketQuery.setData(refreshVO);
                    String s = JSON.toJSONString(websocketQuery);
                    kafkaMessage.setMessage(s);
                    kafkaMessageService.sendMessage("websocket1",kafkaMessage);*//*

                    }
                }catch (Exception e){
                    System.out.println("调用插入方法出错");
                }


                //日志输出
            }else {
                //不做修改，日志输出
            }
            //修改数据
            lampDeviceService.update(lampDevice,updateWrapper);

        }


    }*/

    /**
     * 修改设备详情
     * @param deviceId
     * @param deviceTypeId
     * @param singleLampParamTempVO
     */
    public static void updateSystemDeviceDetail(Integer deviceId, Integer deviceTypeId, SingleLampParamTempVO singleLampParamTempVO){
        // 电压
        Double voltage = singleLampParamTempVO.getVoltage();
        if(voltage!=null){
            SystemDeviceParameter systemDeviceParameter = systemDeviceParameterService.selectByName(deviceTypeId, "电压");
            Integer systemDeviceParameterId = null;
            if (systemDeviceParameter != null) {
                systemDeviceParameterId = systemDeviceParameter.getId();
            }
            if (systemDeviceParameterId != null) {
                lampDeviceParameterService.saveParamValue(deviceId, systemDeviceParameterId, String.valueOf(voltage));
            }
        }
        // 电流
        Double electricCurrent = singleLampParamTempVO.getElectricCurrent();
        if(electricCurrent!=null){
            SystemDeviceParameter systemDeviceParameter = systemDeviceParameterService.selectByName(deviceTypeId, "电流");
            Integer systemDeviceParameterId = null;
            if (systemDeviceParameter != null) {
                systemDeviceParameterId = systemDeviceParameter.getId();
            }
            if (systemDeviceParameterId != null) {
                lampDeviceParameterService.saveParamValue(deviceId, systemDeviceParameterId, String.valueOf(electricCurrent));
            }
        }
        // 功率
        Double power = singleLampParamTempVO.getPower();
        if(power!=null){
            SystemDeviceParameter systemDeviceParameter = systemDeviceParameterService.selectByName(deviceTypeId, "功率");
            Integer systemDeviceParameterId = null;
            if (systemDeviceParameter != null) {
                systemDeviceParameterId = systemDeviceParameter.getId();
            }
            if (systemDeviceParameterId != null) {
                lampDeviceParameterService.saveParamValue(deviceId, systemDeviceParameterId, String.valueOf(power));
            }
        }
        // 能耗
        Double electricEnergy = singleLampParamTempVO.getElectricEnergy();
        if(electricEnergy!=null){
            SystemDeviceParameter systemDeviceParameter = systemDeviceParameterService.selectByName(deviceTypeId, "能耗");
            Integer systemDeviceParameterId = null;
            if (systemDeviceParameter != null) {
                systemDeviceParameterId = systemDeviceParameter.getId();
            }
            if (systemDeviceParameterId != null) {
                lampDeviceParameterService.saveParamValue(deviceId, systemDeviceParameterId, String.valueOf(electricEnergy));
            }
        }
        // 温度
        Double moduleTemperature = singleLampParamTempVO.getModuleTemperature();
        if(moduleTemperature!=null){
            SystemDeviceParameter systemDeviceParameter = systemDeviceParameterService.selectByName(deviceTypeId, "温度");
            Integer systemDeviceParameterId = null;
            if (systemDeviceParameter != null) {
                systemDeviceParameterId = systemDeviceParameter.getId();
            }
            if (systemDeviceParameterId != null) {
                lampDeviceParameterService.saveParamValue(deviceId, systemDeviceParameterId, String.valueOf(moduleTemperature));
            }
        }
        // 时长
        Double lampTime = singleLampParamTempVO.getLampTime();
        if(lampTime!=null){
            SystemDeviceParameter systemDeviceParameter = systemDeviceParameterService.selectByName(deviceTypeId, "时长");
            Integer systemDeviceParameterId = null;
            if (systemDeviceParameter != null) {
                systemDeviceParameterId = systemDeviceParameter.getId();
            }
            if (systemDeviceParameterId != null) {
                lampDeviceParameterService.saveParamValue(deviceId, systemDeviceParameterId, String.valueOf(lampTime));
            }
        }
        // 累计时长
        Double lampATime = singleLampParamTempVO.getLampATime();
        if(lampATime!=null){
            SystemDeviceParameter systemDeviceParameter = systemDeviceParameterService.selectByName(deviceTypeId, "累计时长");
            Integer systemDeviceParameterId = null;
            if (systemDeviceParameter != null) {
                systemDeviceParameterId = systemDeviceParameter.getId();
            }
            if (systemDeviceParameterId != null) {
                lampDeviceParameterService.saveParamValue(deviceId, systemDeviceParameterId, String.valueOf(lampATime));
            }
        }
    }

    /**
     * 判断最新的消息是否改变，并提醒前台
     * @param information
     */
    public static void remind(SingleLampParamTempVO information,Integer deviceTypeId){
        String deviceNum = information.getDeviceNum();
        Integer routesNum = information.getLoopNum();
        //获取对应设备id
        List<Integer> specialDeviceTypeList = Arrays.asList(2, 4, 6, 8, 10, 15);
        Integer deviceId = null;
        if(specialDeviceTypeList.contains(deviceTypeId)){
            deviceId = lampDeviceParameterService.selectDeviceIdByLoopNum(deviceNum, String.valueOf(routesNum), deviceTypeId);
        }else {
            Result systemDeviceById = systemDeviceService.getSystemDeviceById(null, null, deviceNum, deviceTypeId);
            if(systemDeviceById.getData()!=null){
                List<SystemDevice> systemDeviceList = (List<SystemDevice>)systemDeviceById.getData();
                deviceId = systemDeviceList.get(0).getId();
            }
        }
        //不存在该设备时不处理
        if(deviceId==null){
            return;
        }
        //修改设备详情
        updateSystemDeviceDetail(deviceId,deviceTypeId,information);
        SingleLampParam singleLampParamOld = singleLampParamService.getlastTimeByDeviceId(deviceId);
        if(singleLampParamOld==null){
            singleLampParamOld = new SingleLampParam();
            singleLampParamOld.setDeviceId(deviceId);
        }
        if(singleLampParamOld.getElectricEnergy()==null){
            singleLampParamOld.setElectricEnergy(0.0);
        }
        //SingleLampParam singleLampParamOld = singleLampParamService.getSingleLampOne(deviceNum,routesNum,topicNum);
        log.info("解析报文更新数据库信息参数：" + singleLampParamOld);
        if(information!=null){
            //复制一份singleLampOld为singleLampParamNew
            SingleLampParam singleLampParamNew = new SingleLampParam();
            CompareUtil.copyPropertiesIgnoreNull(singleLampParamOld, singleLampParamNew);
            //将information的非null属性设置到singleLampParamNew中
            CompareUtil.copyPropertiesIgnoreNull(information, singleLampParamNew);
            //对比singleLampOld和singleLampNew
            Map<String, List<Object>> stringListMap = CompareUtil.compareFields(singleLampParamOld, singleLampParamNew,new String[]{"id"});
            System.out.println("stringListMap : " + stringListMap);

            if(information.getElectricEnergy()!=null){
                //本次上传信息中的累计能耗数据
                Double electricEnergyNew = information.getElectricEnergy();
                //获取当前数据库的能耗数据
                Double electricEnergy = singleLampParamOld.getElectricEnergy();
                LampEnergy lampEnergy = new LampEnergy();
                lampEnergy.setCreateTime(new Date());
                lampEnergy.setDeviceId(singleLampParamOld.getDeviceId());
                lampEnergy.setCumulativeEnergy(information.getElectricEnergy());
                Double energyDouble = electricEnergyNew-electricEnergy;
                lampEnergy.setEnergy(energyDouble.floatValue());
                lampEnergyService.save(lampEnergy);
                lampEnergyService.cumulativeEnergyByTime(singleLampParamOld.getDeviceId(),energyDouble.floatValue());
            }

            SystemDevice systemDevice = new SystemDevice();
            systemDevice.setId(deviceId);
            systemDevice.setLastOnlineTime(new Date());
            if (!stringListMap.isEmpty()){

                try {
                    //不同，判断是否为状态不同
                    Set<String> keySet = stringListMap.keySet();
                    for (String key : keySet) {
                        if("brightState".equals(key)){
                            //状态不同
                            List<Object> stateList = stringListMap.get("brightState");
                            //获取最新状态
                            Integer stateOneNew = (Integer)stateList.get(1);
                            if (stateOneNew!=null){
                                //状态更改
                                systemDevice.setDeviceState(stateOneNew);
                            }
                        }
                        if("brightness".equals(key)){
                            //亮度不同
                            List<Object> stateList = stringListMap.get("brightness");
                            //获取最新亮度
                            Integer brightnessOneNew = (Integer)stateList.get(1);
                            if(brightnessOneNew!=null){
                                //亮度更改
                                SystemDeviceParameter systemDeviceParameter = systemDeviceParameterService.selectByName(deviceTypeId, "亮度");
                                lampDeviceParameterService.saveParamValue(systemDevice.getId(),systemDeviceParameter.getId(),String.valueOf(brightnessOneNew));
                            }
                        }
                        if("alarm".equals(key)){
                            List<Object> stateList = stringListMap.get("alarm");
                            String alarmString = (String)stateList.get(1);
                            SlLampPost slLampPost = systemDeviceService.selectLampPostByDeviceId(deviceId);
                            if(slLampPost!=null){
                                alarmService.addLampAlarm(slLampPost,alarmString,deviceId);
                            }
                        }
                        if("softwareVersion".equals(key)){
                            //亮度不同
                            List<Object> stateList = stringListMap.get("softwareVersion");
                            //获取最新亮度
                            String softwareVersion = (String)stateList.get(1);
                            if(softwareVersion!=null){
                                //亮度更改
                                SystemDeviceParameter systemDeviceParameter = systemDeviceParameterService.selectByName(deviceTypeId, "硬件版本");
                                lampDeviceParameterService.saveParamValue(systemDevice.getId(),systemDeviceParameter.getId(),softwareVersion);
                            }
                        }
                    }
                    singleLampParamNew.setCreateTime(new Date());
                }catch (Exception e){
                    System.out.println("上报出错");
                }

                //修改数据
                try {
                    singleLampParamNew.setId(null);
                        singleLampParamService.save(singleLampParamNew);
                    /*if(state||brightness){
                        if(state){
                            lampDevice.setBrightState(Integer.parseInt(singleLampParamNew.getStateOne()));
                        }
                        if(brightness){
                            lampDevice.setBrightness(singleLampParamNew.getBrightnessOne());
                        }
                    }
                    KafkaMessage kafkaMessage = new KafkaMessage();
                    kafkaMessage.setType(1);
                    kafkaMessage.setIs2All(2);
                    kafkaMessage.setUserIds(null);
                    WebsocketQuery<RefreshVO> websocketQuery = new WebsocketQuery<>();
                    websocketQuery.setType(16);
                    RefreshVO refreshVO = new RefreshVO();
                    refreshVO.setRefresh(1);
                    websocketQuery.setData(refreshVO);
                    String s = JSON.toJSONString(websocketQuery);
                    kafkaMessage.setMessage(s);
                    kafkaMessageService.sendMessage("websocket1",kafkaMessage);*/

                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("调用插入方法出错");
                }
            }
            //修改数据
            systemDeviceService.updateById(systemDevice);
        }

    }


    /**
     * 发送Lora报文
     * @param message
     * @return
     */
    public static boolean sendLoraMessage(String message, String DevEUI){

        try {
            //请求头token
            Map<String,String> headers = new HashMap<>();
            headers.put("x-access-token",loraApi.getToken());
            //headers.put("Content-Type","application/json");
            //将报文转为Base64字节数组
            byte[] bytes = HexUtil.hexStringToBytes(message);
            Base64 base64 = new Base64();
            String base64Sign = base64.encodeToString(bytes);
            //封装参数并转为Json字符串
            LoraLssueOrderParam loraLssueOrderParam = new LoraLssueOrderParam();
            loraLssueOrderParam.setDevEUI(DevEUI);
            loraLssueOrderParam.setConfirmed(false);
            loraLssueOrderParam.setFPort(9);
            loraLssueOrderParam.setData(base64Sign);
            String loraParamString = JSON.toJSONString(loraLssueOrderParam);
            //通过http请求发送消息
            String post = HttpsUtil.httpsSendJson(loraApi.getSendUrl(), headers, loraParamString, "POST");
            post = post.substring(post.indexOf("{"));//截取
            System.out.println(post);
            LoraIssueOrderReturn loraIssueOrderReturn = JSON.parseObject(post, LoraIssueOrderReturn.class);
            Double code = loraIssueOrderReturn.getCode();
            if(code == 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 发送LoraMc报文
     * @param message
     * @return
     */
    public static boolean sendLoraMc(String message, String McEUI){

        try {
            //请求头token
            Map<String,String> headers = new HashMap<>();
            headers.put("x-access-token",loraApi.getToken());
            //将报文转为Base64字节数组
            byte[] bytes = HexUtil.hexStringToBytes(message);
            Base64 base64 = new Base64();
            String base64Sign = base64.encodeToString(bytes);
            //封装参数并转为Json字符串
            LoraMcParam loraMcParam = new LoraMcParam();
            loraMcParam.setMcEUI(McEUI);
            loraMcParam.setFPort(8);
            loraMcParam.setData(base64Sign);
            String loraParamString = JSON.toJSONString(loraMcParam);
            //通过http请求发送消息
            String post = HttpsUtil.httpsSendJson(loraApi.getSendMcUrl(), headers, loraParamString, "POST");
            post = post.substring(post.indexOf("{"));//截取
            LoraIssueOrderReturn loraIssueOrderReturn = JSON.parseObject(post, LoraIssueOrderReturn.class);
            Double code = loraIssueOrderReturn.getCode();
            if(code == 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }



    /**
     * 创建Lora节点（设备）
     * @param DevName
     * @param DevEUI
     * @return
     */
    public static boolean createNode(String DevName, String DevEUI){

        try {
            //请求头token
            Map<String,String> headers = new HashMap<>();
            headers.put("x-access-token",loraApi.getToken());

            //封装参数并转为Json字符串
            LoraCreateNodeParam loraCreateNodeParam = new LoraCreateNodeParam();
            loraCreateNodeParam.setAppID(Integer.parseInt(loraApi.getAppId()));
            //自动生成
            loraCreateNodeParam.setDevName(DevName);
            //非自动生成
            loraCreateNodeParam.setDevEUI(DevEUI);
            String AppEUI = DevEUI.substring(0, 8)+"00000000";
            loraCreateNodeParam.setAppEUI(AppEUI);
            loraCreateNodeParam.setRegion("CN470");
            loraCreateNodeParam.setSubnet("CH_00-07");
            loraCreateNodeParam.setSupportClassB(false);
            loraCreateNodeParam.setSupportClassC(true);
            loraCreateNodeParam.setAuthType("abp");
            //loraCreateNodeParam.setAppKey("");
            String DevAddr = DevEUI.substring(8, 16);
            loraCreateNodeParam.setDevAddr(DevAddr);
            loraCreateNodeParam.setAppSKey(loraApi.getAppSKey());
            loraCreateNodeParam.setNwkSKey(loraApi.getNwkSKey());
            loraCreateNodeParam.setMacVersion("1.0.2");
            String loraCreateNodeParamString = JSON.toJSONString(loraCreateNodeParam);


            //通过http请求发送消息
            String post = HttpsUtil.httpsSendJson(loraApi.getCreateNode(),headers,loraCreateNodeParamString,"POST");
            post = post.substring(post.indexOf("{"));//截取
            LoraIssueOrderReturn loraIssueOrderReturn = JSON.parseObject(post, LoraIssueOrderReturn.class);
            Double code = loraIssueOrderReturn.getCode();
            System.out.println(post);
            if(code == 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lora多播组增加节点（设备）
     * @param devEUI
     * @return
     */
    public static boolean createNodeMc(String devEUI, String mcEUI){
        try {
            //请求头token
            Map<String,String> headers = new HashMap<>();
            headers.put("x-access-token",loraApi.getToken());

            List<String> devEUIs = new ArrayList<>();
            devEUIs.add(devEUI);
            //封装参数并转为Json字符串
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mcEUI",mcEUI);
            jsonObject.put("devEUIs",devEUIs);
            //通过http请求发送消息
            String post = HttpsUtil.httpsSendJson(loraApi.getCreateNodeMc(),headers,jsonObject.toJSONString(),"POST");
            post = post.substring(post.indexOf("{"));//截取
            LoraIssueOrderReturn loraIssueOrderReturn = JSON.parseObject(post, LoraIssueOrderReturn.class);
            Double code = loraIssueOrderReturn.getCode();
            System.out.println(post);
            if(code == 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除Lora节点（设备）
     * @param devEuiList
     * @return
     */
    public static boolean deleteNode(List<String> devEuiList){
        try {
            //请求头token
            Map<String,String> headers = new HashMap<>();
            headers.put("x-access-token",loraApi.getToken());

            //封装参数并转为Json字符串
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("devEUIs",devEuiList);
            String devEUIs = jsonObject.toJSONString();


            //通过http请求发送消息
            String post = HttpsUtil.httpsSendJson(loraApi.getDeleteNode(),headers,devEUIs,"POST");
            post = post.substring(post.indexOf("{"));//截取
            LoraIssueOrderReturn loraIssueOrderReturn = JSON.parseObject(post, LoraIssueOrderReturn.class);
            Double code = loraIssueOrderReturn.getCode();
            System.out.println(post);
            if(code == 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lora多播组移除节点（设备）
     * @param devEUI
     * @param mcEUI
     * @return
     */
    public static boolean deleteNodeMc(String devEUI, String mcEUI){
        try {
            //请求头token
            Map<String,String> headers = new HashMap<>();
            headers.put("x-access-token",loraApi.getToken());

            List<String> devEUIs = new ArrayList<>();
            devEUIs.add(devEUI);
            //封装参数并转为Json字符串
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mcEUI",mcEUI);
            jsonObject.put("devEUIs",devEUIs);
            //通过http请求发送消息
            String post = HttpsUtil.httpsSendJson(loraApi.getDeleteNodeMc(),headers,jsonObject.toJSONString(),"POST");
            post = post.substring(post.indexOf("{"));//截取
            LoraIssueOrderReturn loraIssueOrderReturn = JSON.parseObject(post, LoraIssueOrderReturn.class);
            Double code = loraIssueOrderReturn.getCode();
            System.out.println(post);
            if(code == 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断发送途径
     * @param message
     * @param sendMode
     * @return
     */
    public static boolean sendByMode(String message, String sendMode, String sendId){
        if("nb".equals(sendMode)){
            return MessageOperationUtil.sendMessage(message,sendId);
        }else if ("cat1".equals(sendMode)){
            return MessageOperationUtil.sendMessage(message,sendId);
        }else if("lora_old".equals(sendMode)){
            return MessageOperationUtil.sendLoraMessage(message,sendId);
        }else if("lora_new".equals(sendMode)){
            return MessageOperationUtil.sendNewLoraMessage(message,sendId);
        }else if("loraMc".equals(sendMode)){
            return MessageOperationUtil.sendLoraMc(message,sendId);
        }else if("dxnb".equals(sendMode)) {
            boolean result = false;
            try {
                result = CTWingApi.sendMessage(sendId, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }else if("dxCat1".equals(sendMode)){
            boolean result = false;
            try {
                result = CtWingMqttUtil.sendMessage(sendId, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }else {
            return false;
        }
    }

    /**
     * 判断发送途径
     * @param message
     * @param deviceTypeId
     * @return
     */
    public static boolean sendByMode(String message, Integer deviceTypeId, String sendId){
        if(deviceTypeId==1||deviceTypeId==2){
            return MessageOperationUtil.sendMessage(message,sendId);
        }else if (deviceTypeId==5||deviceTypeId==6){
            return MessageOperationUtil.sendMessage(message,sendId);
        }else if(deviceTypeId==3||deviceTypeId==4){
            return MessageOperationUtil.sendLoraMessage(message,sendId);
        }else if(deviceTypeId==9||deviceTypeId==10){
            return MessageOperationUtil.sendNewLoraMessage(message,sendId);
        }else if(deviceTypeId==7||deviceTypeId==8) {
            boolean result = false;
            try {
                result = CTWingApi.sendMessage(sendId, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }else if(deviceTypeId.equals(14)||deviceTypeId.equals(15)){
            boolean result = false;
            try {
                result = CtWingMqttUtil.sendMessage(sendId,message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }else {
            return false;
        }
    }




    /**
     * 定时发送场景使能消息
     * @param DateString
     * @param scene
     * @param ADR
     * @param sendMode
     * @param executionMode
     * @param sendId
     */
    public static void scheduledTasks(String DateString, Integer scene, String ADR, String sendMode, int executionMode, String sendId){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(DateString);
            Timer timer = new Timer(DateString);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    //执行任务，发送场景使能或关闭的消息
                    int ADDNum = scene/2;
                    int sceneADDNum = 0x200E + ADDNum;
                    String sceneDR = "06";
                    String sceneADD = HexUtil.intToHexString(sceneADDNum);
                    String sceneLENGTH = "";
                    String sceneINFO = "";
                    if(scene%2==0){
                        if(executionMode==1){
                            sceneINFO = "1FFF";
                        }else if(executionMode==0){
                            sceneINFO = "0FFF";
                        }else if(executionMode==2){
                            sceneINFO = "F1FF";
                        }else if(executionMode==3){
                            sceneINFO = "F0FF";
                        }
                    }else{
                        if(executionMode==1){
                            sceneINFO = "FF1F";
                        }else if(executionMode==0){
                            sceneINFO = "FF0F";
                        }else if(executionMode==2){
                            sceneINFO = "FFF1";
                        }else if(executionMode==3){
                            sceneINFO = "FFF0";
                        }
                    }
                    String scenemessage = "";
                    if("nb".equals(sendMode)){
                        scenemessage = MessageOperationUtil.generateMessage(ADR, sceneDR, sceneADD, sceneLENGTH, sceneINFO);
                    }else if("cat1".equals(sendMode)){
                        scenemessage = MessageOperationUtil.generateMessage(ADR, sceneDR, sceneADD, sceneLENGTH, sceneINFO);
                    }else if("lora_old".equals(sendMode)){
                        scenemessage = MessageOperationUtil.generateMessage("00000000", sceneDR, sceneADD, sceneLENGTH, sceneINFO);
                    }else if("lora_new".equals(sendMode)){
                        scenemessage = MessageOperationUtil.generateMessage("00000000", sceneDR, sceneADD, sceneLENGTH, sceneINFO);
                    }else if("dxnb".equals(sendMode)){
                        scenemessage = MessageOperationUtil.generateMessage("00000000", sceneDR, sceneADD, sceneLENGTH, sceneINFO);
                    }
                    //发送消息
                    sendByMode(scenemessage,sendMode,sendId);
                    System.out.println("定时任务："+scenemessage);
                    Date date1 = new Date();
                    if(date1.getTime()+10>date.getTime()){
                        timer.cancel();
                    }
                }
            }, date,20);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 定时发送消息
     * @param DateString
     * @param sendMode
     * @param sendId
     */
    public static void sendTimingMessage(String DateString, String message, String sendMode, String sendId){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(DateString);
            Timer timer = new Timer(DateString);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    //发送消息
                    sendByMode(message,sendMode,sendId);
                    System.out.println("定时任务发送消息："+message);
                    Date date1 = new Date();
                    if(date1.getTime()+1000>date.getTime()){
                        timer.cancel();
                    }
                }
            }, date,200);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 解析ota文件
     * @param realPath
     * @param ADR
     * @return
     */
    public static List<String> analysisOtaFile(String realPath,String ADR,Integer deviceTypeId){
        if(deviceTypeId>10&&deviceTypeId!=14&&deviceTypeId!=15){
            return null;
        }
        /*Result<LampDevice> byNum = lampDeviceService.getByNum(ADR,model,factory);
        LampDevice lampDeviceByNum = byNum.getData();*/
        FileInputStream file = null;
        List<String> messageInfoList = new ArrayList<>();
        List<String> resultList = new ArrayList<>();
        try {
            file = new FileInputStream(realPath);
            // 读取文件信息，每48个字节为一个byte数组
            int available = file.available();
            List<byte[]> list = new ArrayList<>();

            //读取文件信息
            byte[] fileBytes = new byte[available];
            file.read(fileBytes);
            //判断字节数量
            int bytesTotal = 0;
            if(available%2!=0){
                System.out.println("数据字节数错误");
                resultList.add("01010101");
                return resultList;
            }else {
                bytesTotal = available/2;
            }
            //数组拆分
            for (int i = 0; i < bytesTotal; i++) {
                byte[] bytes = new byte[2];
                byte fileByte = fileBytes[2 * i];
                byte fileByte1 = fileBytes[2 * i + 1];
                bytes[0] = fileByte;
                bytes[1] = fileByte1;
                list.add(bytes);
            }
            //转换为16进制字符串
            String messageInfo = "";
            List<Integer> specialDeviceTypeList = Arrays.asList(1, 2, 5, 6);
            for (int k = 0; k < list.size(); k++) {
                String hexString = HexUtil.bytesTohex(list.get(k)).replace(" ", "");
                messageInfo += hexString;
                if(specialDeviceTypeList.contains(deviceTypeId)){
                    if((k+1)%50==0||(k+1)==list.size()){
                        messageInfoList.add(messageInfo);
                        messageInfo = "";
                    }
                }else if(deviceTypeId==7||deviceTypeId==8||deviceTypeId==14||deviceTypeId==15){
                    if((k+1)%100==0||(k+1)==list.size()){
                        messageInfoList.add(messageInfo);
                        messageInfo = "";
                    }
                }else {
                    if((k+1)%17==0||(k+1)==list.size()){
                        messageInfoList.add(messageInfo);
                        messageInfo = "";
                    }
                }

            }
            //验证包数
            int pageNum = 0;
            if(specialDeviceTypeList.contains(deviceTypeId)){
                if (available % 100 == 0) {
                    pageNum = available / 100;
                } else {
                    pageNum = available / 100 + 1;
                }
            }else if(deviceTypeId==7||deviceTypeId==8||deviceTypeId==14||deviceTypeId==15){
                if (available % 200 == 0) {
                    pageNum = available / 200;
                } else {
                    pageNum = available / 200 + 1;
                }
            }else {
                if (available % 34 == 0) {
                    pageNum = available / 34;
                } else {
                    pageNum = available / 34 + 1;
                }
            }

            if(messageInfoList!=null&&messageInfoList.size()==pageNum){
                System.out.println("操作成功");
            }else {
                resultList.add("01010101");
                return resultList;
            }
        }catch (Exception e){
            resultList.add("01010101");
            return resultList;
        }finally {
            if(file!=null){
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        List<String> messageList = new ArrayList<>();
        if(deviceTypeId==1||deviceTypeId==2){
            messageList = MessageGeneration.nbOta(ADR, messageInfoList);
        }else if (deviceTypeId==5||deviceTypeId==6){
            messageList = CatOneMessageGeneration.catOneOta(ADR, messageInfoList);
        }else if(deviceTypeId==3||deviceTypeId==4){
            messageList = LoraOldMessageGeneration.loraOldOta(messageInfoList);
        }else if(deviceTypeId==9||deviceTypeId==10){
            messageList = LoraNewMessageGeneration.loraNewOta(messageInfoList);
        }else if(deviceTypeId==7||deviceTypeId==8){
            messageList = DxnbMessageGeneration.dxnbOta(messageInfoList);
        }else if (deviceTypeId==14||deviceTypeId==15){
            messageList = DxCatOneMessageGeneration.dxCatOneOta(messageInfoList);
        }
        return messageList;
    }


    /*
    * 以下是顺舟灯具方法
    * */




    /**
     * 生成报文
     * @param frameHeadMatch
     * @param adr
     * @param info
     * @return
     */
    public static String generateMessage(FrameHeadMatch frameHeadMatch,String adr,String info){
        String frameHead = frameHeadMatch.getFrameHead();
        String message = frameHead + adr + info;
        int lengthNum = message.length()/2+3;
        byte[] bytes = HexUtil.intToByteArray(lengthNum);
        byte[] newBytes = {bytes[3]};
        String length = HexUtil.bytesTohex(newBytes).replace(" ", "");
        //帧头 + 长度+ 设备地址 + 端口号 + 动作命令+CRC CRC
        message = frameHead + length + adr + info;
        String Crc16Value = Crc16.generateCrc16(message);
        message = message + Crc16Value;
        return message;
    }

    /**
     * 生成报文AA55协议头的报文
     * @param adr
     * @param info
     * @return
     */
    public static String generateMessage(String adr,String info){
        String frameHead = "AA55";
        int lengthNum = info.length()/2 + 4;
        byte[] bytes = HexUtil.intToByteArray(lengthNum);
        byte[] newBytes = {bytes[2],bytes[3]};
        String length = HexUtil.bytesTohex(newBytes).replace(" ", "");
        //帧头 + 长度 + 预留接口 + CRC CRC + 设备地址 + 动作命令
        String message = frameHead + length + "01000300";
        String Crc16Value = Crc16.generateCrc16(adr + info);
        message += Crc16Value + adr + info;
        return message;
    }

    /**
     * 解析报文
     * @param message
     * @return
     */
    public static List<String> parseMessage2(String message){
        List<String> resultList = new ArrayList<>();
        String frameHead = message.substring(0, 2);
        String espframeHead = message.substring(0, 4);
        String crc16String = "";
        String pendingCheckString = "";
        if("AA55".equals(espframeHead)){
            crc16String = message.substring(16,20);
            pendingCheckString = message.substring(20);
        }else {
            //先校验Crc16是否正确，直接截取最后两个字节，四个字符，进行校验
            int messageLength = message.length();
            //最后四个字符
            crc16String = message.substring(messageLength - 4);
            //除最后四个字符外的字符
            pendingCheckString = message.substring(0, messageLength - 4);
        }
        //进行Crc16校验
        String Crc16Value = Crc16.generateCrc16(pendingCheckString);
        if(Crc16Value.equals(crc16String)){
            if("AA5A".equals(espframeHead)){
                String length = message.substring(6,8);
                String adr = message.substring(8,16);

                int LengthValue = Integer.valueOf(length,16)-10;
                String info = message.substring(16,16+LengthValue*2);

                resultList.add(espframeHead);
                resultList.add(adr);
                resultList.add(info);
            }else if("30".equals(frameHead)){
                String length = message.substring(2,4);
                String adr = "00000000";

                int LengthValue = Integer.valueOf(length,16)-4;
                String info = message.substring(4,4+LengthValue*2);

                resultList.add(frameHead);
                resultList.add(adr);
                resultList.add(info);
            }else if("AA55".equals(espframeHead)){
                return parseMessage3(message);
            }else{
                String length = message.substring(2,4);
                String adr = message.substring(4,12);

                int LengthValue = Integer.valueOf(length,16)-8;
                String info = message.substring(12,12+LengthValue*2);

                resultList.add(frameHead);
                resultList.add(adr);
                resultList.add(info);
            }
        }else{
            System.out.println("Crc16校验错误");
        }
        return resultList;
    }
    /**
     * 解析报文AA55协议头的报文
     * @param message
     * @return
     */
    public static List<String> parseMessage3(String message){
        List<String> resultList = new ArrayList<>();
        String length = message.substring(4,8);
        String adr = message.substring(20,28);
        int LengthValue = Integer.valueOf(length,16)-4;
        String info = message.substring(28,28+LengthValue*2);

        resultList.add("AA55");
        resultList.add(adr);
        resultList.add(info);
        return resultList;
    }

    /**
     * 通过报文获取信息并处理
     * @param message
     * @param deviceId
     * @return
     */
    public static Result getShuncomInformation(String message,String deviceId){
        Result result = new Result();
        Result<LampDevice> byNum = lampDeviceService.getByNum(deviceId,"nb","EXC2");
        LampDevice lampDevice = byNum.getData();
        SingleLampParam singleLampOne = singleLampParamService.getSingleLampOne(deviceId, 1, 3);
        //解析报文，获取ADD,LENGTH和INFO
        List<String> resultList = MessageOperationUtil.parseMessage2(message);
        if(resultList.isEmpty()){
            return result;
        }
        String frameHead = resultList.get(0);
        String adr = resultList.get(1);
        String info = resultList.get(2);

        //设备编号
        Integer adrInt = Integer.parseInt(adr);
        switch (frameHead){
            case "11":
                //当前亮度（%）
                String brightness = info.substring(4, 6);
                int brightnessInt = Integer.parseInt(brightness,16);
                //当前状态（00开灯，01关灯）
                String state = info.substring(10, 12);
                int stateInt = 0;
                if("00".equals(state)){
                    stateInt = 1;
                }

                //电压值（V）
                String voltage = info.substring(18, 22);
                int voltageInt = Integer.parseInt(voltage, 16);
                Double voltageDouble = new Double(voltageInt)/ 100;
                //电流值（A）
                String current = info.substring(22, 26);
                int currentInt = Integer.parseInt(current, 16);
                Double currentDouble = new Double(currentInt)/ 1000;
                //有功功率（W）
                String activePower = info.substring(26, 30);
                int activePowerInt = Integer.parseInt(activePower, 16);
                Double activePowerDouble = new Double(activePowerInt)/ 10;
                //无功功率（W）
                String reactivePower = info.substring(30, 34);
                int reactivePowerInt = Integer.parseInt(reactivePower, 16);
                Double reactivePowerDouble = new Double(reactivePowerInt)/ 10;
                //频率值（Hz）
                String frequency = info.substring(34, 38);
                int frequencyInt = Integer.parseInt(frequency, 16);
                Double frequencyDouble = new Double(frequencyInt)/ 100;
                //温度值（摄氏度）
                String temperature = info.substring(38,42);
                int temperatureInt = Integer.parseInt(temperature, 16);
                Double temperatureDouble = new Double(temperatureInt)/ 100;

                TempParamVO tempParamVO = new TempParamVO();
                if(!"FFFFFFFF".equals(adr)){
                    tempParamVO.setId(adrInt);
                }
                tempParamVO.setBrightness(brightnessInt);
                tempParamVO.setState(state);
                tempParamVO.setVoltage(voltageDouble);
                tempParamVO.setCurrent(currentDouble);
                tempParamVO.setActivePower(activePowerDouble);
                tempParamVO.setReactivePower(reactivePowerDouble);
                tempParamVO.setFrequency(frequencyDouble);
                tempParamVO.setTemperature(temperatureDouble);

                List<SingleLampParam> singleLampByDeviceId = singleLampParamService.getSingleLampByDeviceId(lampDevice.getId(), null);
                SingleLampParam singleLampParam = singleLampByDeviceId.get(0);
                Integer brightStateNow = singleLampParam.getBrightState();
                Integer brightnessNow = singleLampParam.getBrightness();
                if(brightnessInt!=brightnessNow || stateInt!=brightStateNow){
                    singleLampParam.setBrightState(stateInt);
                    singleLampParam.setBrightness(brightnessInt);
                    singleLampParamService.updateOne(singleLampParam);
                }
                System.out.println(tempParamVO);
                break;
            case "26":
                //控制器运行时间（min）
                String runningMinute = info.substring(0, 8);
                Integer runningMinuteInt = Integer.parseInt(runningMinute, 16);
                //控制器重启次数
                String restartNum = info.substring(8, 12);
                Integer restartNumInt = Integer.parseInt(restartNum, 16);
                //有功电能（Kw/h）
                String activeEE = info.substring(14, 22);
                int activeEEInt = Integer.parseInt(activeEE, 16);
                Double activeEEDouble = new Double(activeEEInt)/ 100;
                //无功电能（Kw/h）
                String reactiveEE = info.substring(22, 30);
                int reactiveEEInt = Integer.parseInt(reactiveEE, 16);
                Double reactiveEEDouble = new Double(reactiveEEInt)/ 100;
                //视在电能（Kw/h）
                String apparentEE = info.substring(30, 38);
                int apparentEEInt = Integer.parseInt(apparentEE, 16);
                Double apparentEEDouble = new Double(apparentEEInt)/ 100;

                ControlMachineVO controlMachineVO = new ControlMachineVO();
                controlMachineVO.setId(adrInt);
                controlMachineVO.setRunningMinute(runningMinuteInt);
                controlMachineVO.setRestartNum(restartNumInt);
                controlMachineVO.setActiveElectricEnergy(activeEEDouble);
                controlMachineVO.setReactiveElectricEnergy(reactiveEEDouble);
                controlMachineVO.setApparentElectricEnergy(apparentEEDouble);

                System.out.println("灯具能耗更新代码略" + controlMachineVO);

                //获取当前数据库的能耗数据
                Double electricEnergy = singleLampOne.getElectricEnergy();
                LampEnergy lampEnergy = new LampEnergy();
                lampEnergy.setCreateTime(new Date());
                lampEnergy.setDeviceId(singleLampOne.getId());
                lampEnergy.setCumulativeEnergy(activeEEDouble);
                Double energyDouble = activeEEDouble-electricEnergy;
                lampEnergy.setEnergy(energyDouble.floatValue());
                lampEnergyService.save(lampEnergy);
                lampEnergyService.cumulativeEnergyByTime(singleLampOne.getId(),energyDouble.floatValue());
                /*SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd");
                //昨天的年月日
                Long dateTime = System.currentTimeMillis() - 86400000;
                Date date = new Date();
                date.setTime(dateTime);
                String yesterday = dateFormatJustDay.format(date);*/
                /*//前天的年月日
                Long dateTimeBefore = System.currentTimeMillis() - 172800000;
                Date dateBefore = new Date();
                date.setTime(dateTimeBefore);
                String yesterdayBefore = dateFormatJustDay.format(dateBefore);*/

                //获取昨天的对应日能耗表
                /*Result resultEnergyDay = lampEnergyService.select(lampDevice.getId(),yesterday);
                if(resultEnergyDay!=null){
                    //删除当天数据
                    lampEnergyService.deleteByEnergyTime(dateFormatJustDay.format(new Date()));
                    //将昨天的累计能耗和设备id提取出来
                    List<LampEnergy> lampEnergyList = (List<LampEnergy>)resultEnergyDay.getData();
                    Double energy = 0.0;
                    if(lampEnergyList==null||lampEnergyList.size()==0){
                        energy = activeEEDouble;
                    }else {
                        LampEnergy lampEnergyYtd = lampEnergyList.get(0);
                        Double cumulativeEnergy = lampEnergyYtd.getCumulativeEnergy();
                        energy = activeEEDouble - cumulativeEnergy;
                    }
                    //存入最新能耗数据
                    LampEnergy lampEnergy = new LampEnergy();
                    lampEnergy.setDeviceId(lampDevice.getId());
                    lampEnergy.setCumulativeEnergy(activeEEDouble);
                    //lampEnergy.setEnergyTime(dateFormatJustDay.format(new Date()));
                    lampEnergy.setCreateTime(new Date());
                    lampEnergy.setEnergy(energy.floatValue());
                    lampEnergyService.save(lampEnergy);
                }*/
                break;
            case "3A":
                Integer id = adrInt;
                String group = info;
                System.out.println("id："+id+" "+"group:"+group);
                break;
            case "21":
                Integer id2 = adrInt;
                String port = info.substring(0, 2);
                String substring = info.substring(2, 10);
                if(info.length()==10){
                    if("01".equals(port)){
                        //电流
                        if("02073700".equals(substring)){
                            System.out.println("电流告警阀值修改成功");
                        }else {
                            System.out.println("电流告警阀值修改失败");
                        }
                    }else if("02".equals(port)){
                        //电压
                        if("02073700".equals(substring)){
                            System.out.println("电压告警阀值修改成功");
                        }else {
                            System.out.println("电压告警阀值修改失败");
                        }
                    }
                }else {
                    if("01".equals(port)){
                        //电流
                        System.out.println("电流告警阀值修改失败");
                    }else if("02".equals(port)){
                        //电压
                        System.out.println("电压告警阀值修改失败");
                    }
                }
                break;
            case "AA5A":
                Integer id3 = adrInt;
                String type = info.substring(0, 2);
                String actualValue = info.substring(2, 6);
                String defaultValue = info.substring(6, 10);
                switch (type){
                    case "81":
                        System.out.println("低电流告警当前电流"+actualValue);
                        System.out.println("低电流告警预设电流"+defaultValue);
                        break;
                    case "82":
                        System.out.println("高电流告警当前电流"+actualValue);
                        System.out.println("高电流告警预设电流"+defaultValue);
                        break;
                    case "83":
                        System.out.println("低电压告警当前电压"+actualValue);
                        System.out.println("低电压告警预设电压"+defaultValue);
                        break;
                    case "84":
                        System.out.println("高电压告警当前电压"+actualValue);
                        System.out.println("高电压告警预设电压"+defaultValue);
                        break;
                }
                break;
            case "4A":
                Integer id4 = adrInt;
                if(info.length()==4){
                    //设置本地时间
                    String data = info.substring(0, 4);
                    if("4F6B".equals(data)){
                        System.out.println("本地时间设置成功");
                    }else{
                        System.out.println("本地时间设置失败");
                    }
                }else if(info.length()==12){
                    //读取本地时间
                    String yearHex = info.substring(0,2);
                    String year = String.valueOf(Integer.parseInt(yearHex, 16));
                    String monthHex = info.substring(2,4);
                    String month = String.valueOf(Integer.parseInt(monthHex, 16));
                    String dayHex = info.substring(4,6);
                    String day = String.valueOf(Integer.parseInt(dayHex, 16));
                    String hourHex = info.substring(6,8);
                    String hour = String.valueOf(Integer.parseInt(hourHex, 16));
                    String minuteHex = info.substring(8,10);
                    String minute = String.valueOf(Integer.parseInt(minuteHex, 16));
                    String secondHex = info.substring(10,12);
                    String second = String.valueOf(Integer.parseInt(secondHex, 16));
                    String time = "20" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    System.out.println("本地时间:"+time);
                }
                break;
            case "30":
                if(info.length()==6){
                    String data = info.substring(2,6);
                    if("4F6B".equals(data)){
                        System.out.println("设置单灯地址成功");
                    }else{
                        System.out.println("设置单灯地址失败");
                    }
                }else {
                    System.out.println("设置单灯地址失败");
                }
                break;
            case "AA55":
                String code = info.substring(0, 2);
                switch (code){
                    case "D4":
                        //设置单灯策略回包
                        if("4F6B".equals(info.substring(2, 6))){
                            System.out.println("策略设置成功");
                        }
                        break;
                    case "D3":
                        //查询单灯策略回包
                        SetStrategyParamVO setStrategyParamVO = new SetStrategyParamVO();
                        setStrategyParamVO.setStrategyNum(info.substring(2,4));
                        setStrategyParamVO.setEnable(info.substring(4,6));
                        setStrategyParamVO.setExecutionTimeOne(info.substring(6,12));
                        setStrategyParamVO.setExecutionTypeOne(info.substring(12,16));
                        setStrategyParamVO.setExecutionTimeTwo(info.substring(16,22));
                        setStrategyParamVO.setExecutionTypeTwo(info.substring(22,26));
                        System.out.println(setStrategyParamVO);
                        break;
                    case "D2":
                        //设置策略使能回包 或 查询策略使能回包
                        if("4F6B".equals(info.substring(2, 6))){
                            System.out.println("单灯策略使能成功");
                        }else {
                            String scenelal = info.substring(2, 4);
                            String sceneTime = info.substring(4, 6);
                            if("00".equals(scenelal)){
                                System.out.println("经纬度策略使能");
                            }else if("01".equals(scenelal)){
                                System.out.println("经纬度策略为使能");
                            }
                            if("00".equals(sceneTime)){
                                System.out.println("时间策略使能");
                            }else if("01".equals(sceneTime)){
                                System.out.println("时间策略为使能");
                            }
                        }
                        break;
                }
                break;
            default:
                break;
        }
        return result;
    }

    public static boolean sendNewLoraMessage(String message,String devEui){

        System.out.println(message);
        String getToken = sendNewLoraLogin();
        JSONObject tokenResult = JSON.parseObject(getToken);
        JSONObject tokenData = (JSONObject)tokenResult.get("data");
        String token = (String)tokenData.get("jwt");

        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","Bearer "+token);

        long timeNow = System.currentTimeMillis();
        byte[] bytes = HexUtil.hexStringToBytes(message);
        String data = "";
        try {
            data = SendDataUtils.signAndEncrypt(bytes, timeNow);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",data);
        //System.out.println(data);
        jsonObject.put("devEui",devEui);
        jsonObject.put("fPort",10);
        jsonObject.put("timestamp",timeNow);
        jsonObject.put("useClassA",false);
        jsonObject.put("priority",false);
        jsonObject.put("modeEnum","DEFAULT_MODE");

        //System.out.println(jsonObject.toJSONString());

        String post = HttpUtil.post(loraNewApi.getSendMessage(), jsonObject.toJSONString(), headMap);
        System.out.println(post);
        JSONObject result = JSON.parseObject(post);
        Integer code = (Integer)result.get("code");
        if(code==8000){
            return true;
        }else {
            return false;
        }

    }

    public static String createNewLoraNode(String devEui){

        String getToken = sendNewLoraLogin();
        JSONObject tokenResult = JSON.parseObject(getToken);
        JSONObject tokenData = (JSONObject)tokenResult.get("data");
        String token = (String)tokenData.get("jwt");

        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","Bearer "+token);

        String devAddr = devEui.substring(8,16);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appsKey",loraNewApi.getAppsKey());
        jsonObject.put("clazz","CLASS_C");
        jsonObject.put("devAddr",devAddr);
        jsonObject.put("devEui",devEui);
        jsonObject.put("fcntBits","FCNT_32");
        jsonObject.put("group","");
        jsonObject.put("lorawanVersion","LoRaWAN_1_0_2");
        jsonObject.put("nwksKey",loraNewApi.getNwksKey());
        jsonObject.put("projectId",loraNewApi.getProjectId());

        String post = HttpUtil.post(loraNewApi.getCreateNode(), jsonObject.toJSONString(), headMap);
        System.out.println(post);
        JSONObject result = JSON.parseObject(post);
        Integer code = (Integer)result.get("code");
        if(code==8000){
            JSONObject data = (JSONObject) result.get("data");
            String id = (String) data.get("id");
            return id;
        }else {
            return "";
        }
    }

    /*public static String createNodeMcNew(String nodeId){
        String getToken = sendNewLoraLogin();
        JSONObject tokenResult = JSON.parseObject(getToken);
        JSONObject tokenData = (JSONObject)tokenResult.get("data");
        String token = (String)tokenData.get("jwt");

        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","Bearer "+token);

        List<String> deviceIds = new ArrayList<>();
        deviceIds.add(nodeId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceIds",deviceIds);
        jsonObject.put("groupId","5f363d457979c40001fc3f41");

        System.out.println(jsonObject.toJSONString());

        String post = HttpUtil.post("http://47.110.127.110:8080/api/v1/lorawan/groupDevices", jsonObject.toJSONString(), headMap);
        System.out.println(post);
        JSONObject result = JSON.parseObject(post);
        Integer code = (Integer)result.get("code");
        if(code==8000){
            JSONArray data = result.getJSONArray("data");
            String id = (String)data.getJSONObject(0).get("id");
            return id;
        }else {
            return "";
        }
    }*/


    public static boolean deleteNewLoraNode(List<String> ids){

        String getToken = sendNewLoraLogin();
        System.out.println(getToken);
        JSONObject tokenResult = JSON.parseObject(getToken);
        JSONObject tokenData = (JSONObject)tokenResult.get("data");
        String token = (String)tokenData.get("jwt");

        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","Bearer "+token);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ids",ids);
        System.out.println(jsonObject.toJSONString());

        String post = HttpUtil.delete("http://47.110.127.110:8080/api/v1/lorawan/devices/batchDelete", jsonObject.toJSONString(), headMap);
        System.out.println(post);
        JSONObject result = JSON.parseObject(post);
        Integer code = (Integer)result.get("code");
        if(code==8000){
            return true;
        }else {
            return false;
        }
    }


    /*public static boolean deleteNodeMcNew(List<String> ids){

        String getToken = sendNewLoraLogin();
        JSONObject tokenResult = JSON.parseObject(getToken);
        JSONObject tokenData = (JSONObject)tokenResult.get("data");
        String token = (String)tokenData.get("jwt");

        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        headMap.put("Authorization","Bearer "+token);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ids",ids);
        System.out.println(jsonObject.toJSONString());

        String post = HttpUtil.delete("http://47.110.127.110:8080/api/v1/lorawan/groupDevices/batchDelete", jsonObject.toJSONString(), headMap);
        System.out.println(post);
        *//*JSONObject result = JSON.parseObject(post);
        Integer code = (Integer)result.get("code");
        if(code==8000){
            return true;
        }else {
            return false;
        }*//*
        return false;
    }*/

    public static String sendNewLoraLogin(){
        Map<String,String> headMap = new HashMap<>();
        headMap.put("Content-Type","application/json");
        JSONObject jsonObject = new JSONObject();
        /*jsonObject.put("username",loraNewApi.getUsername());
        jsonObject.put("password",loraNewApi.getPassword());
        String post = HttpUtil.post(loraNewApi.getLogin(), jsonObject.toJSONString(),headMap);*/
        jsonObject.put("username","dvk018");
        jsonObject.put("password","888888");
        String post = HttpUtil.post("http://47.110.127.110:8080/api/v1/auth/login", jsonObject.toJSONString(),headMap);
        return post;
    }

    public static String numToDHS(double d, Integer flag) {
        // 得到小数点前数字即
        double dd = Math.floor(d);

        double hh = Math.floor((d - dd) * 60);
        // 可能存在误差
        double ss = ((d - dd) - (hh / 60.0)) * 3600;

        BigDecimal bB = new BigDecimal(ss);
        ss = bB.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 取整数
        String ddstr = String.valueOf((int) dd);
        String hhstr = String.valueOf((int) hh);
        String ssstr = String.valueOf((int) ss);

        if(flag==2){
            if (ddstr.length() == 1) {
                ddstr = "0" + ddstr;
            }
        }else if(flag==1){
            if (ddstr.length() == 1) {
                ddstr = "00" + ddstr;
            } else if (ddstr.length() == 2) {
                ddstr = "0" + ddstr;
            }
        }
        if (hhstr.length() == 1) {
            hhstr = "0" + hhstr;
        }
        if (ssstr.length() == 1) {
            ssstr = "0" + ssstr;
        }
        return ddstr + " " + hhstr + " " + ssstr;
    }

    /**
     * 定时发送策略消息
     * @param DateString
     * @param strategyTempVOS
     */
    public static void sendTimingStrategyMessage(String DateString, List<StrategyTempVO> strategyTempVOS){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        try {
            Date date = dateFormat.parse(DateString);
            Timer timer = new Timer(DateString);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    log.info("定时发送策略消息:{}",strategyTempVOS);
                    //发送消息
                    for (StrategyTempVO strategyTempVO : strategyTempVOS) {
                        Integer deviceTypeId = strategyTempVO.getDeviceTypeId();
                        String message = strategyTempVO.getMessage();
                        String sendId = strategyTempVO.getSendId();
                        sendByMode(message,deviceTypeId,sendId);
                        System.out.println("定时任务发送消息："+message);
                    }
                    Date date1 = new Date();
                    if(date1.getTime()+1000>date.getTime()){
                        timer.cancel();
                    }
                }
            }, date,200);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 定时重发策略消息
     * @param DateString
     * @param messageList
     * @param deviceTypeId
     * @param sendId
     * @param deviceIds
     * @param strategyId
     */
    public static void sendTimingStrategyMessageAgain(String DateString, List<String> messageList,Integer deviceTypeId,String sendId,List<Integer> deviceIds,Integer strategyId){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        try {
            Date date = dateFormat.parse(DateString);
            Timer timer = new Timer(DateString);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    log.info("定时重发策略消息:{},{}",deviceIds,strategyId);
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.SECOND,Integer.parseInt(ctwingApi.getRetransmissionTime()));
                    for (int i = 0; i < deviceIds.size(); i++) {
                        Integer deviceId = deviceIds.get(i);
                        //判断当前设备策略历史消息状态
                        DeviceStrategyHistory deviceStrategyHistory = deviceStrategyHistoryService.selectNewOne(deviceId,strategyId);
                        if(deviceStrategyHistory!=null){
                            Integer isSuccess = deviceStrategyHistory.getIsSuccess();
                            if(isSuccess==3){

                            }else if(isSuccess==0){
                                //重发消息
                                for (String message : messageList) {
                                    sendByMode(message,deviceTypeId,sendId);
                                    System.out.println("定时任务发送消息："+message);
                                }
                                deviceStrategyHistory.setIsSuccess(1);
                                deviceStrategyHistoryService.updateById(deviceStrategyHistory);
                                List<Integer> deviceIdList = new ArrayList<>();
                                deviceIdList.add(deviceId);
                                //定时判定
                                sendTimingStrategyMessageAgain(dateFormat.format(calendar.getTime()),messageList,deviceTypeId,sendId,deviceIdList,strategyId);
                            }else if(isSuccess==1){
                                //发送失败
                                deviceStrategyHistory.setIsSuccess(2);
                                deviceStrategyHistoryService.updateById(deviceStrategyHistory);
                            }
                        }
                    }
                    Date date1 = new Date();
                    if(date1.getTime()+1000>date.getTime()){
                        timer.cancel();
                    }
                }
            }, date,200);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
