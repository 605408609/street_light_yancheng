package com.exc.street.light.resource.vo.req.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 站点日程 强电策略定时接收对象
 *
 * @author Linshiwen
 * @date 2018/7/11
 */
@Setter
@Getter
@ToString
public class ReqElectricityScriptVO {
    /**
     * 强电场景
     */
    private List<ReqElectricitySceneVO> sceneVOS;

    /**
     * 日程模式id
     */
    private Integer scheduleModeId;
    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String stopDate;

    /**
     * 周期类型(1:星期一 2:星期二 3:星期三 4:星期四 5:星期五 6:星期六 7:星期日)
     */
    private int[] cycleTypes;

    private Integer siteScheduleId;
    private Integer partitionId;
}
