package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 工单列表接收参数
 *
 * @author Longshuangyang
 * @date 2020/03/23
 */
@Setter
@Getter
@ToString
public class WoaReqOrderListVO {

    /**
     * 工单名称
     */
    @ApiModelProperty(name = "orderName", value = "工单名称")
    private String orderName;

    /**
     * 告警类型
     */
    @ApiModelProperty(name = "alarmTypeId", value = "告警类型")
    private Integer alarmTypeId;

    /**
     * 工单状态
     */
    @ApiModelProperty(name = "orderStatus", value = "工单状态")
    private Integer orderStatus;

    /**
     * 查询的用户id
     */
    @ApiModelProperty(name = "userId", value = "查询用户id")
    private Integer userId;

}
