package com.exc.street.light.resource.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.exc.street.light.resource.entity.ua.Role;
import com.exc.street.light.resource.vo.resp.DlmRespProjectPicVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author Xiezhipeng
 * @Description
 * @Date 2020/3/13
 */
@Data
public class UaUserVO {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "主键id")
    private Integer id;

    @ApiModelProperty(name = "accountName", value = "账号名称")
    private String accountName;

    @ApiModelProperty(name = "name", value = "姓名")
    private String name;

    @ApiModelProperty(name = "gender", value = "性别(0:男 1:女)")
    private Integer gender;

    @ApiModelProperty(name = "phone", value = "电话号码")
    private String phone;

    @ApiModelProperty(name = "founderId", value = "创建人Id")
    private Integer founderId;

    @ApiModelProperty(name = "founderName", value = "创建人名称")
    private String founderName;

    @ApiModelProperty(name = "type", value = "用户类型（用户类型 1:超级管理员 2:其他）")
    private Integer type;

    @ApiModelProperty(name = "areaId", value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "areaName", value = "区域名称")
    private String areaName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "onlineTime", value = "在线离线时间")
    private Date onlineTime;

    @ApiModelProperty(name = "periodType", value = "有效期类型（1：永久有效 2：设定有效期）")
    private Integer periodType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(name = "validityPeriod", value = "有效期")
    private Date validityPeriod;

    @ApiModelProperty(name = "online", value = "是否在线(0：在线 1：离线)")
    private Integer online;

    @ApiModelProperty(name = "forbidden", value = "是否禁用（0：禁用 1：启用）")
    private Integer forbidden;

    @ApiModelProperty(name = "roleName", value = "角色名称")
    private String roleName;

    @ApiModelProperty(name = "roles", value = "角色集合")
    private List<Role> roles;

    @ApiModelProperty(name = "projectPicVO" , value = "项目图片")
    private DlmRespProjectPicVO projectPicVO;
}
