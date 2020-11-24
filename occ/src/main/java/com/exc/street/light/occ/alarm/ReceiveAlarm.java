package com.exc.street.light.occ.alarm;
import com.exc.street.light.occ.schedulers.SchedulerTask;
import com.exc.street.light.resource.dto.AhDeviceDTO;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.entity.occ.AhPlay;
import com.exc.street.light.resource.utils.ConstantUtil;
import com.exc.street.light.resource.utils.WebsocketUtil;
import com.exc.street.light.resource.vo.OccAhPlayVO;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 接收报警信息
 * @author huangmin
 * @date 2020/03/21
 */
public class ReceiveAlarm {
	private static final Logger logger = LoggerFactory.getLogger(ReceiveAlarm.class);
    HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
    HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();// 设备登录信息
    HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();// 设备信息
    private String deviceIP;// 已登录设备的IP地址
    private int devicePort;// 已登录设备的端口
    private String deviceUserName;// 设备用户名
    private String devicePassword;// 设备密码
    private String listenIP; // 监听服务器IP地址
    private int listenPort; // 监听服务器端口号

    private int lUserID;// 用户句柄
    private int lAlarmHandle;// 报警布防句柄
    private int lListenHandle;// 报警监听句柄

    public FMSGCallBack fMSFCallBack;// 报警回调函数实现
    public FMSGCallBack_V31 fMSFCallBack_V31;// 报警回调函数实现

    public FGPSDataCallback fGpsCallBack;// GPS信息查询回调函数实现
    
    private AhDevice ahDevice = null;
    //布防成功
    private boolean listenSussess = false;
    

    public ReceiveAlarm(AhDeviceDTO deviceVO,AhDevice ahDevice) {
        deviceIP = deviceVO.getDeviceIP();
        deviceUserName = deviceVO.getDeviceUserName();
        devicePassword = deviceVO.getDevicePassword();
        devicePort = deviceVO.getDevicePort();
        listenIP = deviceVO.getListenIP();
        listenPort = deviceVO.getListenPort();
        this.ahDevice = ahDevice;


        lUserID = -1;
        lAlarmHandle = -1;
        lListenHandle = -1;
        fMSFCallBack = null;
        fMSFCallBack_V31 = null;
        fGpsCallBack = null;

        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc != true) {
            logger.error("初始化失败");
        }

        HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG struGeneralCfg = new HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG();

        if (!hCNetSDK.NET_DVR_SetSDKLocalCfg(17, struGeneralCfg.getPointer())) {
            logger.error("NET_DVR_SetSDKLocalCfg失败");
        }
    }
    
    public void jButtonLoginActionPerformedNew() {
        // 注册之前先注销已注册的用户,预览情况下不可注销
        if (lUserID > -1) {
            // 先注销
            hCNetSDK.NET_DVR_Logout(lUserID);
            lUserID = -1;
        }

        // 注册
        m_strLoginInfo.sDeviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
        System.arraycopy(deviceIP.getBytes(), 0, m_strLoginInfo.sDeviceAddress, 0, deviceIP.length());

        m_strLoginInfo.sUserName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
        System.arraycopy(deviceUserName.getBytes(), 0, m_strLoginInfo.sUserName, 0, deviceUserName.length());

        m_strLoginInfo.sPassword = new byte[HCNetSDK.NET_DVR_LOGIN_PASSWD_MAX_LEN];
        System.arraycopy(devicePassword.getBytes(), 0, m_strLoginInfo.sPassword, 0, devicePassword.length());

        m_strLoginInfo.wPort = (short)devicePort;

        m_strLoginInfo.bUseAsynLogin = false; // 是否异步登录：0- 否，1- 是

        m_strLoginInfo.write();
        lUserID = hCNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);

        if (lUserID == -1) {
            logger.error("设备注册失败，设备名称是："+ahDevice.getName()+",设备编号是："+ahDevice.getNum()+",设备ip是："+ahDevice.getIp()+",设备用户名是："+deviceUserName+",设备密码是："+devicePassword+",请确认该设备IP或密码是否正确！！！"+"错误号:" + hCNetSDK.NET_DVR_GetLastError());
        } else {
            logger.info("设备注册成功，设备名称是："+ahDevice.getName()+",设备编号是："+ahDevice.getNum());
        }
    }

    public boolean SetupAlarmChan() {
//        桌面窗口会出现不点击注册按钮，直接点击布防的情况，但是程序自动执行的时候不会出现该情况
//        if (lUserID == -1) {
//            logger.error("设备布防失败，设备名称是："+ahDevice.getName()+",请先注册设备");
//        }
        if (lAlarmHandle < 0)// 尚未布防,需要布防
        {
            if (fMSFCallBack_V31 == null) {
                fMSFCallBack_V31 = new FMSGCallBack_V31();
                Pointer pUser = null;
                if (!hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fMSFCallBack_V31, pUser)) {
                    logger.error("设置回调函数失败!");
                }
            }
            HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
            m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
            m_strAlarmInfo.byLevel = 1;// 智能交通布防优先级：0- 一等级（高），1- 二等级（中），2- 三等级（低）
            m_strAlarmInfo.byAlarmInfoType = 1;// 智能交通报警信息上传类型：0- 老报警信息（NET_DVR_PLATE_RESULT），1-
                                               // 新报警信息(NET_ITS_PLATE_RESULT)
            m_strAlarmInfo.byDeployType = 1; // 布防类型(仅针对门禁主机、人证设备)：0-客户端布防(会断网续传)，1-实时布防(只上传实时数据)
            m_strAlarmInfo.write();
            lAlarmHandle = hCNetSDK.NET_DVR_SetupAlarmChan_V41(lUserID, m_strAlarmInfo);
            if (lAlarmHandle == -1) {
                // JOptionPane.showMessageDialog(null, "布防失败，错误号:" + hCNetSDK.NET_DVR_GetLastError());
            	this.CloseAlarmChan();
                logger.error("设备布防失败，设备名称是："+ahDevice.getName()+",设备编号是："+ahDevice.getNum()+",设备ip是："+ahDevice.getIp()+",设备用户名是："+deviceUserName+",设备密码是："+devicePassword+",请确认该设备IP或密码是否正确！！！"+"错误号:" + hCNetSDK.NET_DVR_GetLastError());
                SchedulerTask.update(ConstantUtil.REGISTRATION_FAIL, ahDevice);
            } else {
                logger.info("设备布防成功，设备名称是："+ahDevice.getName()+",设备编号是："+ahDevice.getNum());
				 SchedulerTask.update(ConstantUtil.REGISTRATION_SUCCESS, ahDevice); 
                listenSussess = true;
            	return listenSussess;
                // JOptionPane.showMessageDialog(null, "布防成功");
            }
        }
		return listenSussess;
    }

    public void CloseAlarmChan() {
        // 报警撤防
        if (lAlarmHandle > -1) {
            if (hCNetSDK.NET_DVR_CloseAlarmChan_V30(lAlarmHandle)) {
                logger.info("撤防成功");
                lAlarmHandle = -1;
            }
        }
    }

    public void StartAlarmListen() {
        Pointer pUser = null;

        if (fMSFCallBack == null) {
            fMSFCallBack = new FMSGCallBack();
        }
        lListenHandle = -1;
        lListenHandle = hCNetSDK.NET_DVR_StartListen_V30(listenIP, (short)listenPort, fMSFCallBack, pUser);
        if (lListenHandle < 0) {
            logger.error("启动监听失败,监听ip："+listenIP+"监听端口："+listenPort);
        } else {
            logger.info("启动监听成功,监听ip："+listenIP+"监听端口："+listenPort);
        }
    }

    public void StopAlarmListen() {
        if (lListenHandle < 0) {
            return;
        }

        if (!hCNetSDK.NET_DVR_StopListen_V30(lListenHandle)) {
            logger.error("停止监听失败");
        } else {
            logger.info("停止监听成功");
        }
    }

    public void Logout() {
        logger.info("退出登录");
        // 报警撤防
        if (lAlarmHandle > -1) {
            if (!hCNetSDK.NET_DVR_CloseAlarmChan_V30(lAlarmHandle)) {
                logger.error("撤防失败");
            } else {
                lAlarmHandle = -1;
            }
        }

        // 注销
        if (lUserID > -1) {
            if (hCNetSDK.NET_DVR_Logout(lUserID)) {
                logger.error("注销失败");

                lUserID = -1;
            }
        }
    }

    public void AlarmDataHandle(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
        Pointer pUser) {
        String deviceNum = null ;
        try {

//            String sAlarmType = new String("lCommand=0x") + Integer.toHexString(lCommand);
             byte[] sSerialNumberByte = pAlarmer.sSerialNumber;
             deviceNum = new String(sSerialNumberByte);
            // lCommand是传的报警类型
             AhPlay ap = new AhPlay();
            int a = 1/10 ;
            //一键呼叫紧急报警，通知前端故障告警的灯杆ID，前端根据灯杆ID，调用后台接口查询该灯杆的默认弹窗摄像头的监控点编号
            switch (lCommand) {

                case HCNetSDK.COMM_ALARMHOST_CID_ALARM:
//                    System.out.println(",该款设备是报警主机");

                    HCNetSDK.NET_DVR_CID_ALARM netDvrCidAlarm = new HCNetSDK.NET_DVR_CID_ALARM();
                    netDvrCidAlarm.write();
                    Pointer pNetDvrCidAlarm = netDvrCidAlarm.getPointer();
                    pNetDvrCidAlarm.write(0, pAlarmInfo.getByteArray(0, netDvrCidAlarm.size()), 0,
                        netDvrCidAlarm.size());
                    netDvrCidAlarm.read();

                    Integer sCIDCode = Integer.parseInt(new String(netDvrCidAlarm.sCIDCode));
//                    logger.info("deviceNum: "+deviceNum);
                    ap.setDeviceNum(deviceNum.trim());
                    
                    
                    OccAhPlayVO occAhPlayVO  = SchedulerTask.getLampId(ap);//灯杆ID
                    
                    BeanUtils.copyProperties(occAhPlayVO,ap);
//                    logger.info("lampId: "+occAhPlayVO.getLampId());
                    ap.setLampId(occAhPlayVO.getLampId());
                    ap.setCreateTime(new Date());
                    ap.setDeviceNum(deviceNum.trim());
                    
                    switch (sCIDCode) {
                        case 1129:
                        	logger.info("设备["+deviceNum.trim()+"]紧急求助报警");
                        	ap.setContent(occAhPlayVO.getAreaName()+occAhPlayVO.getStreetName()+occAhPlayVO.getSiteName()+occAhPlayVO.getLampPostName()+"紧急求助报警");
							ap.setStatus(ConstantUtil.STATUS_START);
							SchedulerTask.insertAhPlay(ap);
							SchedulerTask.sendMessage(0, WebsocketUtil.getJsonString(ap, ConstantUtil.OCC_TYPE));
                            break;
                        case 3129:
                        	logger.info("设备["+deviceNum.trim()+"]紧急求助报警恢复");
                        	ap.setContent(occAhPlayVO.getAreaName()+occAhPlayVO.getStreetName()+occAhPlayVO.getSiteName()+occAhPlayVO.getLampPostName()+"紧急求助报警恢复");
							ap.setStatus(ConstantUtil.STATUS_RESTORE);
							SchedulerTask.insertAhPlay(ap);
							SchedulerTask.sendMessage(1, WebsocketUtil.getJsonString(ap, ConstantUtil.OCC_TYPE));
                            break;
                        default:
                        	logger.error("未获取到设备["+deviceNum.trim()+"]状态, 出现未知错误!");
                            break;
                    }

                default:
                    break;
            }
        } catch (Exception ex) {
        	logger.error("处理报警盒"+deviceNum.trim()+"报警信息时,发生异常,异常信息是: " + ex.toString());
        }
    }

    public class FMSGCallBack_V31 implements HCNetSDK.FMSGCallBack_V31 {
        // 报警信息回调函数

        public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
            Pointer pUser) {
            AlarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
            return true;
        }
    }

    public class FMSGCallBack implements HCNetSDK.FMSGCallBack {

        // 报警信息回调函数

        public void invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen,
            Pointer pUser) {
            AlarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
        }
    }

    public class FGPSDataCallback implements HCNetSDK.fGPSDataCallback {
        public void invoke(int nHandle, int dwState, Pointer lpBuffer, int dwBufLen, Pointer pUser) {}
    }
}
