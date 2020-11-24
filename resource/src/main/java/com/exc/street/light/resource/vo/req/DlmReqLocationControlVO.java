package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 集中控制器请求对象
 * @Date 2020/8/24
 */
@Data
public class DlmReqLocationControlVO {

    @ApiModelProperty(name = "id" , value = "集中控制器ID")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "编号")
    private String num;

    @ApiModelProperty(name = "location" , value = "地址")
    private String location;

    @ApiModelProperty(name = "typeId" , value = "集中控制器类型id")
    private Integer typeId;

    @ApiModelProperty(name = "cabinetId" , value = "配电柜id")
    private Integer cabinetId;

    @ApiModelProperty(name = "ip" , value = "ip")
    private String ip;

    @ApiModelProperty(name = "port" , value = "端口")
    private String port;

    @ApiModelProperty(name = "mac" , value = "mac地址")
    private String mac;

}
