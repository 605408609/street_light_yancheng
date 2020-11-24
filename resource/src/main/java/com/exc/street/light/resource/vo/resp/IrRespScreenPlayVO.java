package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 播放中节目返回对象
 *
 * @author Longshuangyang
 * @date 2020/04/01
 */
@Setter
@Getter
@ToString
public class IrRespScreenPlayVO {

    @ApiModelProperty(name = "id" , value = "播放id")
    private Integer id;

    @ApiModelProperty(name = "playStatus" , value = "播放状态（0：待播放，1：正在播放，2：结束播放）")
    private Integer playStatus;

    @ApiModelProperty(name = "programId" , value = "节目id")
    private Integer programId;

    @ApiModelProperty(name = "programName" , value = "节目名称")
    private String programName;

    @ApiModelProperty(name = "startDate" , value = "开始时间（年月日）")
    private String startDate;

    @ApiModelProperty(name = "endDate" , value = "结束时间（年月日）")
    private String endDate;

    @ApiModelProperty(name = "executionEndTime" , value = "执行结束时间（时分秒）")
    private String executionEndTime;

    @ApiModelProperty(name = "executionStartTime" , value = "执行开始时间（时分秒）")
    private String executionStartTime;

    @ApiModelProperty(name = "executionMode" , value = "执行模式(1:每天执行,2:每周执行)")
    private Integer executionMode;

    @ApiModelProperty(name = "sdName" , value = "显示屏名字")
    private String sdName;

    @ApiModelProperty(name = "slpName" , value = "灯杆的名字")
    private String slpName;


}
