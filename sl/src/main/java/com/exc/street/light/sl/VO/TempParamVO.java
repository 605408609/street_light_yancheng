package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class TempParamVO {
    private Integer id;
    //当前亮度（%）
    private int brightness;
    //当前状态（00开灯，01关灯）
    private String state;
    //电压值（V）
    private Double voltage;
    //电流值（A）
    private Double current;
    //有功功率（W）
    private Double activePower;
    //无功功率（W）
    private Double reactivePower;
    //频率值（Hz）
    private Double frequency;
    //温度值（摄氏度）
    private Double temperature;
}
