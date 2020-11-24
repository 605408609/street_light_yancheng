package com.exc.street.light.resource.dto.electricity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 定时参数
 *
 * @author LinShiWen
 * @date 2018/4/21
 */
@Setter
@Getter
@ToString
public class TimingParameter {
    /**
     * 控制对象编码
     */
    private int tagId;
    /**
     * 控制对象类型
     */
    private int tagType = 11;
    /**
     * 控制值类型
     */
    private int tagValueType = 1;
    /**
     * 控制值
     */
    private double tagValue = 1;

    /**
     * 定时值,HH:mm:ss
     */
    private String time;

    /**
     * 日出日落偏移量
     */
    private int minute;

    /**
     * 定时类型 1:定时执行 2:周期执行 3:立即执行 4：每天执行 5:日出之前 6:日落之后  7:日落之前 8:日落之后
     */
    private int type;
    /**
     * 周期类型
     */
    private int[] cycleTypes;
    /**
     * 是否执行 0-不执行，1-执行
     */
    private int isExecute;

    /**
     * 开始时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 结束时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 强电节点
     */
    private Integer nid;


    /**
     * 模式定时id
     */
    private Integer pid;
}
