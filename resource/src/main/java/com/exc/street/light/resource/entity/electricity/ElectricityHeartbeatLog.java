package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 强电心跳包实体类
 *
 * @author Linshiwen
 * @date 2019/1/9
 */
@Setter
@Getter
@TableName(value = "electricity_heartbeat_log")
public class ElectricityHeartbeatLog extends Model<ElectricityHeartbeatLog> implements Serializable {
    private static final long serialVersionUID = 6522437942944029627L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Long id;

    /**
     * 协议内容
     */
    @ApiModelProperty(name = "data", value = "协议内容")
    private String data;

    /**
     * ip
     */
    @ApiModelProperty(name = "ip", value = "ip")
    private String ip;

    @TableField(value = "node_time")
    @ApiModelProperty(name = "nodeTime", value = "网关时间")
    private Date nodeTime;

    @TableField(value = "record_time")
    @ApiModelProperty(name = "recordTime", value = "心跳时间")
    private Date recordTime;

    @ApiModelProperty(name = "nid", value = "网关id")
    private Integer nid;

}