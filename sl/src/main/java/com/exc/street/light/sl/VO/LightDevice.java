package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class LightDevice {

    //设备类型
    private Integer type	;
    //灯控器类型
    private Integer lamp_ctrl_type	;
    //同上
    private Integer lampControllerType	;
    //类型名称
    private String typeName	;
    //在线状态
    private Integer online	;
    //地址
    private String addrStr	;
    //地址
    private String addr	;
    //添加用户
    private String addUser	;
    //添加时间
    private String addDate	;
    //修改用户
    private String modifyUser	;
    //修改时间
    private String modifyDate	;
    //所属区划ID
    private String divisionId	;
    //所属区划名称
    private String advName	;
    //所属机构ID
    private String organizationId	;
    //所属机构名称
    private String orgName	;
    //所属项目ID
    private String projectId	;
    //所属项目名称
    private String prjName	;
    //设备名称
    private String name	;
    //经度
    private String longitude	;
    //纬度
    private String latitude	;
    //经度
    //private String longitudeMap	;
    //纬度
    //private String latitudeMap	;
    //端口号
    private Integer port_id	;
    //灯控器ID
    private String lamp_ctrl_id	;
    //网关ID
    //private String gatewayId	;
    //网关名称
    //private String gatewayName	;
    //回路号
    //private Integer sectionId	;
    //注册包
    //private String regPkg	;
    //变比
    private String formula	;
    //轮循时间
    private Integer pollInterval	;
    //是否加密
    private Integer encryption	;
    //
    //private String appEui	;
    //
    private String imsi	;
    //灯杆类型
    private Integer lampPoleType	;
    //灯杆编号
    private String lampPoleNumber	;
    //数据最新上报时间
    private String refresh_time	;
    //上一次数据最新上报时间
    private String pre_refresh_time	;
    //是否告警
    private Integer enable_alarm	;
    //频率(Hz)
    private String frequency	;
    //电流(A)
    private String current	;
    //无功功率(W)
    private String reactive_power	;
    //亮度(百分比)
    private Integer bri	;
    //温度(摄氏度)
    private String temperature	;
    //开关状态
    private Integer on	;
    //电压(V)
    private String voltage	;
    //上一次开关状态
    private Integer pre_on	;
    //是否支持双控
    private boolean dblControlZigbeeLamp;
    //有功功率(W)
    private String active_power	;
    //告警配置id
    //private String alarm_id	;

    //不知道
    private String roadside;
    private String runtime;
    private String pEnergy;
    private String lamp_post_number;
    private String sEnergy;
    private String powerFactor;
    private String poleHeight;
    private String qEnergy;
    private String lampsUses;
    private String lamp_post_type;
    private String location;





    private String uid;




    /*//三相电数据
    private List<LightThreePhaseElect> threePhaseElect;
    //回路(线圈)数据
    private List<LightSection> sections;*/


}
