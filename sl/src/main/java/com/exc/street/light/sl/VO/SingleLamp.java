package com.exc.street.light.sl.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SingleLamp {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //开关1状态
    private String stateOne;
    //开关2状态
    private String stateTwo;
    //1亮度
    private Integer brightnessOne;
    //2亮度
    private Integer brightnessTwo;


    //模块温度
    private Integer moduleTemperature;
    //当前电压有效值
    private Integer voltage;


    //通道1当前电流有效值
    private Integer electricCurrentOne;
    //通道1当前有功功率
    private Integer powerOne;
    //通道1有功电能（累计）
    private Integer electricEnergyOne;


    //模块上电时长
    private Integer powerTime;
    //模块累计工作时长
    private Integer moduleATime;


    //通道1灯具累计工作时长
    private Integer lampATimeOne;
    //通道1灯具工作时长
    private Integer lampTimeOne;



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


    //通道2当前电流2有效值
    private Integer electricCurrentTwo;
    //通道2当前有功功率2
    private Integer powerTwo;
    //通道2有功电能（累计）
    private Integer electricEnergyTwo;


    //通道2灯具累计工作时长
    private Integer lampATimeTwo;
    //通道2灯具工作时长
    private Integer lampTimeTwo;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
