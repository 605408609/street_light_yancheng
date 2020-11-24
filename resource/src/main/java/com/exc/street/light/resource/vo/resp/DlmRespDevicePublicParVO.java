package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@ToString
public class DlmRespDevicePublicParVO<T> {

    /**
     * 设备id
     */
    @ApiModelProperty(name = "id" , value = "设备id")
    private Integer id;

    /**
     * 分别id
     */
    @ApiModelProperty(name = "partId" , value = "分别id")
    private String partId;

    /**
     * 设备编号
     */
    @ApiModelProperty(name = "num" , value = "设备编号")
    private String num;

    /**
     * 设备名称
     */
    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    /**
     * 网络状态(0:离线，1:在线)默认0
     */
    @ApiModelProperty(name = "networkState" , value = "网络状态(0:离线，1:在线)默认0")
    private Integer networkState;

    /**
     * 最后在线时间
     */
    @ApiModelProperty(name = "lastOnlineTime" , value = "最后在线时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastOnlineTime;

    /**
     * 设备类型编号（1：智慧照明，2：公共WIFI，3：公共广播，4：智能安防，5：信息发布，6：一键呼叫，7：环境监测）
     */
    @ApiModelProperty(name = "deviceType" , value = "设备类型编号（1：智慧照明，2：公共WIFI，3：公共广播，4：智能安防，5：信息发布，6：一键呼叫，7：环境监测）")
    private Integer deviceType;

    /**
     * 灯杆id
     */
    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    /**
     * 灯杆id
     */
    @ApiModelProperty(name = "superId" , value = "灯杆id")
    private Integer superId;

    /**
     * 灯杆name
     */
    @ApiModelProperty(name = "superName" , value = "灯杆name")
    private String superName;

    /**
     * 设备详细字段对象
     */
    @ApiModelProperty(name = "dlmRespDevice" , value = "设备详细字段对象")
    private T dlmRespDevice;

}
