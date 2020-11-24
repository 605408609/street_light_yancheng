package com.exc.street.light.resource.vo.req.htLamp;

import com.exc.street.light.resource.enums.sl.ht.HtSingleLampBranchOutputEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 华体-单灯控制器计划
 * @Author: Xiaok
 * @Date: 2020/8/13 17:55
 */
@Getter
@Setter
@ToString
public class HtSingleLampPlanRequestVO {


    /**
     * 单灯控制器序号
     */
    private List<Integer> node;

    /**
     * 具体控制动作
     */
    private List<HtSingleLampPlanChildRequestVo> plan;
}
