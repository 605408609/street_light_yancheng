package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 获取修改传感器关联显示屏显示数据设置列表返回对象
 *
 * @author Longshuangyang
 * @date 2020/11/11
 */
@Setter
@Getter
@ToString
public class IrRespScreenSubtitleEmVO {

    @ApiModelProperty(name = "id" , value = "传感器关联显示屏显示数据设置id")
    private Integer id;

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "areaName" , value = "区域名称")
    private String areaName;

    @ApiModelProperty(name = "resolvingPower" , value = "分辨率（宽*高）")
    private String resolvingPower;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "lampPostNames" , value = "灯杆名称（逗号拼接）")
    private String lampPostNames;

}
