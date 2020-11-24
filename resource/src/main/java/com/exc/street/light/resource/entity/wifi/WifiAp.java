/**
 * @filename:WifiAp 2020-03-12
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.resource.entity.wifi;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: ap设备实体类
 * @version: V1.0
 * @author: xiezhipeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WifiAp extends Model<WifiAp> {

    private static final long serialVersionUID = 1583992716976L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "ap设备id")
    private Integer id;

    @ApiModelProperty(name = "count", value = "连接次数")
    private Integer count;

    @ApiModelProperty(name = "population", value = "连接人数")
    private Integer population;

    @ApiModelProperty(name = "currentPopulation", value = "当前连接人数")
    private Integer currentPopulation;

    @ApiModelProperty(name = "dayConn", value = "当日入网人数")
    private Integer dayConn;

    @ApiModelProperty(name = "avgFlow", value = "人均流量")
    private Integer avgFlow;

    @ApiModelProperty(name = "probability", value = "认证成功率（%）")
    private Integer probability;

    @ApiModelProperty(name = "onlineTime", value = "上网时长（min)")
    private Integer onlineTime;

    @ApiModelProperty(name = "upFlow", value = "上行流量（mb)")
    private Integer upFlow;

    @ApiModelProperty(name = "downFlow", value = "下行流量（mb)")
    private Integer downFlow;

    @ApiModelProperty(name = "netflow", value = "上网流量（mb)")
    private Integer netflow;

    @ApiModelProperty(name = "deviceId", value = "设备id")
    private Integer deviceId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
