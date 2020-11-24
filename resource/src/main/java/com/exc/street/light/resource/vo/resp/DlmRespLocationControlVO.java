package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Xiezhipeng
 * @Description 集中控制器返回对象
 * @Date 2020/8/24
 */
@Data
public class DlmRespLocationControlVO {

    @ApiModelProperty(name = "id" , value = "集中控制器ID")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "编号")
    private String num;

    @ApiModelProperty(name = "location" , value = "地址")
    private String location;

    @ApiModelProperty(name = "typeId" , value = "集中控制器类型ID")
    private Integer typeId;

    @ApiModelProperty(name = "typeName" , value = "集中控制器类型名称")
    private String typeName;

    @ApiModelProperty(name = "isOnline" , value = "网络状态(0:离线，1:在线)")
    private Integer isOnline;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "lastOnlineTime" , value = "最后在线时间")
    private Date lastOnlineTime;

    @ApiModelProperty(name = "cabinetId" , value = "配电柜ID")
    private Integer cabinetId;

    @ApiModelProperty(name = "cabinetName" , value = "配电柜名称")
    private String cabinetName;

    @ApiModelProperty(name = "areaId" , value = "区域ID")
    private Integer areaId;

    @ApiModelProperty(name = "areaName" , value = "区域名称")
    private String areaName;

    @ApiModelProperty(name = "ip" , value = "ip")
    private String ip;

    @ApiModelProperty(name = "port" , value = "端口")
    private String port;

    @ApiModelProperty(name = "mac" , value = "mac地址")
    private String mac;
}
