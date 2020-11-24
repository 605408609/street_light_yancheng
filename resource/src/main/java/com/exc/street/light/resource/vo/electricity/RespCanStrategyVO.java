package com.exc.street.light.resource.vo.electricity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: Xiaok
 * @Date: 2020/11/18 10:49
 */
@Data
public class RespCanStrategyVO {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("策略名称")
    private String name;

    @ApiModelProperty("策略开始日期")
    private String startDate;

    @ApiModelProperty("策略结束日期")
    private String endDate;

    @ApiModelProperty("创建人名称")
    private String creatorName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("策略创建时间")
    private Date createTime;

    @ApiModelProperty("策略动作集合")
    private List<RespCanStrategyActionVO> actionVOList;
}
