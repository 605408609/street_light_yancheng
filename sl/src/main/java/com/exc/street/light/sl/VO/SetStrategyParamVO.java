package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class SetStrategyParamVO {

    //策略号
    private String strategyNum;
    //使能
    private String enable;
    //执行时间一
    private String executionTimeOne;
    //执行功能一
    private String executionTypeOne;
    //执行时间二
    private String executionTimeTwo;
    //执行功能二
    private String executionTypeTwo;

}
