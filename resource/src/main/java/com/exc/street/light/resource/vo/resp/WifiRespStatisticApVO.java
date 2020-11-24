package com.exc.street.light.resource.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description wifi统计报表返回对象
 * @Date 2020/4/25
 */
@Data
public class WifiRespStatisticApVO {

    /**
     * 当前用户数
     */
    private Integer currentUserCount = 0;

    /**
     * 峰值用户数
     */
    private Integer maxUserCount = 0;

    /**
     * 累计用户数
     */
    private Integer sumUserCount = 0;

    /**
     * 上行流量
     */
    private List<Integer> upFlowList;

    /**
     * 下行流量
     */
    private List<Integer> downFlowList;

    /**
     * 当前用户数集合,用户app端人流量监测
     */
    private List<Integer> currentUserList;

    /**
     * ap在线数，app端使用
     */
    private Integer apOnlineCount;

    /**
     * ap离线数，app端使用
     */
    private Integer apOfflineCount;

}
