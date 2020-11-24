package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 策略返回参数
 *
 * @author xiezhipeng
 * @date 2020/08/28
 */
@Setter
@Getter
@ToString
public class SlRespStrategyVO {

    @ApiModelProperty(name = "id" , value = "策略id")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "策略名称")
    private String name;

    @ApiModelProperty(name = "strategyTypeId" , value = "策略类型id")
    private Integer strategyTypeId;

    @ApiModelProperty(name = "strategyTypeName" , value = "策略类型名称")
    private String strategyTypeName;

    @ApiModelProperty(name = "startDate" , value = "开始日期")
    private String startDate;

    @ApiModelProperty(name = "endDate" , value = "结束日期")
    private String endDate;

    @ApiModelProperty(name = "description" , value = "策略描述")
    private String description;

    @ApiModelProperty(name = "creator" , value = "创建人id")
    private Integer creator;

    @ApiModelProperty(name = "creatorName" , value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(name = "idSynchro" , value = "是否同步（0：否，1：是）")
    private Integer idSynchro;

    @ApiModelProperty(name = "deviceTypeIdList" , value = "设备类型id集合")
    private List<Integer> deviceTypeIdList;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "slRespStrategyActionVOList" , value = "策略动作集合")
    private List<SlRespStrategyActionVO> slRespStrategyActionVOList;

}
