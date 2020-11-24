package com.exc.street.light.resource.vo.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 强电定时列表视图
 *
 * @author Linshiwen
 * @date 2018/6/1
 */
@Setter
@Getter
@ToString
public class CanTimingVO {
    private Integer id;

    /**
     * 定时类型 1:定时执行 2:周期执行 4：一直执行 5:日出之前 6:日落之后  7:日落之前 8:日落之后
     */
    private Integer type;

    /**
     * 是否执行: 1执行 2不执行
     */
    private Integer isExecute;

    /**
     * 周期名称
     */
    private Integer[] cycleNames;

    /**
     * 开始日期
     */
    private String beginDate;

    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 场景tagId
     */
    private Integer sid;

    /**
     * 时间参数
     */
    private String time;

    /**
     * 偏移值
     */
    private Integer minute;
}