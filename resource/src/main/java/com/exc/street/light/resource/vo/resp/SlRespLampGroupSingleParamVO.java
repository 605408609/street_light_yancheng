package com.exc.street.light.resource.vo.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 灯具分组列表返回对象
 *
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
@Data
public class SlRespLampGroupSingleParamVO {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    @ApiModelProperty(name = "lampPositionId" , value = "灯具位置id")
    private Integer lampPositionId;

    //开关状态
    private Integer brightState;
    //开关2状态
    //private String stateTwo;
    //亮度
    private Integer brightness;
    //2亮度
    //private Integer brightnessTwo;


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


    //通道2当前电流2有效值
    //private Integer electricCurrentTwo;
    //通道2当前有功功率2
    //private Integer powerTwo;
    //通道2有功电能（累计）
    //private Double electricEnergyTwo;


    //通道2灯具累计工作时长
    //private Integer lampATimeTwo;
    //通道2灯具工作时长
    //private Integer lampTimeTwo;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private Integer deviceId;

    private String deviceNum;

    @ApiModelProperty(name = "loopNum" , value = "回路数")
    private Integer loopNum;

    @ApiModelProperty(name = "loopTypeId" , value = "回路类型id")
    private Integer loopTypeId;

    @ApiModelProperty(name = "lampPosition" , value = "回路位置")
    private String lampPosition;
}
