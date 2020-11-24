package com.exc.street.light.resource.vo.pb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 广播定时任务vo
 * @Author: Xiaok
 * @Date: 2020/10/26 15:13
 */
@Data
public class PbRespRadioPlayPageVO {

    @ApiModelProperty(name = "id" , value = "广播播放表id，自增")
    private Integer id;

    @ApiModelProperty(name = "executionMode" , value = "执行模式(1:每天执行,2:每周执行,3:每月执行,4:一次性执行)")
    private Integer executionMode;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "executionTime" , value = "执行时间（时分秒）")
    private Date executionTime;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "executionEndTime" , value = "执行结束时间（时分秒）")
    private Date executionEndTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(name = "startDate" , value = "开始时间（年月日）")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(name = "endDate" , value = "结束时间（年月日）")
    private Date endDate;

    @ApiModelProperty(name = "weekValue" , value = "周控制值")
    private Integer weekValue;

    @ApiModelProperty(name = "playStatus" , value = "播放状态（0：待播放，1：正在播放 2:结束播放）")
    private Integer playStatus;

    @ApiModelProperty(name = "programId" , value = "节目id")
    private Integer programId;

    @ApiModelProperty(name = "programName" , value = "节目名称")
    private String programName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "radioDeviceId" , value = "广播设备ID")
    private String radioDeviceId;

    @ApiModelProperty(name = "radioDeviceName" , value = "广播设备名称")
    private String radioDeviceName;

    @ApiModelProperty(name = "radioDeviceVol" , value = "广播设备音量 0-56")
    private Integer radioDeviceVol;

    @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;
}
