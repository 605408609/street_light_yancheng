package com.exc.street.light.resource.enums.sl.shuncom;

/**
 * 顺舟云盒 设备扩展属性
 * @Author:Xiaok
 * @Date:2020/8/2016:52
 */
public enum DeviceStInfoEnums {
    mac("mac", "网关mac地址", "String", false),
    hver("hver", "硬件版本", "Integer", false),
    sver("sver", "软件版本", "Integer", false),
    fac("mac", "厂家名", "String", false),
    dsp("dsp", "设备描述", "String", false),
    date("date", "日期", "String", false),
    ps("ps", "电源类型", "Integer", false),
    swid("swid", "软件id，即软件版本号", "String", false),
    on("on", "开关状态:true：开false：关", "Boolean", false),
    childlock("childlock", "童锁开关状态:true：开false：关", "Boolean", false),

    sact("sact", "开关切换动作", "Integer", false),
    swht("swht", "开关切换类型", "Integer", false),
    bri("bri", "当前亮度状态:取值范围:1-254", "Integer", false),
    rmtim("rmtim", "剩余时间", "Integer", false),
    ooftrantim("ooftrantim", "开关转换时间", "Integer", false),
    onlev("onlev", "当前亮度", "Integer", false),
    otrantim("otrantim", "开转换时间", "Integer", false),
    oftrantim("oftrantim", "关转换时间", "Integer", false),
    rate("rate", "开转换时间", "变化速度", false),
    energy("energy", "当前测量总量，耗电量、流量等等", "Long", false),
    msta("msta", "测量状态", "Integer", false),
    munit("munit", "测量单位", "Integer", false),
    mmult("mmult", "测量值乘数", "Integer", false),
    mdiv("mdiv", "测量值除数", "Integer", false),
    msum("msum", "测量总量格式", "Integer", false),
    mdtype("mdtype", "测量设备类型", "Integer", false),
    volt("volt", "实时电压", "Integer", false),
    avolt("avolt", "a相实时电压", "Integer", false),
    bvolt("bvolt", "b相实时电压", "Integer", false),
    cvolt("cvolt", "c相实时电压", "Integer", false),
    curr("curr", "实时电流", "Integer", false),
    acurr("acurr", "a相实时电流", "Integer", false),
    bcurr("bcurr", "b相实时电流", "Integer", false),
    ccurr("ccurr", "c相实时电流", "Integer", false),
    actp("actp", "有功功率", "Integer", false),
    aactp("aactp", "a相有功功率", "Integer", false),
    bactp("bactp", "b相有功功率", "Integer", false),
    cactp("cactp", "c相有功功率", "Integer", false),
    reactp("reactp", "无功功率", "Integer", false),
    reactps("reactps", "无功功率", "String", false),
    areactp("areactp", "a相无功功率", "Integer", false),
    breactp("breactp", "b相无功功率", "Integer", false),
    creactp("creactp", "c相无功功率", "Integer", false),
    facp("facp", "功率系数", "Integer", false),
    afacp("afacp", "a相功率系数", "Integer", false),
    bfacp("bfacp", "b相功率系数", "Integer", false),
    cfacp("cfacp", "c相功率系数", "Integer", false),
    PDiv("PDiv", "功率除数", "Long", false),
    aPDiv("aPDiv", "a相功率除数", "Long", false),
    bPDiv("bPDiv", "b相功率除数", "Long", false),
    cPDiv("cPDiv", "c相功率除数", "Long", false),
    Vdiv("Vdiv", "电压除数", "Integer", false),
    aVdiv("aVdiv", "a相电压除数", "Integer", false),
    bVdiv("bVdiv", "b相电压除数", "Integer", false),
    cVdiv("cVdiv", "c相电压除数", "Integer", false),
    Cdiv("Cdiv", "电流除数", "Integer", false),
    aCdiv("aCdiv", "a相电流除数", "Integer", false),
    bCdiv("bCdiv", "b相电流除数", "Integer", false),
    cCdiv("cCdiv", "c相电流除数", "Integer", false),
    fmode("fmode", "风扇工作模式", "Integer", false),
    fseq("fseq", "风扇序列", "Integer", false),
    mtemp("mtemp", "测量温度", "Integer", false),
    mtemps("mtemps", "测量温度", "String", false),
    minmtemp("minmtemp", "最小测量温度", "Integer", false),
    maxmtemp("maxmtemp", "最大测量温度", "Integer", false),
    mhumi("mhumi", "测量湿度", "Integer", false),
    minhumi("minhumi", "最小测量湿度", "Integer", false),
    maxhumi("maxhumi", "最大测量湿度", "Integer", false),
    allowerr("allowerr", "允许误差", "Integer", false),
    zsta("zsta", "zone设备状态", "Integer", false),
    ztype("ztype", "zone设备类型", "Integer", false),
    zid("zid", "zone设备id", "Integer", false),
    mlux("zsta", "测量光照度", "Integer", false),
    minmlux("minmlux", "最小测量光照度", "Integer", false),


    //未设置
    maxmlux("maxmlux","最大测量光照度","Integer",false),
    backled("backled","背光灯模式","Integer",false),
    luxlev("luxlev","光照度等级","Integer",false),
    tluxlev("tluxlev","目标光照度","Integer",false),
    bats("bats","电池大小","Integer",false),
    palm("palm","电池报警","Integer",false),
    batpt("batpt","电池剩余电量","Integer",false),
    dsta("dsta","门锁状态","Integer",false),
    dtype("dtype","门锁类型","Integer",false),
    den("den","门锁自动开关使能","Boolean",false),
    dalm("dalm","门锁报警","Integer",false),
    theralm("theralm","恒温器报警","Integer",false),
    ltemp("ltemp","恒温器实际温度","Integer",false),
    coolset("coolset","恒温器制冷设置值","Integer",false),
    heatset("heatset","恒温器制热设置值","Integer",false),
    NumOfAircond("NumOfAircond","空调个数","Integer",false),
    thermode("thermode","空调模式","Integer",false),
    pt("pt","窗帘开度","Integer",false),
    wtype("wtype","窗帘类型","Integer",false),
    wmode("wmode","空调模式","Integer",false),
    hue("hue","颜色状态:取值范围:1-254","Integer",false),
    sat("sat","饱和度状态:取值范围:1-254","Integer",false),
    ctp("ctp","色温状态：取值范围:153-499","Integer",false),
    starttim("starttim","开始时间","Integer",false),
    occupancy("occupancy","传感配置率","Integer",false),
    identime("identime","识别时间","Integer",false),
    gnamsup("gnamsup","分组名称支持","Integer",false),
    scenecount("scenecount","场景个数","Integer",false),
    modelid("modelid","阿里定义设备类型","Integer",false),
    rand("rand","阿里定义随机字符串","String",false),
    sign("sign","阿里定义标志字符串","String",false),
    Supervision("Supervision","阿里定义心跳","Integer",false),
    lqi("lqi","链路质量","Integer",false),

    pm25("pm25","pm25","Integer",false),
    CO2("CO2","二氧化碳含量","Integer",false),
    formaldehyde("formaldehyde","否","Integer",false),
    kval("kval","否","Integer",false),
    szrad("szrad","顺舟私有透传属性","String",false),
    lightsta("lightsta","路灯状态","Integer",false),
    lightnetsta("lightnetsta","路灯联网状态","Integer",false),
    lightf("lightf","路灯频率","Integer",false),
    lightfs("lightfs","路灯频率","String",false),
    lightalm("lightalm","路灯报警","Integer",false),
    gwaddr("gwaddr","网关地址","String",false),
    gwnam("gwnam","网关名称","String",false),
    gwtype("gwtype","网关类型","String",false),
    gwlongitude("gwlongitude","经度","String",false),
    gwlatitude("gwlatitude","纬度","String",false),
    imei("imei","设备","String",true),
    rssi("rssi","设备信号强度（0-100）","Integer",true),
    imsi("imsi","设备SIM卡标识号","String",true),
    userid("userid","设备的用户描述","String",false),
    softwareversion("softwareversion","设备软件版本","String",false),

    airtemp("airtemp","空气温度","Integer",false),
    airhumi("airhumi","空气湿度","Integer",false),
    soiltemp("soiltemp","土壤温度","Integer",false),
    soilhumi("soilhumi","土壤湿度","Integer",false),
    lux("lux","光照强度","Integer",false),
    co2ppm("co2ppm","Co2浓度","Integer",false),
    soilPH("soilPH","土壤PH","Integer",false),
    soilEC("soilEC","土壤电导率","Integer",false),
    o2("o2","氧气","Integer",false),
    airkPa("airkPa","大气压","Integer",false),
    wspeed("wspeed","风速","Integer",false),
    wdir("wdir","风向","Integer",false),
    rain("rain","雨量","Integer",false),
    ver("ver","网关版本号","String",false),
    nettype("nettype","网关接入互联网的方式。4gnet：4g卡；wireless：作为sta连接别的路由器；wireline：有线；none：未知联网方式，因某些原因，网关判断不了自己通过什么连接网络。","String",false),

    gwmode("gwmode","网关模式。1：性能模式；性能模式：网关上报网关网络状态和网关基本状态，且心跳间隔短；2：标准模式；标准模式：网关只上报心跳维持连接，且心跳包中不带设备信息、是timezone_number时区","Integer",false),
    lalen("lalen","v3灯控器经纬度使能；0：开启；1：关闭；","Integer",false),
    v3ruen("v3ruen","v3灯控器策略使能；0：开启；1：关闭；","Integer",false),
    luxen("luxen","v3灯控器光感使能；0：开启；1：关闭；","Integer",false),
    HW_model("HW_model","网关硬件型号：SZ11-GW-3：聚盒SZ11-GW-4：小聚盒SZ09-GW-2：4531家居网关SZ10-GW-4：四代路灯SZ10-GW-5：云盒SZ10-GW-6：lora防水基站","String",true),
    satnum("satnum","卫星个数","Integer",false),
    gpstp("gpstp","定位系统类型。1：GPS；2：北斗；3：GPS加北斗；","Integer",false),
    chiptype("chiptype","表示网关芯片方案如7688、4531","String",false),
    gwapptype("gwapptype","表示网关应用类型如音乐网关、穿墙王、离线语音网关","String",false),
    lansta1("lansta1","网关lan口状态，0-未知状态；1-连接；2-断开；","Integer",false),
    lansta2("lansta2","网关lan口状态，0-未知状态；1-连接；2-断开；","Integer",false),
    lansta3("lansta3","网关lan口状态，0-未知状态；1-连接；2-断开；","Integer",false),
    lansta4("lansta4","网关lan口状态，0-未知状态；1-连接；2-断开；","Integer",false),
    lansta5("lansta5","网关lan口状态，0-未知状态；1-连接；2-断开；","Integer",false),
    lansta6("lansta6","网关lan口状态，0-未知状态；1-连接；2-断开；","Integer",false),
    lansta7("lansta7","网关lan口状态，0-未知状态；1-连接；2-断开；","Integer",false),
    lansta8("lansta8","网关lan口状态，0-未知状态；1-连接；2-断开；","Integer",false),
    loopnetsta("loopnetsta","环网的状态，0-未知；1-连接；2-断开；","Integer",false),
    grssi_4g("4grssi","4g信号强度","Integer",false),
    ;


    /**
     * 数据code
     */
    private String code;

    /**
     * 说明
     */
    private String name;

    /**
     * 数据类型StringLongInteger
     */
    private String dataType;

    /**
     * 是否必需
     */
    private boolean isRequired;

    DeviceStInfoEnums(String code, String name, String dataType, boolean isRequired) {
        this.code = code;
        this.name = name;
        this.dataType = dataType;
        this.isRequired = isRequired;
    }

    public String code() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public static DeviceStInfoEnums getByCode(String code) {
        for (DeviceStInfoEnums c : DeviceStInfoEnums.values()) {
            if (c.code().equals(code)) {
                return c;
            }
        }
        return null;
    }
}
