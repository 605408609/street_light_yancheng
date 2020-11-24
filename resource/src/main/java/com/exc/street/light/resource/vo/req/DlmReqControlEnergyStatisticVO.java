package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 集控能耗统计请求参数类
 * @Date 2020/10/28
 */
@Data
public class DlmReqControlEnergyStatisticVO {

    @ApiModelProperty(name = "startDate", value = "开始日期")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "结束日期")
    private String endDate;

    @ApiModelProperty(name = "controlIdList", value = "集控id集合")
    private List<Integer> controlIdList;

    @ApiModelProperty(name = "areaId", value = "区域id")
    private Integer areaId;

}
