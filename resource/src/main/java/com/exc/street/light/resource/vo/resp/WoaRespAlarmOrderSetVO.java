package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 告警自动生成工单设置返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespAlarmOrderSetVO {

    @ApiModelProperty(name = "id" , value = "告警生成工单设置表id")
    private Integer id;

    @ApiModelProperty(name = "lampPostNames" , value = "灯杆名称集")
    private String lampPostNames;

    @ApiModelProperty(name = "processor" , value = "处理人")
    private String processor;

    @ApiModelProperty(name = "status" , value = "自动生成工单状态（0：关闭，1：开启）")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

}
