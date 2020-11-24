package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Auther: Xiezhipeng
 * @Description: 场景动作返回视图类
 * @Date: 2020/11/9 10:19
 */
@Data
public class DlmRespSceneActionVO {

    @ApiModelProperty(name = "id" , value = "回路场景动作表，主键id自增")
    private Integer id;

    @ApiModelProperty(name = "isOpen" , value = "场景动作（0：关 1：开）")
    private Integer isOpen;

    @ApiModelProperty(name = "executionTime" , value = "执行时间（时分秒）")
    private String executionTime;

    @ApiModelProperty(name = "startDate" , value = "开始时间（年月日）")
    private String startDate;

    @ApiModelProperty(name = "endDate" , value = "结束时间（年月日）")
    private String endDate;

    @ApiModelProperty(name = "cycleTypes", value = "周期类型(1:星期天 2:星期一 3:星期二 4:星期三 5:星期四 6:星期五 7:星期六)")
    private Integer[] cycleTypes;

    @ApiModelProperty(name = "strategyId" , value = "策略id")
    private Integer strategyId;

    @ApiModelProperty(name = "openModeId" , value = "开关方式id(经纬度)")
    private Integer openModeId;

    @ApiModelProperty(name = "deviation" , value = "偏移值（分钟）")
    private Integer deviation;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

}
