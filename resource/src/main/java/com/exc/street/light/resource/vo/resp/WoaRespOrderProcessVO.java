package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单图片返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespOrderProcessVO {

    @ApiModelProperty(name = "processId", value = "工单进程id")
    private Integer processId;

    @ApiModelProperty(name = "operatorId", value = "操作人id")
    private Integer operatorId;

    @ApiModelProperty(name = "operatorName", value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(name = "description", value = "工单进程描述")
    private String description;

    @ApiModelProperty(name = "statusId", value = "工单状态id")
    private Integer statusId;

    @ApiModelProperty(name = "statusName", value = "工单状态名称")
    private String statusName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;
}