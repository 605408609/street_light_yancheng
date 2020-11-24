package com.exc.street.light.resource.enums.sl.shuncom;

/**
 * 顺舟云盒 网关或设备控制属性
 * @Author:Xiaok
 * @Date:2020/8/2016:52
 */
public enum GatewayControlEnums {
    p("p","色温状态:取值范围:153–499","Integer",false),
    on("on","开关状态:true开,false关","Boolean",false),
    childlock("childlock","童锁开关状态:true开,false关","Boolean",false),
    raw("raw","发送透传数据","String",false),
    bri("bri","亮度状态:取值范围:1–254","Integer",false),
    hue("hue","颜色状态:取值范围:1–254","Integer",false),
    sat("sat","饱和度状态:取值范围:1–254","Integer",false),
    incd("incd","发送红外控制码","String",false),
    inle("inle","红外学习命令","Integer",false),
    inct("inct","红外控制命令","Integer",false),
    szrad("szrad","顺舟私有透传命令","String",false),
    tlux("tlux","目标光照度","String",false),
    pt("pt","表示百分比","Integer",false),
    ctrl("ctrl","0:关,向下,反转等;1:开,向上,正转等;2:停止","Integer",false),
    lock("lock","0:锁门1:不锁门2:取反","Integer",false),
    pmtjn("pmtjn","0:关闭permitjoin1~254:开始permitjoin,时间为1~254s255:永远开启permitjoin","Integer",false),
    wwtlst("wwtlst","[\"0100124b0004207950\",…]","JSONArray",false),
    gwtlst("gwtlst","{}:一个空的json对象","JSONObject",false),
    enwtlst("enwtlst","0:关闭白名单1:使能白名单","Integer",false),
    wtlst("wtlst","数组[\"0100124b0004207950\",…]","JSONArray",false),
    dn("dn","设备名称","String",false),
    uname("uname","门锁中用户的名称","String",false),
    voltMin("voltMin","无电压报警阈值","String",false),
    avoltMin("avoltMin","a相无电压报警阈值","String",false),
    bvoltMin("bvoltMin","b相无电压报警阈值","String",false),
    cvoltMin("cvoltMin","c相无电压报警阈值","String",false),
    voltLow("voltLow","欠电压报警阈值","String",false),
    avoltLow("avoltLow","a相欠电压报警阈值","String",false),
    bvoltLow("bvoltLow","b相欠电压报警阈值","String",false),
    cvoltLow("cvoltLow","c相欠电压报警阈值","String",false),
    voltMax("voltMax","过电压报警阈值","String",false),
    avoltMax("avoltMax","a相过电压报警阈值","String",false),
    bvoltMax("bvoltMax","b相过电压报警阈值","String",false),
    cvoltMax("cvoltMax","c相过电压报警阈值","String",false),
    currMin("currMin","无电流报警阈值","String",false),
    acurrMin("acurrMin","a相无电流报警阈值","String",false),
    bcurrMin("bcurrMin","b相无电流报警阈值","String",false),
    ccurrMin("ccurrMin","c相无电流报警阈值","String",false),
    currLow("currLow","欠电流报警阈值","String",false),
    acurrLow("acurrLow","a相欠电流报警阈值","String",false),
    bcurrLow("bcurrLow","b相欠电流报警阈值","String",false),
    ccurrLow("ccurrLow","c相欠电流报警阈值","String",false),
    currMax("currMax","过电流报警阈值","String",false),
    acurrMax("acurrMax","a相过电流报警阈值","String",false),
    bcurrMax("bcurrMax","b相过电流报警阈值","String",false),
    ccurrMax("ccurrMax","c相过电流报警阈值","String",false),
    looptime("looptime","设备轮询时间,单位","Integer",false),
    devfreq("devfreq","设备频段","Integer",false),
    lora_raw("raw","lora网关接收到的数据","String",false),
    rawlen("rawlen","lora网关接收到数据的长度","Integer",false),
    timestamp("timestamp","lora网关接收到数据的时间戳","Integer",false),
    loraep("loraep","lora","Integer",false),
    datr("datr","设备的发送速率","Integer",false),
    adr("adr","设备自适应发送速率","Integer",false),
    encfm("encfm","设置设备接收到命令时需不需要confirm0:不需要;1:需要","Integer",false),
    read("read","通过此字段,可让网管对设备进行实时的读取.\"srne\":表示读取硕日太阳能的所有属性;","String",false),
    Sz06crtl("Sz06crtl","Sz06采集器io输出命令.\"0001010002000301\":表示第一端口输出高电平,第二端口输出低电平,第三端口输出低,第四端口输出高;","String",false),
    screenPasswd("screenPasswd","四代路灯网关屏幕密码","String",false),
    rssh("rssh","反向登录的ip和port,例如:“192.168.5.22:12345”","String",false),
    agent("agent","穿透服务;1:开启穿透服务;2:关闭穿透服务;","Integer",false),
    PIRen("PIRen","微波、红外人体感应使能.0:使能;1:不使能;","Integer",false),
    rptitv("rptitv","4-20ma定时上报时间间","Integer",false),
    signtp("signtp","信号类型.4:开关量;5:4~20ma;","Integer",false),
    signen("signen","信号使能/不使能.0:使能;1:不使能","Integer",false),
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

    GatewayControlEnums(String code, String name, String dataType, boolean isRequired) {
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

    public static GatewayControlEnums getByCode(String code) {
        for (GatewayControlEnums c : GatewayControlEnums.values()) {
            if (c.code().equals(code)) {
                return c;
            }
        }
        return null;
    }
}
