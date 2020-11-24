package com.exc.street.light.resource.vo.sl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class SingleLampParamRespVO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    @ApiModelProperty(name = "lampPosition" , value = "灯具位置")
    private String lampPosition;

    @ApiModelProperty(name = "lampPositionId" , value = "灯具位置id")
    private Integer lampPositionId;


    //开关状态
    private Integer brightState;
    //亮度
    private Integer brightness;


    //模块温度
    private Integer moduleTemperature;
    //当前电压有效值
    private Integer voltage;


    //通道当前电流有效值
    private Integer electricCurrent;
    //通道当前有功功率
    private Integer power;
    //通道有功电能（累计）
    private Double electricEnergy;


    //模块上电时长
    private Integer powerTime;
    //模块累计工作时长
    private Integer moduleATime;


    //通道灯具累计工作时长
    private Integer lampATime;
    //通道灯具工作时长
    private Integer lampTime;



    //告警
    private String alarm;
    //电压告警次数
    private Integer voltageAlarm;
    //电流告警次数
    private Integer electricCurrentAlarm;
    //温度告警次数
    private Integer temperatureAlarm;


    //出厂序列号(6字节)
    private String factorySerialNum;

    @ApiModelProperty(name = "networkState" , value = "网络状态(0:离线，1:在线)默认0")
    private Integer networkState;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "lastOnlineTime" , value = "最后在线时间")
    private Date lastOnlineTime;

    //private Integer deviceId;

    private String num;

    @ApiModelProperty(name = "loopNum" , value = "回路数")
    private Integer loopNum;

    @ApiModelProperty(name = "loopTypeId" , value = "回路类型id")
    private Integer loopTypeId;

    @ApiModelProperty(name = "loopType" , value = "回路类型")
    private String loopType;

    @ApiModelProperty(name = "model" , value = "设备型号")
    private String model;

    @ApiModelProperty(name = "factory" , value = "设备厂家")
    private String factory;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;

    @ApiModelProperty(name = "sendId" , value = "信息发送标识")
    private String sendId;

    /*@ApiModelProperty(name = "devEui" , value = "信息发送标识")
    private String devEui;*/
}
