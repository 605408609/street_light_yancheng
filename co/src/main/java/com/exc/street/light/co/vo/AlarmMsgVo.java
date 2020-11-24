package com.exc.street.light.co.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author tanhonghang
 * @create 2020/9/29 9:04
 */
@Data
public class AlarmMsgVo {

    /**
     * 设备号
     */
    private String snCode;

    /**
     * 告警时间
     */
    private Date alarmTime;

    /**
     * 告警码 1：上报  2：恢复
     */
    private int alarmCommandCode;

    /**
     *  告警类型 5:低电量   6： 井盖
     */
    private int alarmTypeCode;

}
