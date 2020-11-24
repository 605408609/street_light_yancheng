/**
 * @filename:SystemDevice 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.resource.entity.sl;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description:TODO(设备表实体类)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SingleLampParam extends Model<SingleLampParam> {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id" , value = "设备表")
    private Integer id;

    /*@ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    @ApiModelProperty(name = "lampPositionId" , value = "灯具位置id")
    private Integer lampPositionId;*/

    //开关状态
    private Integer brightState;
    //亮度
    private Integer brightness;


    //模块温度
    private Double moduleTemperature;
    //当前电压有效值
    private Double voltage;


    //通道当前电流有效值
    private Double electricCurrent;
    //通道当前有功功率
    private Double power;
    //通道有功电能（累计）
    private Double electricEnergy;


    //模块上电时长
    private Double powerTime;
    //模块累计工作时长
    private Double moduleATime;


    //通道灯具累计工作时长
    private Double lampATime;
    //通道灯具工作时长
    private Double lampTime;



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
    //卡号
    private String cardNumber;

    //信号强度
    private Integer signalIntensity;
    //电压频率
    private Integer voltageFrequency;
    //功率因数
    private Integer powerFactor;
    //灯杆x，y偏移量
    private Integer poleOffsetXy;

    //灯杆x,y轴信息
    private Integer xyAxisInformation;
    //灯杆z轴信息
    private Integer zAxisInformation;
    //漏电数值
    private Integer leakageValue;
    //软件版本
    private String softwareVersion;
    //日出时分
    private Date atSunrise;
    //日落时分
    private Date downSunrise;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private Integer deviceId;

    /*private String deviceNum;

    @ApiModelProperty(name = "loopNum" , value = "回路数")
    private Integer loopNum;

    @ApiModelProperty(name = "loopTypeId" , value = "回路类型id")
    private Integer loopTypeId;*/

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
