package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 配电柜下拉列表返回对象类
 * @Date 2020/8/22
 */
@Data
public class DlmRespDistributeCabinetWithOptionVO {

    @ApiModelProperty(name = "id" , value = "配电柜ID")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "配电柜名称")
    private String name;
}
