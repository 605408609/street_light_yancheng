package com.exc.street.light.resource.vo.req.electricity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 网关策略下发vo
 *
 * @Author: Xiaok
 * @Date: 2020/11/18 19:32
 */
@Data
@ApiModel(value = "ReqCanStrategyIssueVO",description = "网关策略下发vo")
public class ReqCanStrategyIssueVO {

    @ApiModelProperty(name = "strategyId", value = "策略ID")
    private Integer strategyId;

    @ApiModelProperty(name = "controlVOList", value = "回路信息集合")
    private List<ReqCanChannelControlVO> controlVOList;
}
