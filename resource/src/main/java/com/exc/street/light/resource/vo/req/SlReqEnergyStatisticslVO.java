package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 路灯控制接收参数
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class SlReqEnergyStatisticslVO {

    /**
     * 下发数据模式
     */
    @ApiModelProperty(name = "startDate", value = "开始日期")
    private String startDate;
    /**
     * 下发数据模式
     */
    @ApiModelProperty(name = "endDate", value = "结束日期")
    private String endDate;

    /**
     * 站点id集合
     */
    @ApiModelProperty(name = "siteIdList", value = "站点id集合")
    private List<Integer> siteIdList;

    /**
     * 分组id集合
     */
    @ApiModelProperty(name = "groupIdList", value = "分组id集合")
    private List<Integer> groupIdList;

    /**
     * 分组id集合
     */
    @ApiModelProperty(name = "deviceIdList", value = "设备id集合")
    private List<Integer> deviceIdList;

}