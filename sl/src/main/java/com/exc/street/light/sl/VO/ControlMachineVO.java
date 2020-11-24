package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class ControlMachineVO {


    private Integer id;
    //控制器运行时间
    private Integer runningMinute;
    //控制器重启次数
    private Integer restartNum;
    //有功电能
    private Double activeElectricEnergy;
    //无功电能
    private Double reactiveElectricEnergy;
    //视在电能
    private Double apparentElectricEnergy;
}
