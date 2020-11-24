package com.exc.street.light.resource.dto.sl.shuncom;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 顺舟云盒 策略信息
 *
 * @Author: Xiaok
 * @Date: 2020/8/25 16:51
 */
@Getter
@Setter
@ToString
public class StrategyInfoDTO {

    /**
     * 策略 ID  非空
     */
    private Integer rid;

    /**
     * 策略名  非空
     */
    private String name;

    /**
     * 策略使能（非空）：  0：disable 1：enable
     */
    private Integer state;

    /**
     * 策略计划被触发的次数（非空）；0 表示持续执行
     */
    @JSONField(name = "trig")
    private Integer triggerTimes;

    /**
     * 创建时间（非空）： xxxx-xx-xxTxx:xx:xx
     */
    @JSONField(name = "ct",format = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createTime;

    /**
     * 策略表达式（非空）（使用lua 语言描述）exp中main(a1,a2,a3,a4)a1、a3 分别为 cond中条件 1、2 字段的实际值，a2、a4 分别为条件 1、2 中的设定值。
     * 禁止使用系统调用相关接口，否则可能会导致系统崩溃
     * 1个条件使用：   function main(a1,a2) if (a1 == a2) then return true else return false end end
     * 2个条件使用：   function main(a1,a2,a3,a4) if ((a1 == a2) and (a3 == a4)) then return true else return false end end
     */
//    private String exp;

    /**
     * 条件列表
     */
    @JSONField(name = "cond")
    private List<StrategyConditionDTO> conditionList;

    /**
     * 动作列表
     */
    @JSONField(name = "act")
    private List<StrategyActDTO> actList;
}
