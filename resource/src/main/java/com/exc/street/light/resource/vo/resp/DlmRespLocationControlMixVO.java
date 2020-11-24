package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 集控混合信息接口返回类
 * @Date 2020/9/28
 */
@Data
public class DlmRespLocationControlMixVO {

    @ApiModelProperty(name = "id", value = "集控id")
    private Integer id;

    @ApiModelProperty(name = "name", value = "集控名称")
    private String name;

    @ApiModelProperty(name = "num", value = "集控编号")
    private String num;

    @ApiModelProperty(name = "loopId", value = "分组id")
    private Integer loopId;

    @ApiModelProperty(name = "loopName", value = "分组名称")
    private String loopName;

    @ApiModelProperty(name = "loopNum", value = "分组编号")
    private String loopNum;

    @ApiModelProperty(name = "loopSn", value = "分组序列号")
    private String loopSn;

    @ApiModelProperty(name = "loopOrders", value = "分组序号")
    private Integer loopOrders;

    @ApiModelProperty(name = "deviceId", value = "设备id")
    private Integer deviceId;

    @ApiModelProperty(name = "strategyId", value = "策略id")
    private Integer strategyId;

}
