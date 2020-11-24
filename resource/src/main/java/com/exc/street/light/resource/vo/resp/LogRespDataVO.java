package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: XuJiaHao
 * @Description:
 * @Date: Created in 17:36 2020/5/13
 * @Modified:
 */
@Data
public class LogRespDataVO {

    @ApiModelProperty(name = "id" , value = "主键ID")
    private Long id;

    @ApiModelProperty(name = "modul" , value = "功能模块")
    private String modul;

    @ApiModelProperty(name = "logType" , value = "操作类型")
    private String logType;

    @ApiModelProperty(name = "description" , value = "操作描述")
    private String description;

    @ApiModelProperty(name = "requParam" , value = "请求参数")
    private String requParam;

    @ApiModelProperty(name = "respParam" , value = "返回参数")
    private String respParam;

    @ApiModelProperty(name = "userId" , value = "用户id")
    private Integer userId;

    @ApiModelProperty(name = "userName" , value = "用户姓名")
    private String userName;

    @ApiModelProperty(name = "method" , value = "操作方法")
    private String method;

    @ApiModelProperty(name = "uri" , value = "请求uri")
    private String uri;

    @ApiModelProperty(name = "requIp" , value = "操作请求ip")
    private String requIp;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "操作创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "操作更新时间")
    private Date updateTime;
}
