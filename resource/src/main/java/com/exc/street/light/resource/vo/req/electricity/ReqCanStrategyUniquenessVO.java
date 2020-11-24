package com.exc.street.light.resource.vo.req.electricity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 路灯网关场景策略唯一性校验vo类
 *
 * @Author: Xiaok
 * @Date: 2020/11/24 14:37
 */
@Data
@ApiModel(value = "ReqCanStrategyUniquenessVO", description = "路灯网关场景策略唯一性校验vo类")
public class ReqCanStrategyUniquenessVO {

    @ApiModelProperty(name = "id", value = "策略ID")
    private Integer id;

    @ApiModelProperty(name = "name", value = "策略名称")
    private String name;
}
