package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 新状态工单查询对象
 *
 * @author Longshuangyang
 * @date 2020/03/26
 */
@Setter
@Getter
@ToString
public class WoaReqOrderNewStatusVO {

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
     * 工单处于新状态，未浏览（0：否，1：是）
     */
    @ApiModelProperty(name = "newStatus", value = "工单处于新状态，未浏览（0：否，1：是）")
    private Integer newStatus;

}
