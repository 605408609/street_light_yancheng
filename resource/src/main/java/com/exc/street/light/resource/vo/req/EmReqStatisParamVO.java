package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 气象设备请求统计历史数据实体
 *
 * @author LeiJing
 * @Date 2019/5/20
 */
@Data
public class EmReqStatisParamVO {
    /**
     * 查询日期，格式：yyyy-MM-dd
     */
    @ApiModelProperty(name = "queryDate", value = "查询日期")
    private String queryDate;

    /**
     * 查询气象数据类型
     */
    @ApiModelProperty(name = "queryType", value = "查询气象数据类型")
    private String queryType;

    /**
     * 查询气象设备id
     */
    @ApiModelProperty(name = "deviceId", value = "查询气象设备id")
    private Integer deviceId;

    private Date beginTime;

    private Date endTime;
}
