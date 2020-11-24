package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 策略接收参数
 *
 * @author xiezhipeng
 * @date 2020/08/26
 */
@Data
public class SlReqStrategyVO {

    @ApiModelProperty(name = "id", value = "策略id")
    private Integer id;

    @ApiModelProperty(name = "name", value = "策略名称")
    private String name;

    @ApiModelProperty(name = "strategyTypeId", value = "策略类型id")
    private Integer strategyTypeId;

    @ApiModelProperty(name = "description", value = "策略描述")
    private String description;

    @ApiModelProperty(name = "deviceTypeIdList" , value = "绑定策略的设备类型id集合")
    private List<Integer> deviceTypeIdOfStrategyList;

    @ApiModelProperty(name = "idSynchro" , value = "是否同步（0：否，1：是）")
    private Integer idSynchro;

    @ApiModelProperty(name = "slReqStrategyActionVOList", value = "策略动作集合")
    private List<SlReqStrategyActionVO> slReqStrategyActionVOList;

}
