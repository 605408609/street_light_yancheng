package com.exc.street.light.resource.utils;

/**
 * 常量工具类
 *
 * @author Longshuangyang
 * @date 2020/03/28
 */
public class BaseConstantUtil {
    /**
     * 编码类型
     */
    public static final String CHARSET = "UTF-8";

    /**
     * 线程最大个数
     */
    public static final int THREADPOOLSIZE_2 = 50;

    /**
     * 设备类型：智慧照明(灯具) sl
     */
    public static final int DEVICE_TYPE_SL = 1;

    /**
     * 设备类型：公共WIFI      wifi
     */
    public static final int DEVICE_TYPE_WIFI = 2;

    /**
     * 设备类型：公共广播     pb
     */
    public static final int DEVICE_TYPE_PB = 3;

    /**
     * 设备类型：智能安防(摄像头)   ss
     */
    public static final int DEVICE_TYPE_SS = 4;

    /**
     * 设备类型：信息发布(显示屏)   ir
     */
    public static final int DEVICE_TYPE_IR = 5;

    /**
     * 设备类型：一键呼叫(远程呼叫)   occ
     */
    public static final int DEVICE_TYPE_OCC = 6;

    /**
     * 设备类型：环境监测(气象)   em
     */
    public static final int DEVICE_TYPE_EM = 7;

    /**
     * 设备类型：设备位置管理   dlm
     */
    public static final int DEVICE_TYPE_DLM= 8;

    /**
     * 设备类型：工单告警   WOA
     */
    public static final int DEVICE_TYPE_WOA = 9;

    /**
     * 设备类型：用户权限（认证中心）   UA
     */
    public static final int DEVICE_TYPE_UA = 10;

    /**
     * 设备类型：日志统计   ls
     */
    public static final int DEVICE_TYPE_LS = 11;

    /**
     * 设备类型：路灯网关   gw
     */
    public static final int DEVICE_TYPE_GW = 12;

    /**
     * 延时时间:分钟(延时时间到恢复默认状态:常亮) 65535:一直保持控制动作
     */
    public static final int MANCTRL_DELAY = 65535;

    /**
     * 路灯类型
     */
    public static final String LIGHT_DEVICE_TYPE = "yameida_lampcontrol";

    /**
     * 单灯控制命令
     */
    public static final String SINGLE_CMD = "SetLampLightness";

    /**
     * 告警状态，1：未处理
     */
    public static final Integer ALARM_STATUS_UNTREATED = 1;

    /**
     * 告警状态，2：处理中
     */
    public static final Integer ALARM_STATUS_IN_PROCESSING = 2;

    /**
     * 告警状态，3：已处理
     */
    public static final Integer ALARM_STATUS__PROCESSING = 3;

    /**
     * 工单状态，1：待初审
     */
    public static final Integer ORDER_STATUS_PENDING_TRIAL = 1;

    /**
     * 工单状态，2：被驳回
     */
    public static final Integer ORDER_STATUS_REJECT = 2;

    /**
     * 工单状态，3：待处理
     */
    public static final Integer ORDER_STATUS_UNTREATED = 3;

    /**
     * 工单状态，4：跟进中
     */
    public static final Integer ORDER_STATUS_IN_PROCESSING = 4;

    /**
     * 工单状态，5：待审核
     */
    public static final Integer ORDER_STATUS_TO_BE_AUDITED = 5;

    /**
     * 工单状态，6：审核通过
     */
    public static final Integer ORDER_STATUS_PASS = 6;

    /**
     * 工单状态，7：已超时
     */
    public static final Integer ORDER_STATUS_TIMEOUT = 7;

    /**
     * 告警类型，1：灯控器离线
     */
    public static final Integer ALARM_TYPE_SL_OFFLINE = 1;

    /**
     * 告警类型，2：网关离线
     */
    public static final Integer ALARM_TYPE_GATEWAY_OFFLINE = 2;

    /**
     * 告警类型，3：灯控器电流异常
     */
    public static final Integer ALARM_TYPE_SL_CURRENT_ABNORMAL = 3;

    /**
     * 告警类型，4：灯控器电压异常
     */
    public static final Integer ALARM_TYPE_SL_VOLTAGE_ABNORMAL = 4;

    /**
     * 告警类型，5：灯控器温度异常
     */
    public static final Integer ALARM_TYPE_SL_TEMPERATURE_ABNORMAL = 5;

    /**
     * 告警类型，6：摄像头离线
     */
    public static final Integer ALARM_TYPE_SS_OFFLINE = 6;

    /**
     * 告警类型，7：显示屏离线
     */
    public static final Integer ALARM_TYPE_IR_OFFLINE = 7;

    /**
     * 告警类型，8：传感器离线
     */
    public static final Integer ALARM_TYPE_EM_OFFLINE = 8;

    /**
     * 告警类型，9：广播离线
     */
    public static final Integer ALARM_TYPE_PB_OFFLINE = 9;

    /**
     * 告警类型，10：其他
     */
    public static final Integer ALARM_TYPE_OTHER = 10;

}
