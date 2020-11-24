package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 节目发布接收参数
 *
 * @author Longshuangyang
 * @date 2019/8/23
 */
@Setter
@Getter
@ToString
public class IrReqSendProgramVO {

    /**
     * 节目id
     */
    @ApiModelProperty(name = "programId", value = "节目id")
    private Integer programId;

    /**
     * 开始日期（年月日）
     */
    @ApiModelProperty(name = "startDate", value = "开始日期（年月日）")
    private String startDate;

    /**
     * 结束日期（年月日）
     */
    @ApiModelProperty(name = "endDate", value = "结束日期（年月日）")
    private String endDate;

    /**
     * 模式: 1:周期执行 2:定时执行
     */
    @ApiModelProperty(name = "executionMode", value = "模式: 1:周期执行 2:定时执行")
    private Integer executionMode;

    /**
     * 周期类型(1:星期天 2:星期一 3:星期二 4:星期三 5:星期四 6:星期五 7:星期六)
     */
    @ApiModelProperty(name = "cycleTypes", value = "周期类型(1:星期天 2:星期一 3:星期二 4:星期三 5:星期四 6:星期五 7:星期六)")
    private Integer[] cycleTypes;

    /**
     * 执行开始时间（时分秒）
     */
    @ApiModelProperty(name = "executionStartTime", value = "执行开始时间（时分秒）")
    private String executionStartTime;

    /**
     * 执行结束时间（时分秒）
     */
    @ApiModelProperty(name = "executionEndTime", value = "执行结束时间（时分秒）")
    private String executionEndTime;

    /**
     * 显示屏设备id集合
     */
    @ApiModelProperty(name = "screenDeviceIdList", value = "显示屏设备id集合")
    private List<Integer> screenDeviceIdList;

    /**
     * 显示屏设备id拼接device集合
     */
    @ApiModelProperty(name = "screenDeviceIdStringList", value = "显示屏设备id拼接device集合")
    private List<String> screenDeviceIdStringList;

    /**
     * 路灯id集合
     */
    @ApiModelProperty(name = "lampPostIdList", value = "路灯id集合")
    private List<Integer> lampPostIdList;

    /**
     * 分组id集合
     */
    @ApiModelProperty(name = "groupIdList", value = "分组id集合")
    private List<Integer> groupIdList;

    /**
     * 设备隶属于（1：区域，2：分组）
     */
    @ApiModelProperty(name = "deviceSubordinate", value = "设备隶属于（1：区域，2：分组）")
    private Integer deviceSubordinate;

}
