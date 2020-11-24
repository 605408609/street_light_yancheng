package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 工单查询对象
 *
 * @author Longshuangyang
 * @date 2020/03/26
 */
@Setter
@Getter
@ToString
public class WoaOrderQuery extends QueryObject{
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
     * 创建用户id
     */
    @ApiModelProperty(name = "creator", value = "创建用户id")
    private Integer creator;


    /**
     * 处理用户id
     */
    @ApiModelProperty(name = "processor", value = "处理用户id")
    private Integer processor;

    /**
     * 审核用户id
     */
    @ApiModelProperty(name = "approval", value = "审核用户id")
    private Integer approval;

    /**
     * 超时
     */
    @ApiModelProperty(name = "overtime", value = "超时")
    private Integer overtime;

    /**
     * 我的创建或者待我处理(1:创建，2：处理)
     */
    @ApiModelProperty(name = "choose", value = "我的创建或者待我处理(1:创建，2：处理)")
    private Integer choose;
}
