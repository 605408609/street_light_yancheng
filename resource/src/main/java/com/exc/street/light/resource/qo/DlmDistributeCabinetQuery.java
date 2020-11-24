package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 配电柜分页查询参数对象
 * @Date 2020/8/22
 */
@Data
public class DlmDistributeCabinetQuery extends PageParam {

    @ApiModelProperty(name = "name" , value = "配电柜名称")
    private String name;

    @ApiModelProperty(name = "state" , value = "配电柜状态（1：正常，0：异常）")
    private Integer state;

    @ApiModelProperty(name = "areaId" , value = "区域ID")
    private Integer areaId;

    @ApiModelProperty(name = "streetId" , value = "街道ID")
    private Integer streetId;

}
