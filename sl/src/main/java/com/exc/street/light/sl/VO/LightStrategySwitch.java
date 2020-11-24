package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class LightStrategySwitch {

    private Integer devtype;

    private String devaddr;

    private Integer rule_on;

    private Integer rule_gps_on;
}
