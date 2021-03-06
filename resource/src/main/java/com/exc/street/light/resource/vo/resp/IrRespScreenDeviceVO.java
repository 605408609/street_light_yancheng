package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 显示屏设备详情返回对象
 *
 * @author Longshuangyang
 * @date 2020/04/01
 */
@Setter
@Getter
@ToString
public class IrRespScreenDeviceVO {

    @ApiModelProperty(name = "id" , value = "显示屏表id，自增长")
    private Integer id;

    @ApiModelProperty(name = "ip" , value = "ip")
    private String ip;

    @ApiModelProperty(name = "port" , value = "端口")
    private Integer port;

    @ApiModelProperty(name = "width" , value = "屏幕宽")
    private Float width;

    @ApiModelProperty(name = "height" , value = "屏幕高")
    private Float height;

    @ApiModelProperty(name = "bright" , value = "显示屏亮度")
    private Integer bright;

    @ApiModelProperty(name = "volume" , value = "显示屏音量")
    private Integer volume;

    @ApiModelProperty(name = "factory" , value = "设备厂家")
    private String factory;

    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "设备编号")
    private String num;

    @ApiModelProperty(name = "networkState" , value = "网络状态(0:离线，1:在线)默认0")
    private Integer networkState;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "lastOnlineTime" , value = "最后在线时间")
    private Date lastOnlineTime;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;

    @ApiModelProperty(name = "model" , value = "设备型号")
    private String model;

    @ApiModelProperty(name = "switchState" , value = "显示屏开关状态（0：关，1：开）")
    private Integer switchState;

    @ApiModelProperty(name = "isPlayProgram" , value = "是否播放节目（0：否，1：是）")
    private Integer isPlayProgram;

    @ApiModelProperty(name = "isPlaySubtitle" , value = "是否播放字幕（0：否，1：是）")
    private Integer isPlaySubtitle;

}
