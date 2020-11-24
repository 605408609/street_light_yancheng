package com.exc.street.light.resource.qo.electricity;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 网关策略查询类
 * @Author: Xiaok
 * @Date: 2020/11/18 10:57
 */
@Data
@ApiModel("网关策略查询类")
public class CanStrategyQueryObject extends PageParam<CanStrategyQueryObject> {

    private static final long serialVersionUID = -7699655956884418918L;

    @ApiModelProperty("策略名称")
    private String name;

    @ApiModelProperty("区域ID")
    private Integer areaId;
}
