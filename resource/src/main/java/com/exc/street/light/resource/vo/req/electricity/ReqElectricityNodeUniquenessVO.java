package com.exc.street.light.resource.vo.req.electricity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 路灯网关唯一校验类
 * @Author: Xiaok
 * @Date: 2020/11/16 11:36
 */
@Data
@ApiModel(description = "路灯网关唯一校验类")
public class ReqElectricityNodeUniquenessVO {

    @ApiModelProperty(name = "id",value = "id")
    private Integer id;

    @ApiModelProperty(name = "num",value = "网关编号")
    private String num;

    @ApiModelProperty(name = "name",value = "网关名称")
    private String name;

    @ApiModelProperty(name = "mac",value = "MAC地址")
    private String mac;

}
