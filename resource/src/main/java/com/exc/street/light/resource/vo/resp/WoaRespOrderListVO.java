package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单列表返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespOrderListVO {

    @ApiModelProperty(name = "orderId" , value = "工单id")
    private Integer orderId;

    @ApiModelProperty(name = "orderName" , value = "工单名称")
    private String orderName;

    @ApiModelProperty(name = "alarmTypeId" , value = "故障id")
    private Integer alarmTypeId;

    @ApiModelProperty(name = "alarmTypeName" , value = "故障类型名称")
    private String alarmTypeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "creator" , value = "创建人id")
    private Integer creator;

    @ApiModelProperty(name = "creatorName" , value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(name = "statusId" , value = "工单状态id")
    private Integer statusId;

    @ApiModelProperty(name = "statusName" , value = "工单状态名称")
    private String statusName;

    @ApiModelProperty(name = "processorId" , value = "处理人id")
    private Integer processorId;

    @ApiModelProperty(name = "processorName" , value = "处理人名称")
    private String processorName;

    @ApiModelProperty(name = "overtime" , value = "是否已超时（0：否，1：是）")
    private Integer overtime;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆名称")
    private String lampPostName;

    @ApiModelProperty(name = "partId" , value = "区别id")
    private String partId;
}
