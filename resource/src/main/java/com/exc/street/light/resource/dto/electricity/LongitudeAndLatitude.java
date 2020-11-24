package com.exc.street.light.resource.dto.electricity;

import lombok.Data;

/**
 * @Author: XuJiaHao
 * @Description: 强电节点经纬度
 * @Date: Created in 15:43 2020/9/7
 * @Modified:
 */
@Data
public class LongitudeAndLatitude {
    /**
     * 节点id
     */
    private Integer nid;

    /**
     * 节点经度
     */
    private Double longitude;

    /**
     * 节点纬度
     */
    private Double latitude;
}
