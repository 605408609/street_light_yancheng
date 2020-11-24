package com.exc.street.light.sl.VO;

import lombok.Data;

/**
 * 设备告警信息
 */
@Data
public class AlarmInfoDTO {

    //告警级别 
    private String alarmSeverity;
    //告警状态 
    private boolean alarmStatus;
    //告警上报时间 
    private String alarmTime;
}
