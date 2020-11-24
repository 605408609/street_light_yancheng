package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Xiezhipeng
 * @Description 历史设备策略返回视图类
 * @Date 2020/10/17
 */
@Data
public class SlRespDeviceStrategyHistoryVO {

    @ApiModelProperty(name = "id" , value = "历史设备策略Id")
    private Integer id;

    @ApiModelProperty(name = "strategyId" , value = "策略id")
    private Integer strategyId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "isSuccess" , value = "策略是否下发成功,0: 发送中 1:重发中 2:失败 3：成功,默认为0")
    private Integer isSuccess;

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

    @ApiModelProperty(name = "creator" , value = "创建人id")
    private Integer creator;

    @ApiModelProperty(name = "creatorName" , value = "创建人名称")
    private String creatorName;

}
