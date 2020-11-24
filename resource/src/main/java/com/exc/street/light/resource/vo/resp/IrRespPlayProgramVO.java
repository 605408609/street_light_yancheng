package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 显示屏播放中节目返回对象
 *
 * @author Longshuangyang
 * @date 2020/04/03
 */
@Getter
@Setter
@ToString
public class IrRespPlayProgramVO {

    @ApiModelProperty(name = "id" , value = "显示屏播放表id，自增")
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "startDate" , value = "开始时间")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "endDate" , value = "结束时间")
    private Date endDate;

    @ApiModelProperty(name = "playStatus" , value = "播放状态（0：待播放，1：正在播放，2：结束播放）")
    private Integer playStatus;

    @ApiModelProperty(name = "deviceId" , value = "设备id")
    private Integer deviceId;

    @ApiModelProperty(name = "programId" , value = "节目id")
    private Integer programId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    /**
     * 显示屏设备名称
     */
    @ApiModelProperty(name = "deviceName" , value = "显示屏设备名称")
    private String deviceName;

    /**
     * 显示屏设备编号sn
     */
    @ApiModelProperty(name = "deviceSn" , value = "显示屏设备编号sn")
    private String deviceSn;

    /**
     * 节目名称
     */
    @ApiModelProperty(name = "programName" , value = "节目名称")
    private String programName;

}
