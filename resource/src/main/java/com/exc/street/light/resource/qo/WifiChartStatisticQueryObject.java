package com.exc.street.light.resource.qo;

import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 报表统计接口查询参数
 * @Date 2020/4/24
 */
@Data
public class WifiChartStatisticQueryObject {

    /**
     * 查询日期,格式：yyyy-MM-dd
     */
    private String queryDate;

    /**
     * 分区id
     */
    private Integer areaId;

    /**
     * 街道id
     */
    private Integer streetId;

    /**
     * 站点id
     */
    private Integer siteId;

    /**
     * 站点id集合
     */
    private List<Integer> siteIdList;

    /**
     * ap设备的id,用于单个ap设备的查询
     */
    private Integer apDeviceId;

}
