/**
 * @filename:WifiUser 2020-03-12
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
 * @Description: WiFi用户实体类
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WifiUser extends Model<WifiUser> {

    private static final long serialVersionUID = 1583992716976L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "wifi用户表id，自增")
    private Integer id;

    @ApiModelProperty(name = "userMac", value = "用户Mac")
    private String userMac;

    @ApiModelProperty(name = "phone", value = "手机号")
    private String phone;

    @ApiModelProperty(name = "terminal", value = "终端类型(android/ios/other)")
    private String terminal;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "certifTime", value = "认证时间")
    private Date certifTime;

    @ApiModelProperty(name = "certifResult", value = "认证结果(0:失败 1：成功)")
    private Integer certifResult;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "logoutTime", value = "下线时间")
    private Date logoutTime;

    @ApiModelProperty(name = "internetTime", value = "上网时长(min)")
    private Integer internetTime;

    @ApiModelProperty(name = "internetTraffic", value = "上网流量（mb)")
    private Integer internetTraffic;

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
