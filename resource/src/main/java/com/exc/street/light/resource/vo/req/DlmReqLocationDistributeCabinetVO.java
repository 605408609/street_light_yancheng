package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 配电柜请求对象
 * @Date 2020/8/22
 */
@Data
public class DlmReqLocationDistributeCabinetVO {

    @ApiModelProperty(name = "id" , value = "配电柜ID")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "配电柜名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "配电柜编号")
    private String num;

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "streetId" , value = "街道id")
    private Integer streetId;

    @ApiModelProperty(name = "longitude" , value = "经度")
    private Double longitude;

    @ApiModelProperty(name = "latitude" , value = "维度")
    private Double latitude;

    @ApiModelProperty(name = "location" , value = "安装位置")
    private String location;

    @ApiModelProperty(name = "description" , value = "配电柜描述")
    private String description;
}
