package com.exc.street.light.resource.vo.req.electricity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 网关场景 新增/修改vo类
 * @Author: Xiaok
 * @Date: 2020/11/18 9:48
 */
@Data
@ApiModel("网关场景 新增/修改vo类")
public class ReqCanStrategyVO {

    @ApiModelProperty(name = "id",value = "场景id")
    private Integer id;

    @ApiModelProperty(name = "name",value = "场景名称")
    private String name;

    @ApiModelProperty(name = "name",value = "动作列表")
    private List<ReqCanStrategyActionVO> actionList;
}
