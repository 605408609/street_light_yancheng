package com.exc.street.light.resource.quartz;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 定时
 *
 * @author Linshiwen
 * @date 2018/10/15
 */
@Setter
@Getter
@ToString
public class TimerDTO {
    /**
     * 定时类型 1:定时指定节目执行 2:周期指定节目执行 3:手动切换
     */
    private Integer type;
    /**
     * 周期类型(1:星期天 2:星期一 3:星期二 4:星期三 5:星期四 6:星期五 7:星期六)
     */
    private Integer[] cycleTypes;

    /**
     * 开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;

    /**
     * 结束时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
}
