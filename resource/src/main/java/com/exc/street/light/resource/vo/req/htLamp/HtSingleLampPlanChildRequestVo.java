package com.exc.street.light.resource.vo.req.htLamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @Author: Xiaok
 * @Date: 2020/8/13 18:09
 */
@Getter
@Setter
@ToString
public class HtSingleLampPlanChildRequestVo {


    /**
     * 开始时间 如果开始时间和结束时间为空，则按time中的时间点来
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startDate;

    /**
     * 结束时间 如果开始时间和结束时间为空，则按time中的时间点来
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endDate;

    /**
     * 时间点,如果开始时间和结束时间为空，该属性为每一天的时间点
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    /**
     * 输出亮度列表 int[3]  HtSingleLampBranchOutputEnum.code
     */
    private List<Integer> act;
}
