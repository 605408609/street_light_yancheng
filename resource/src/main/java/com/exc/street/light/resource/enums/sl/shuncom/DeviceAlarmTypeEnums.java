package com.exc.street.light.resource.enums.sl.shuncom;

/**
 * 顺舟云盒 设备告警类型
 *
 * @Author: Xiaok
 * @Date: 2020/8/21 16:22
 */
public enum DeviceAlarmTypeEnums {
    NORMAL(0, "电压正常"),
    ALARM_1(1, "无电压"),
    ALARM_2(2, "欠电压"),
    ALARM_3(3, "过电压"),
    ALARM_10(10, "电流正常"),
    ALARM_11(11, "无电流"),
    ALARM_12(12, "欠电流"),
    ALARM_13(13, "过电流"),
    ALARM_20(20, "功率正常"),
    ALARM_21(21, "无功率"),
    ALARM_22(22, "低功率"),
    ALARM_23(23, "过功率"),
    ALARM_60(60, "电源停电恢复"),
    ALARM_61(61, "电源停电报警"),
    ALARM_62(62, "电源缺相恢复"),
    ALARM_63(63, "电源缺相报警"),
    ALARM_64(64, "交流接触器失效恢复"),
    ALARM_65(65, "交流接触器失效报警"),
    ALARM_66(66, "配电箱门开恢复"),
    ALARM_67(67, "配电箱门开报警"),
    ALARM_68(68, "壳体拆除恢复"),
    ALARM_69(69, "壳体拆除报警"),
    ALARM_70(70, "异常亮灯恢复"),
    ALARM_71(71, "异常亮灯报警"),
    ALARM_72(72, "异常灭灯恢复"),
    ALARM_73(73, "异常灭灯报警"),
    ALARM_80(80, "设备离线恢复"),
    ALARM_81(81, "设备离线报警"),
    ALARM_100(100, "A相电压正常"),
    ALARM_101(101, "A相无电压"),
    ALARM_102(102, "A相欠电压"),
    ALARM_103(103, "A相过电压"),
    ALARM_104(104, "A相电流正常"),
    ALARM_105(105, "A相无电流"),
    ALARM_106(106, "A相欠电流"),
    ALARM_107(107, "A相过电流"),
    ALARM_108(108, "A相功率正常"),
    ALARM_109(109, "A相无功率是"),
    ALARM_110(110, "A相低功率"),
    ALARM_111(111, "A相过功率"),
    ALARM_120(120, "B相电压正常"),
    ALARM_121(121, "B相无电压"),
    ALARM_122(122, "B相欠电压"),
    ALARM_123(123, "B相过电压"),
    ALARM_124(124, "B相电流正常"),
    ALARM_125(125, "B相无电流"),
    ALARM_126(126, "B相欠电流"),
    ALARM_127(127, "B相过电流"),
    ALARM_128(128, "B相功率正常"),
    ALARM_129(129, "B相无功率"),
    ALARM_130(130, "B相低功率"),
    ALARM_131(131, "B相过功率"),
    ALARM_140(140, "C相电压正常"),
    ALARM_141(141, "C相无电压"),
    ALARM_142(142, "C相欠电压"),
    ALARM_143(143, "C相过电压"),
    ALARM_144(144, "C相电流正常"),
    ALARM_145(145, "C相无电流"),
    ALARM_146(146, "C相欠电流"),
    ALARM_147(147, "C相过电流"),
    ALARM_148(148, "C相功率正常"),
    ALARM_149(149, "C相无功率"),
    ALARM_150(150, "C相低功率"),
    ALARM_151(151, "C相过功率"),
    ALARM_160(160, "A相电源缺相恢复"),
    ALARM_161(161, "A相电源缺相报警"),
    ALARM_162(162, "B相电源缺相恢复"),
    ALARM_163(163, "B相电源缺相报警"),
    ALARM_164(164, "C相电源缺相恢复"),
    ALARM_165(165, "C相电源缺相报警"),
    ALARM_166(166, "过放"),
    ALARM_167(167, "超压"),
    ALARM_168(168, "负载短路"),
    ALARM_169(169, "电池故障"),
    ALARM_170(170, "内部超温"),
    ALARM_171(171, "外部超温"),
    ALARM_172(172, "市电互补负载供电状态市电"),
    ALARM_173(173, "负载开路"),
    ALARM_174(174, "输出电容超压保护"),
    ALARM_175(175, "电池板过流"),
    ALARM_176(176, "电池板短路"),
    ALARM_177(177, "电池板超压"),
    ALARM_178(178, "电池板反接"),
    ALARM_179(179, "充电逆流"),
    ALARM_180(180, "锂电池低温关闭充电");


    /**
     * 数据code
     */
    private int code;

    /**
     * 说明
     */
    private String name;
    
    DeviceAlarmTypeEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int code() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static DeviceAlarmTypeEnums getByCode(int code) {
        for (DeviceAlarmTypeEnums c : DeviceAlarmTypeEnums.values()) {
            if (c.code() == code) {
                return c;
            }
        }
        return null;
    }
}
