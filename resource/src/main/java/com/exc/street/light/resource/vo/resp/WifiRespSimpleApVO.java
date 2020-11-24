package com.exc.street.light.resource.vo.resp;

import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 简单AP返回对象类，用于app上ap设备的信息
 * @Date 2020/5/20
 */
@Data
public class WifiRespSimpleApVO {

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备编号
     */
    private String num;

    /**
     * 网络状态
     */
    private Integer networkState;

    /**
     * 归属灯杆id
     */
    private Integer lampPostId;

    /**
     * 归属灯杆名称
     */
    private String lampPostName;

    /**
     * 区域id
     */
    private Integer areaId;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 街道id
     */
    private Integer streetId;

    /**
     * 街道名称
     */
    private String streetName;

    /**
     * 站点id
     */
    private Integer siteId;

    /**
     * 站点名称
     */
    private String siteName;

    /**
     * 安装位置
     */
    private String location;
}
