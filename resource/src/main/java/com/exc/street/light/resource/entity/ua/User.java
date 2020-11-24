/**
 * @filename:User 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.resource.entity.ua;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description:TODO(实体类)
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> {

    private static final long serialVersionUID = 1584013158613L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "主键id")
    private Integer id;

    @ApiModelProperty(name = "accountName", value = "账号名称")
    private String accountName;

    @ApiModelProperty(name = "name", value = "姓名")
    private String name;

    @ApiModelProperty(name = "password", value = "密码")
    private String password;

    @ApiModelProperty(name = "gender", value = "性别(0:男 1:女)")
    private Integer gender;

    @ApiModelProperty(name = "phone", value = "电话号码")
    private String phone;

    @ApiModelProperty(name = "email", value = "邮箱")
    @ApiParam(hidden = true)
    private String email;

    @ApiModelProperty(name = "status", value = "是否有效   1:有效  2:无效  3:已过期")
    @ApiParam(hidden = true)
    private Integer status;

    @ApiModelProperty(name = "founderId", value = "创建人id")
    private Integer founderId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @ApiParam(hidden = true)
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @ApiParam(hidden = true)
    private Date updateTime;

    @ApiModelProperty(name = "type", value = "用户类型 1:超级管理员 2:其他")
    @ApiParam(hidden = true)
    private Integer type;

    @ApiModelProperty(name = "areaId", value = "区域id")
    private Integer areaId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(name = "validityPeriod", value = "有效期")
    private Date validityPeriod;

    @ApiModelProperty(name = "online", value = "是否在线 0：在线 1：离线")
    @ApiParam(hidden = true)
    private Integer online;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "onlineTime", value = "在线离线时间")
    @ApiParam(hidden = true)
    private Date onlineTime;

    @ApiModelProperty(name = "periodType", value = "有效期类型（1：永久有效 2：设定有效期）")
    private Integer periodType;

    @ApiModelProperty(name = "failCount", value = "连续登录失败次数，5次冻结")
    @ApiParam(hidden = true)
    private Integer failCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "frozenTime", value = "解冻时间")
    @ApiParam(hidden = true)
    private Date frozenTime;

    @ApiModelProperty(name = "forbidden", value = "是否禁用（0：禁用 1：启用）")
    @ApiParam(hidden = true)
    private Integer forbidden;

    @TableField(exist = false)
    @ApiModelProperty(name = "roleIds", value = "角色id")
    private List<Integer> roleIds;

    @ApiModelProperty(name = "picId" , value = "图片id")
    private transient Integer picId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
