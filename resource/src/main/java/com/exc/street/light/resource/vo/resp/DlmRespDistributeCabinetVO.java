package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 配电柜返回视图对象
 * @Date 2020/8/22
 */
@Data
public class DlmRespDistributeCabinetVO {

    @ApiModelProperty(name = "id" , value = "配电柜ID")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "配电柜名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "配电柜编号")
    private String num;

    @ApiModelProperty(name = "state" , value = "配电柜状态（1：正常，0：异常）")
    private Integer state;

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "areaName" , value = "区域名称")
    private String areaName;

    @ApiModelProperty(name = "streetId" , value = "街道id")
    private Integer streetId;

    @ApiModelProperty(name = "streetName" , value = "街道名称")
    private String streetName;

    @ApiModelProperty(name = "longitude" , value = "经度")
    private Double longitude;

    @ApiModelProperty(name = "latitude" , value = "维度")
    private Double latitude;

    @ApiModelProperty(name = "location" , value = "安装位置")
    private String location;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "description" , value = "配电柜描述")
    private String description;

    @ApiModelProperty(name = "controlOfCabinetVOList" , value = "集中控制器信息")
    private List<DlmRespLocationControlOfCabinetVO> controlOfCabinetVOList;
}
