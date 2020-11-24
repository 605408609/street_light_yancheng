package com.exc.street.light.occ.schedulers;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.exc.street.light.occ.alarm.ReceiveAlarm;
import com.exc.street.light.occ.alarm.ReceiveAlarmCallable;
import com.exc.street.light.occ.config.AlarmConfig;
import com.exc.street.light.occ.config.OccConfigs;
import com.exc.street.light.occ.mapper.AhDeviceDao;
import com.exc.street.light.occ.po.KafkaMessage;
import com.exc.street.light.occ.service.AhDeviceService;
import com.exc.street.light.resource.dto.AhDeviceDTO;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.entity.occ.AhPlay;
import com.exc.street.light.resource.utils.ConstantUtil;
import com.exc.street.light.resource.utils.HttpClientUtil;
import com.exc.street.light.resource.utils.JacksonUtil;
import com.exc.street.light.resource.utils.TestingOnlineUtil;
import com.exc.street.light.resource.vo.OccAhPlayVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 *
 * @author huangmin
 */
@Component
public class SchedulerTask {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerTask.class);
    @Autowired
    private AlarmConfig serviceConfiguration;
    @Autowired
    private AhDeviceDao ahDeviceDao;
    @Autowired
    private AhDeviceService ahDeviceService;
    @Autowired
    private OccConfigs occConfigs;

    //项目启动时,延时10s 执行update注册设备状态,解决启动时错误
    private static long delayedTime = 10000;

    //设置线程大小为10
    private static ExecutorService pool = Executors.newFixedThreadPool(50);

    private static String getLampIdUrl = null;
    private static String addAhPlayUrl = null;
    private static String sendMsgToKafkaUrl = null;
    private static String updateStateByIdUrl = null;


    
    
    @PostConstruct
    protected void createListener() {
        getLampIdUrl = occConfigs.getHttpType()+"://"+occConfigs.getHttpIp()+":"+occConfigs.getHttpPort()+occConfigs.getControllerRequestGetLampId();
        addAhPlayUrl = occConfigs.getHttpType()+"://"+occConfigs.getHttpIp()+":"+occConfigs.getHttpPort()+occConfigs.getControllerRequestAddAhPlay();

        sendMsgToKafkaUrl = occConfigs.getHttpType()+"://"+occConfigs.getHttpIp()+":"+occConfigs.getHttpPort()+occConfigs.getControllerRequestSendMsgToKafka();
        updateStateByIdUrl = occConfigs.getHttpType()+"://"+occConfigs.getHttpIp()+":"+occConfigs.getHttpPort()+occConfigs.getControllerRequestUpdateStateById();
        logger.info("获取灯杆ID接口地址是:"+getLampIdUrl);
        logger.info("新增告警信息的接口地址是:"+addAhPlayUrl);
        logger.info("获取灯杆ID接口地址是:"+sendMsgToKafkaUrl);
        logger.info("新增告警信息的接口地址是:"+updateStateByIdUrl);
        
        List<AhDevice> listAhDevice = ahDeviceDao.selectList(null);

        logger.info("-------------------- 项目启动时,初始化所有设备 为注册成功 -------------------- ");
        for (AhDevice ahDevice : listAhDevice) {
            ahDevice.setRegistrationSuccess(ConstantUtil.REGISTRATION_FAIL);
        }
        ReceiveAlarm receiveAlarm = null;
        if (listAhDevice.size() > 0) {
            ahDeviceService.updateBatchById(listAhDevice);
            for (AhDevice ahDevice : listAhDevice) {
                AhDeviceDTO deviceVO = getDeviceVO(ahDevice.getIp());
                receiveAlarm = new ReceiveAlarm(deviceVO,ahDevice);
                ReceiveAlarmCallable cvt = new ReceiveAlarmCallable(receiveAlarm);
                pool.submit(cvt);

            }
        }
        logger.info("数据库报警设备表数据为空");

        logger.info("-------------------- 开启监听设备报警信息 -------------------- ");
        if (receiveAlarm != null){
            receiveAlarm.StartAlarmListen();
        }


    }
    /**
     * 定时注册"未注册成功的设备"
     */
//    @Async
    @Scheduled(cron = "#{scheduleCheckRegister}")
    protected void doScheduleCheckRegister() {
        List<AhDevice> listAhDevice = ahDeviceDao.selectList(null);
        logger.info("-------------------- 定时查询已添加的设备,对未注册的设备进行注册 -------------------- ");
        logger.info("数据库记录的报警盒数量: "+listAhDevice.size());

        List<AhDevice> listFailAhDevice = listAhDevice.stream().filter(ahDevice -> ahDevice.getRegistrationSuccess() == ConstantUtil.REGISTRATION_FAIL).collect(Collectors.toList());

        if (listFailAhDevice.size() > 0 ) {
            for (AhDevice ahDevice : listFailAhDevice) {
                AhDeviceDTO deviceVO = getDeviceVO(ahDevice.getIp());
                ReceiveAlarm receiveAlarm = new ReceiveAlarm(deviceVO,ahDevice);
                ReceiveAlarmCallable cvt = new ReceiveAlarmCallable(receiveAlarm);
                pool.submit(cvt);
            }
        }
    }
    /**
     * 定时检测设备是否在线
     */
//    @Async
    @Scheduled(cron = "#{scheduleCheckOnline}")
    protected void doScheduleCheckOnline() {
        logger.info("-------------------- 定时检测设备是否在线 -------------------- ");
        List<AhDevice> listAhDevice = ahDeviceDao.selectList(null);

        for (AhDevice ahDevice : listAhDevice) {
            if (TestingOnlineUtil.getOnline(ahDevice.getIp())) {
                ahDevice.setNetworkState(ConstantUtil.DEVICE_ONLINE);
            }else {
                ahDevice.setNetworkState(ConstantUtil.DEVICE_OFFLINE);
            }
        }
        List<AhDevice> listOnlineAhDevice = listAhDevice.stream().filter(ahDevice -> ahDevice.getNetworkState() == ConstantUtil.DEVICE_ONLINE).collect(Collectors.toList());
        logger.info("已在线的报警盒数量: "+listOnlineAhDevice.size());
        if (listAhDevice.size() > 0) {
            ahDeviceService.updateBatchById(listAhDevice);
            return;
        }
        logger.info("数据库报警设备表数据为空");

    }
	
	/**
	 * 根据设备编号获取灯杆ID
	 * @param ap1	已赋值设备编号的对象
	 * @return	灯杆ID
	 */
	public static OccAhPlayVO getLampId(AhPlay ap1) {

		String json = JacksonUtil.toJsonString(ap1);

		logger.info("根据设备编号获取灯杆ID是："+json);
		String returnJson = HttpClientUtil.sendHttpPostJson(getLampIdUrl, json);
		logger.info("获取灯杆的信息是："+returnJson);
		if (returnJson.equals("")) {
			logger.info("报警盒没有挂载到灯杆上，请先挂载报警盒，报警盒编号是："+json);
		}
		OccAhPlayVO ap = JacksonUtil.json2Object(returnJson, OccAhPlayVO.class);
		return ap;
	}
    
	/**
	 * 新增告警数据
	 * @param ap1	已赋值设备编号的对象
	 * @return	灯杆ID
	 */
	public static void insertAhPlay(AhPlay ap1) {
		String json = JacksonUtil.toJsonString(ap1);
		HttpClientUtil.sendHttpPostJson(addAhPlayUrl, json);
	}

    /**
     * 修改设备注册状态
     * 
     * @param registrationSuccess    告警状态
     * @param ahDevice 设备序列号
     * @return
     */
    public static void update(Integer registrationSuccess, AhDevice ahDevice) {
        try {
            Thread.sleep(delayedTime);
        }catch (Exception e){
            System.out.println("SchedulerTask.update线程睡眠发生错误信息:"+e);
        }
        ahDevice.setRegistrationSuccess(registrationSuccess);
        String json = JacksonUtil.toJsonString(ahDevice);
        logger.info("post请求地址:"+updateStateByIdUrl+",请求参数"+json);
        HttpClientUtil.sendHttpPostJson(updateStateByIdUrl, json);
    }

    /**
     * 发送信息到消费者
     * @param status    状态
     * @param msg
     */
    public static void sendMessage(Integer status, String msg) {
        KafkaMessage kafkaMessage=new KafkaMessage();
        kafkaMessage.setType(1);
        kafkaMessage.setStatus(status);
        kafkaMessage.setMessage(msg);
        kafkaMessage.setIs2All(2);
        kafkaMessage.setUserIds(null);
        String json = JacksonUtil.toJsonString(kafkaMessage);
        HttpClientUtil.sendHttpPostJson(sendMsgToKafkaUrl, json);
    }
    /**
     * 获取VO对象
     * @param deviceIP  设备ip地址
     * @return
     */
    public AhDeviceDTO getDeviceVO(String deviceIP) {
        AhDeviceDTO deviceVO = new AhDeviceDTO();
        deviceVO.setDeviceIP(deviceIP);
        deviceVO.setDeviceUserName(serviceConfiguration.getDeviceUsername());
        deviceVO.setDevicePassword(serviceConfiguration.getDevicePwd());
        deviceVO.setDevicePort(serviceConfiguration.getDevicePort());
        deviceVO.setListenIP(serviceConfiguration.getListenIp());
        deviceVO.setListenPort(serviceConfiguration.getListenPort());
        return deviceVO;
    }
    
}