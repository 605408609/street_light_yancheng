package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespOrderVO {

    @ApiModelProperty(name = "orderId" , value = "工单id")
    private Integer orderId;

    @ApiModelProperty(name = "orderName" , value = "工单名称")
    private String orderName;

    @ApiModelProperty(name = "alarmTypeId" , value = "告警类型id")
    private Integer alarmTypeId;

    @ApiModelProperty(name = "alarmTypeName" , value = "告警类型名称")
    private String alarmTypeName;

    @ApiModelProperty(name = "orderAddr" , value = "工单发生地址")
    private String orderAddr;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "creatorId" , value = "创建人id")
    private Integer creatorId;

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

    @ApiModelProperty(name = "processorRoleId" , value = "处理角色id")
    private Integer processorRoleId;

    @ApiModelProperty(name = "processorRoleName" , value = "处理角色名称")
    private String processorRoleName;

    @ApiModelProperty(name = "description" , value = "工单描述")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "finishTime" , value = "指定处理完成时间")
    private Date finishTime;

    @ApiModelProperty(name = "overtime" , value = "是否已超时（0：否，1：是）")
    private Integer overtime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "修改时间")
    private Date updateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "修改时间")
    private Date startHandleTime;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆名称")
    private String lampPostName;

    @ApiModelProperty(name = "partId" , value = "区别id")
    private String partId;

}
