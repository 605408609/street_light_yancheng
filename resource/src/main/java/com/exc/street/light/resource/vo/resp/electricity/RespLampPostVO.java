package com.exc.street.light.resource.vo.resp.electricity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 灯杆信息及是否绑定广播设备
 * @Author: Xiaok
 * @Date: 2020/11/16 11:10
 */
@Data
public class RespLampPostVO {

    /**
     * 灯杆id
     */
    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    /**
     * 灯杆名称
     */
    @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;

    /**
     *
     */
    @ApiModelProperty(name = "isBindGateway" , value = "灯杆是否已被路灯网关绑定,null-未绑定,1-已绑定")
    private Integer isBindGateway;
}
