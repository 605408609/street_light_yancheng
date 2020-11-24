package com.exc.street.light.resource.quartz;

import com.exc.street.light.resource.entity.sl.LampDevice;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 定时参数
 *
 * @author Longshuangyang
 * @date 2020/03/24
 */
@Setter
@Getter
@ToString
public class ScheduleParam implements Serializable {
    /**
     * cron表达式
     */
    private String cron;

    /**
     * 亮度
     */
    private int lightness;

    /**
     * 任务id
     */
    private String jobKey;
    private Date endDate;
    private Date startDate;

    /**
     * 0:关 1:开
     */
    private int type;

    /**
     * 路灯设备集合
     */
    private List<LampDevice> lampDeviceList;

    /**
     * 灯杆id集合
     */
    private List<Integer> lampPostIdList;

    private HttpServletRequest httpServletRequest;

}
