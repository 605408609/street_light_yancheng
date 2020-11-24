package com.exc.street.light.lj.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TrashVo {

    //终端id
    private  Integer id;
    //设备类型
    private Integer type;
    //产品SN(每个终端具有全局唯一的 SN )
    private Integer sn;
    //硬件版本信息
    private String hw;
    //定时上报时间间隔(单位分钟)
    private Integer timingTime;
    //采样时间间隔(单位分钟)
    private Integer samplingTime;
    //报警深度阈值
    private Integer alarmDepth;
    //终端状态（0正常  1电池欠压  2云端应答错误  3无线模块状态异常  4设备撤防  5A桶报警  6B桶报警）
    private Integer trashType;
    //垃圾桶A深度
    private Integer alarmDepthA;
    //垃圾桶B深度
    private Integer alarmDepthB;
    //报警时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date date;
}
