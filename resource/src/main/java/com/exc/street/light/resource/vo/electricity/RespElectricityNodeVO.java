package com.exc.street.light.resource.vo.electricity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 路灯网关返回vo类
 * @Author: Xiaok
 * @Date: 2020/11/18 16:43
 */
@Data
@ApiModel("路灯网关返回vo类")
public class RespElectricityNodeVO {

    @ApiModelProperty(name = "id",value = "网关ID")
    private Integer id;

    @ApiModelProperty(name = "name",value = "网关名称")
    private String name;
}
