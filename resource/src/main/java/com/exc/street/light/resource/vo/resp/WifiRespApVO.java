package com.exc.street.light.resource.vo.resp;

import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description wifiAp返回视图类
 * @Date 2020/3/27
 */
@Data
public class WifiRespApVO {

    /**
     * wifiAp的id
     */
    private Integer id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备mac
     */
    private String mac;

    /**
     * 安装位置
     */
    private String location;

    /**
     * 连接次数
     */
    private Integer count;

    /**
     * 连接人数
     */
    private Integer population;

    /**
     * 认证成功率
     */
    private Integer probability;

    /**
     * 上网时长
     */
    private Integer onlineTime;

    /**
     * 上网流量
     */
    private Integer netflow;
}
