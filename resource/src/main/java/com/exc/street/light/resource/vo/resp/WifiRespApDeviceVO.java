package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Xiezhipeng
 * @Description AP设备返回视图类
 * @Date 2020/3/23
 */
@Data
public class WifiRespApDeviceVO {

    /**
     * id
     */
    private Integer id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备编号
     */
    private String num;

    /**
     *设备型号
     */
    private String model;

    /**
     * ip地址
     */
    private String ip;

    /**
     * Mac地址
     */
    private String mac;

    /**
     * 设备厂家
     */
    private String factory;

    /**
     * 归属灯杆id
     */
    private Integer lampPostId;

    /**
     * 归属灯杆名称
     */
    private String lampPostName;

    /**
     * 网络状态
     */
    private Integer networkState;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 当前连接人数
     */
    private Integer population;

    /**
     * 最后在线时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastOnlineTime;

    /**
     * 当日入网人数
     */
    private Integer dayConn;

    /**
     * 人均流量
     */
    private Integer avgFlow;

    /**
     * 归属ac的ip地址
     */
    private String acDeviceIp;

    /**
     * 连接次数
     */
    private Integer count;

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
