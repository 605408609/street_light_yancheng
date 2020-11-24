package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: XuJiaHao
 * @Description:
 * @Date: Created in 17:30 2020/5/14
 * @Modified:
 */
@Data
public class LogExceDataQueryObject extends QueryObject{

    @ApiModelProperty(name = "modul" , value = "功能模块")
    private String modul;

    @ApiModelProperty(name = "logType" , value = "操作类型")
    private String logType;

    @ApiModelProperty(name = "userName" , value = "用户姓名")
    private String userName;

    @ApiModelProperty(name = "method" , value = "操作方法")
    private String method;

    @ApiModelProperty(name = "uri" , value = "请求uri")
    private String uri;

    @ApiModelProperty(name = "requIp" , value = "操作请求ip")
    private String requIp;

    @ApiModelProperty(name = "beginTime" , value = "开始时间")
    private String beginTime;

    @ApiModelProperty(name = "endTime" , value = "结束时间")
    private String endTime;
}
