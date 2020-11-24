package com.exc.street.light.resource.vo.req.electricity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 路灯网关回路控制vo类
 * @Author: Xiaok
 * @Date: 2020/11/18 16:54
 */
@Data
@ApiModel(value = "ReqCanChannelControlVO",description = "路灯网关回路控制vo类")
public class ReqCanChannelControlVO {

    @ApiModelProperty(name = "nodeId",value = "路灯网关设备ID")
    private Integer nodeId;

    @ApiModelProperty(name = "controlId",value = "回路编号，范围1~4")
    private Integer controlId;
}
