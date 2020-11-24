package com.exc.street.light.resource.utils;

/**
 * 常量工具类
 *
 * @author Linshiwen
 * @date 2018/5/22
 */
public class ElectricityConstantUtil {

    /**
     * 编码类型
     */
    public static final String CHARSET = "UTF-8";

    /**
     * 变化数据集合在redis的key值
     */
    public static final String CHANGE_DATA_KEY = "owner:changeData";

    /**
     * 变化数据集合在redis的key值
     */
    public static final String MAC_CHANGE_DATA_KEY = "owner:macChangeData";

    /**
     * 心跳包集合在redis的key值
     */
    public static final String HEART_BEAT_KEY = "owner:power:heartbeat";

    /**
     * 验证码时效:3分钟
     */
    public static final int VERIFYTIME = 1;

    /**
     * 超级管理员
     */
    public static final int ROLE_GRADE_1 = 1;
    /**
     * 总控管理员
     */
    public static final int ROLE_GRADE_2 = 2;
    /**
     * 分区总管理员
     */
    public static final int ROLE_GRADE_3 = 3;
    /**
     * 分区管理员
     */
    public static final int ROLE_GRADE_4 = 4;
    /**
     * 技术支持
     */
    public static final int ROLE_GRADE_5 = 5;

    /**
     * socket最大连接次数
     */
    public static final int MAX_CONNCET = 2;

    /**
     * 按时执行
     */
    public static final int TIMING_TYPE_1 = 1;

    /**
     * 周期执行
     */
    public static final int TIMING_TYPE_2 = 2;

    /**
     * 立即执行
     */
    public static final int TIMING_TYPE_3 = 3;

    /**
     * 一直执行
     */
    public static final int TIMING_TYPE_4 = 4;

    /**
     * 日出之前
     */
    public static final int TIMING_TYPE_5 = 5;
    /**
     * 日出之后
     */
    public static final int TIMING_TYPE_6 = 6;

    /**
     * 日落之前
     */
    public static final int TIMING_TYPE_7 = 7;

    /**
     * 日落之后
     */
    public static final int TIMING_TYPE_8 = 8;



    public static final int THREADPOOLSIZE_2 = 50;

    /**
     * 合广:16  自研发:16
     * 按时执行最大数量
     */
    public static final int TIMING_TYPE_NUM_1 = 16;

    /**
     * 合广:48   自研:16
     * 周期执行最大数量
     */
    public static final int TIMING_TYPE_NUM_2 = 16;
    /**
     * socket连接超时时长
     */
    public static final int CONNECT_TIMEOUT = 3000;
    /**
     * socket读取超时时长
     */
    public static final int SO_TIMEOUT = 4000;

    /**
     * 搜索设备读取超时时长
     */
    public static final int SO_TIMEOUT_SEARCH = 20000;


    public static final Integer[] SCENE_IDS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};

    /**
     * 帧起始符   0xEF 0xEF为自研    后面的为合广
     */
    public static final byte[] START_OF_INFORMATION = {(byte) 0xEF, (byte) 0xEF};
//    public static final byte[] START_OF_INFORMATION = {0x7E, 0x7E, 0x7E, 0x7E, 0x7E};

    /**
     * 帧结束符 自研和合广都为0x0D
     */
    public static final byte END_OF_INFORMATION = 0x0D;

    /**
     * 协议帧基本开销
     *
     * @modification date 2020/3/10
     * 17为合广
     * 14为自研
     */
//    public static final int BASE_LENGTH = 17;
    public static final int BASE_LENGTH = 14;

    /**
     * 定时器基本开销 ：自研为8  合广为9
     */
//    public static final int TIMER_BASE_LENGTH = 9;
    public static final int TIMER_BASE_LENGTH = 12;

    /**
     * 协议版本：合广才有，自研无
     */
    public static final byte VERSION = 0x10;

    /**
     * 设备类型编码
     */
    public static final byte CONTROL_IDENTIFIER1_1 = (byte) 0x80;

    /**
     * 命令信息编码:获取时间
     */
    public static final byte CONTROL_IDENTIFIER2_1 = (byte) 0x51;

    /**
     * 命令信息编码:设置时间
     */
    public static final byte CONTROL_IDENTIFIER2_2 = (byte) 0x52;

    /**
     * 命令信息编码:获取设备厂家信息
     */
    public static final byte CONTROL_IDENTIFIER2_4 = (byte) 0x54;

    /**
     * 命令信息编码:设置系统参数
     */
    public static final byte CONTROL_IDENTIFIER2_8 = (byte) 0x58;

    /**
     * 命令信息编码:设置场景
     */
    public static final byte CONTROL_IDENTIFIER2_9 = (byte) 0x43;

    /**
     * 命令信息编码:设置定时
     */
    public static final byte CONTROL_IDENTIFIER2_10 = (byte) 0x45;

    /**
     * 命令信息编码:控制命令
     */
    public static final byte CONTROL_IDENTIFIER2_11 = (byte) 0x97;

    /**
     * 命令信息编码:按设备获取实时数据
     */
    public static final byte CONTROL_IDENTIFIER2_12 = (byte) 0x93;

    /**
     * 命令信息编码:按地址段获取实时数据
     */
    public static final byte CONTROL_IDENTIFIER2_13 = (byte) 0x94;

    /**
     * 命令信息编码:设置基本参数
     */
    public static final byte CONTROL_IDENTIFIER2_14 = (byte) 0x47;

    /**
     * 命令信息编码:群控制命令
     */
    public static final byte CONTROL_IDENTIFIER2_15 = (byte) 0x9B;

    /**
     * 场景模块
     */
    public static final int MODULE_TYPE_1 = 1;

    /**
     * 驱动模块
     */
    public static final int MODULE_TYPE_2 = 2;

    /**
     * 输入模块
     */
    public static final int MODULE_TYPE_3 = 3;

    /**
     * 电流模块
     */
    public static final int MODULE_TYPE_4 = 4;

    /**
     * 自研新增: 交流接触器模块
     */
    public static final int MODULE_TYPE_5 = 5;


    /**
     * 传感器回路
     */
    public static final int CHANNEL_TYPE_1 = 1;

    /**
     * 继电器回路
     */
    public static final int CHANNEL_TYPE_2 = 2;

    /**
     * 场景回路
     */
    public static final int CHANNEL_TYPE_3 = 3;

    /**
     * 自研新增: 电流模块回路
     */
    public static final int CHANNEL_TYPE_4 = 11;

    /**
     * 自研新增： 交流接触器模块回路
     */
    public static final int CHANNEL_TYPE_5 = 12;

    /**
     * 自研新增： 交流接触器模块绑定相位 A
     */
    public static final int CHANNEL_BIND_PHASE_POSITION_A = 1;

    /**
     * 自研新增： 交流接触器模块绑定相位 B
     */
    public static final int CHANNEL_BIND_PHASE_POSITION_B = 2;

    /**
     * 自研新增： 交流接触器模块绑定相位 C
     */
    public static final int CHANNEL_BIND_PHASE_POSITION_C = 3;

    /**
     * 模拟输入量
     */
    public static final int SUPERVISION_TYPE_1 = 1;

    /**
     * 状态输入量
     */
    public static final int SUPERVISION_TYPE_2 = 2;

    /**
     * 状态输出量
     */
    public static final int SUPERVISION_TYPE_4 = 4;

    /**
     * 执行输出量
     */
    public static final int SUPERVISION_TYPE_11 = 11;

    /**
     * 场景设备编号
     */
    public static final int DEVICE_TYPE_1 = 17;

    /**
     * 通用照明设备1
     */
    public static final int DEVICE_TYPE_2 = 22;

    /**
     * 通用单个模拟量输入设备
     */
    public static final int DEVICE_TYPE_3 = 11;

    /**
     * 通用单个开关量输入设备
     */
    public static final int DEVICE_TYPE_4 = 12;

    /**
     * 自研:新增通用电流检测设备
     */
    public static final int DEVICE_TYPE_5 = 13;

    /**
     * 自研:新增通用交流接触器设备
     */
    public static final int DEVICE_TYPE_6 = 14;

    /**
     * 控制器回路类型
     */
    public static final int CHANNEL_TYPE_7 = 7;

    /**
     * 交换机回路类型
     */
    public static final int CHANNEL_TYPE_8 = 8;

    /**
     * can设备地址标识符
     */
    public static final String CAN_DEVICE_ADDRESS_HEAD = "A2";
    /**
     * can设备地址标识符
     */
    public static final String CAN_DEVICE_ADDRESS = "GATEWAY";
    /**
     * can设备地址结束标识符
     */
    public static final String CAN_DEVICE_ADDRESS_END = "//IP通信设备";
    /**
     * can设备标识符
     */
    public static final String CAN_DEVICE_TAG = "CANDEV";

    /**
     * com设备标识符
     */
    public static final String COM_DEVICE_TAG = "COMDEV";
    /**
     * com设备结束标识符
     */
    public static final String COM_DEVICE_END = "//CAN模块通道";
    /**
     * can回路标识符
     */
    public static final String CAN_CHANNEL_TAG = "CANCHAN";
    /**
     * can回路标识符
     */
    public static final String CAN_CHANNEL_END = "//串口设备通道信息";
    /**
     * com回路标识符
     */
    public static final String COM_CHANNEL_TAG = "COMCHAN";

    /**
     * node表ip和port列在redis中的缓存key
     */
    public static final String REDIS_NODE_KEY = "power_ip_port";
    public static final String REDIS_IP_KEY = ":ip";
    public static final String REDIS_PORT_KEY = ":port";

    /**
     * 强电网关第一个心跳包记录(hash)
     */
    public static final String FIRST_HEARTBEAT_KEY = "power_first_heartbeat";

    /**
     * 常规模式
     */
    public static final int SCHEDULE_MODE_1 = 1;

    /**
     * 假日模式
     */
    public static final int SCHEDULE_MODE_2 = 2;


    /**
     * 自研发协议合集
     */

    /**
     * 获取网关软件版本号
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_1 = 0x01;
    /**
     * 获取MAC地址
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_2 = 0x02;
    /**
     * 设置网关时间指令
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_3 = 0x03;
    /**
     * 获取网关时间指令
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_4 = 0x04;
    /**
     * 心跳指令
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_5 = 0x05;
    /**
     * 设置场景参数
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_6 = 0x06;
    /**
     * 获取场景参数
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_7 = 0x07;
    /**
     * 设置定时参数
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_8 = 0x08;
    /**
     * 获取定时参数
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_9 = 0x09;

    /**
     * 设置经纬度
     */
    public static final byte CONTROL_IDENTIFIER_EXC_13 = 0x09;

    /**
     * 参数设置
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_10 = 0x0A;
    /**
     * 获取设置参数
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_11 = 0x0B;
    /**
     * 设置设备名称
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_12 = 0x0C;

    /**
     * 设置串口参数
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_14 = 0x0E;
    /**
     * 获取串口配置
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_15 = 0x0F;

    /**
     * 设置经纬度
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_19 = 0x13;


    /**
     * 获取经纬度和日出日落时间
     * 来源:自研发
     * 类型：通用
     */
    public static final byte CONTROL_IDENTIFIER_EXC_20 = 0x14;

    /**
     * 主动上报变化数据
     * 来源:自研发
     * 类型：运维
     */
    public static final byte CONTROL_IDENTIFIER_EXC_21 = 0x21;
    /**
     * 主动上报告警数据
     * 来源:自研发
     * 类型：运维
     */
    public static final byte CONTROL_IDENTIFIER_EXC_22 = 0x22;
    /**
     * 获取模块的回路电流数据
     * 来源:自研发
     * 类型：运维
     */
    public static final byte CONTROL_IDENTIFIER_EXC_23 = 0x23;
    /**
     * 查询模块在线状态
     * 来源:自研发
     * 类型：运维
     */
    public static final byte CONTROL_IDENTIFIER_EXC_24 = 0x24;
    /**
     * 网关升级指令
     * 来源:自研发
     * 类型：运维
     */
    public static final byte CONTROL_IDENTIFIER_EXC_25 = 0x25;
    /**
     * 调取日志指令
     * 来源:自研发
     * 类型：运维
     */
    public static final byte CONTROL_IDENTIFIER_EXC_26 = 0x26;
    /**
     * 重启网关指令
     * 来源:自研发
     * 类型：运维
     */
    public static final byte CONTROL_IDENTIFIER_EXC_27 = 0x27;
    /**
     * 恢复出厂设置指令
     * 来源:自研发
     * 类型：运维
     */
    public static final byte CONTROL_IDENTIFIER_EXC_28 = 0x28;
    /**
     * 获取网关点表信息
     * 来源:自研发
     * 类型：运维
     */
    public static final byte CONTROL_IDENTIFIER_EXC_29 = 0x29;
    /**
     * 模块升级指令
     * 来源:自研发
     * 类型：运维
     */
    public static final byte CONTROL_IDENTIFIER_EXC_41 = 0x41;
    /**
     * 模块地址自动设置指令
     * 来源:自研发
     * 类型：模块控制指令
     */
    public static final byte CONTROL_IDENTIFIER_EXC_42 = 0x42;
    /**
     * 指定修改模块地址
     * 来源:自研发
     * 类型：模块控制指令
     */
    public static final byte CONTROL_IDENTIFIER_EXC_43 = 0x43;
    /**
     * 模块搜索指令
     * 来源:自研发
     * 类型：模块控制指令
     */
    public static final byte CONTROL_IDENTIFIER_EXC_44 = 0x44;
    /**
     * 单控指令
     * 来源:自研发
     * 类型：模块控制指令
     */
    public static final byte CONTROL_IDENTIFIER_EXC_45 = 0x45;
    /**
     * 群控指令
     * 来源:自研发
     * 类型：模块控制指令
     */
    public static final byte CONTROL_IDENTIFIER_EXC_46 = 0x46;

    /**
     * COM端口号 默认为1
     */
    public static final int GATEWAY_COM_1 = 1;
    /**
     * COM端口号 默认为1
     */
    public static final int GATEWAY_COM_2 = 2;
    /**
     * COM端口号 默认为1
     */
    public static final int GATEWAY_COM_3 = 3;
    /**
     * COM端口号 默认为1
     */
    public static final int GATEWAY_COM_4 = 4;
    /**
     * COM端口号 默认为1
     */
    public static final int GATEWAY_COM_5 = 5;
    /**
     * 默认电表信息单个模块设备长度
     * modification 20200420 55修改59  设备型号新增4字节
     * 20200522 修改为67
     */
    public static final int GATEWAY_MODULE_ONE_BODY = 67;

    /**
     * 下限值
     */
    public static final double CHANNEL_LOWER_VALUE = -9999.0;

    /**
     * 上限值
     */
    public static final double CHANNEL_UPPER_VALUE = 9999.0;

    /**
     * 驱动模块回路前缀
     */
    public static final String CHANNEL_DRIVE_NAME_PREFIX = "驱动模块,通道";


    /**
     * 场景模块回路前缀
     */
    public static final String CHANNEL_SCENE_NAME_PREFIX = "场景模块,通道";

    /**
     * 交流接触器回路前缀
     */
    public static final String CHANNEL_ACC_NAME_PREFIX = "交流接触器模块,通道";

    /**
     * 自研
     * 电流模块回路前缀
     */
    public static final String CHANNEL_ELECTRICITY_NAME_PREFIX = "电流模块,通道";

    /**
     * 驱动模块监控数据类型ID
     */
    public static final int CHANNEL_DRIVE_DATA_TYPE_ID = 4;

    /**
     * 电流模块类型
     */
    public static final int CHANNEL_ELECTRICITY_DATA_TYPE_ID = 7;

    /**
     * 交流接触器模块类型
     */
    public static final int CHANNEL_ACC_DATA_TYPE_ID = 9;

    /**
     * 场景模块监控数据类型ID
     */
    public static final int CHANNEL_SCENE_DATA_TYPE_ID = 11;

    /**
     * 控制返回 正常
     */
    public static final byte RET_1 = 0;

    /**
     * 控制返回  ADR不匹配
     */
    public static final int RET_2 = 1;

    /**
     * 控制返回 CMD无效
     */
    public static final int RET_3 = 2;

    /**
     * 控制返回 INFO数据错误 CRC校验出错
     */
    public static final int RET_4 = 3;

    /**
     * 控制返回 命令执行失败
     */
    public static final int RET_5 = 4;

    /**
     * 控制返回 系统执行结果不对
     */
    public static final int RET_6 = 5;

    /**
     * 控制返回 解密错误
     */
    public static final int RET_7 = 6;

    /**
     * 控制返回 数据错误 协议通或协议尾错误
     */
    public static final int RET_8 = 7;

    /**
     * 控制回路状态  0x00 开  0xFF 关  0x55反转
     */
    public static final String CHANNEL_CONTROL_STATUS = "00FF55";

    /**
     * 回路控制延时ms数
     */
    public static final int CHANNEL_CONTROL_DELAYED_TIME = 300;

    /**
     * 定时参数设置；以星期定时时前三个字节为0
     */
    public static final int TIMING_VALUE_1 = 0;

    /**
     * 立即执行场景SN
     */
    public static final int SCENE_NOW_CONTROL_SN = 1;
}
