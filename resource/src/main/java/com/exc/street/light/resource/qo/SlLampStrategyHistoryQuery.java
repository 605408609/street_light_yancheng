package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 历史策略查询条件
 * @Date 2020/10/23
 */
@Data
public class SlLampStrategyHistoryQuery extends PageParam {

    @ApiModelProperty(name = "name" , value = "策略名称")
    private String name;

    @ApiModelProperty(name = "areaId" , value = "区域ID")
    private Integer areaId;

}
