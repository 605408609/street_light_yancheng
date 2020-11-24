package com.exc.street.light.resource.utils;

/**
 * @author EXC
 */
public class ConstantUtil {
	/**
	 * 紧急求助报警,状态（0：紧急求助报警，1：紧急求助报警恢复）默认0
	 */
	public static final int STATUS_START = 0;
	
	/**
	 * 紧急求助报警恢复,状态（0：紧急求助报警，1：紧急求助报警恢复）默认0
	 */
	public static final int STATUS_RESTORE = 1;
	
	/**
	 * 监听服务是否注册设备成功,0 未注册或注册失败 , 1 注册成功
	 */
	public static final int REGISTRATION_SUCCESS = 1;
	/**
	 * 监听服务是否注册设备成功,0 未注册或注册失败 , 1 注册成功
	 */
	public static final int REGISTRATION_FAIL = 0;
	
	   /**
     * 测试PING的超时时长，毫秒
     */
    public static final int TIMEOUT_PING = 2000;
    
    /**
     * 设备在线, 网络状态(0:离线，1:在线)默认0
     */
    public static final int DEVICE_ONLINE = 1;
    /**
     * 设备已离线,网络状态(0:离线，1:在线)默认0
     */
    public static final int DEVICE_OFFLINE = 0;
    
    /**
     * occ服务的websocket 类型type
     */
    public static final int OCC_TYPE = 14;
    
    /**
     * woa服务的 服务Id(值为1:故障告警，值为2：一键呼叫)
     */
    public static final int SERVER_ID_ALARM = 1;
    /**
     * woa服务的 服务Id(值为1:故障告警，值为2：一键呼叫)
     */
    public static final int SERVER_ID_OCC = 2;
    
    
}
