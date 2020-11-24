package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 告警类型返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespAlarmTypeVO {

    /**
     * 告警类型id
     */
    @ApiModelProperty(name = "alarmTypeId" , value = "告警类型id")
    private Integer alarmTypeId;

    /**
     * 告警类型名称
     */
    @ApiModelProperty(name = "alarmTypeName" , value = "告警类型名称")
    private String alarmTypeName;

    /**
     * 未处理完成的告警数量
     */
    @ApiModelProperty(name = "alarmNumber" , value = "未处理完成的告警数量")
    private Integer alarmNumber;

}
