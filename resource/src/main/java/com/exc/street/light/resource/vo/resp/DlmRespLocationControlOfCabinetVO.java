package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 配电柜详情下的集中控制器对象
 * @Date 2020/8/22
 */
@Data
public class DlmRespLocationControlOfCabinetVO {

    @ApiModelProperty(name = "controlId" , value = "集中控制器ID")
    private Integer controlId;

    @ApiModelProperty(name = "controlName" , value = "集中控制器名称")
    private String controlName;

    @ApiModelProperty(name = "location" , value = "地址")
    private String location;

    @ApiModelProperty(name = "controlTypeId" , value = "集中控制器类型ID")
    private Integer controlTypeId;

    @ApiModelProperty(name = "controlTypeName" , value = "集中控制器类型名称")
    private String controlTypeName;
}
