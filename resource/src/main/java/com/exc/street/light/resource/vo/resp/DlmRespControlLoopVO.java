package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Xiezhipeng
 * @Description 集控分组或回路返回对象
 * @Date 2020/8/24
 */
@Data
public class DlmRespControlLoopVO {

    @ApiModelProperty(name = "id" , value = "集中控制器回路（分组）ID")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "编号")
    private String num;

    @ApiModelProperty(name = "isOpen" , value = "回路开关状态（0：关，1：开）")
    private Integer isOpen;

    @ApiModelProperty(name = "sceneStrategyId" , value = "场景策略id")
    private Integer sceneStrategyId;

    @ApiModelProperty(name = "creator" , value = "创建人")
    private Integer creator;

    @ApiModelProperty(name = "creatorName" , value = "创建人名称")
    private String creatorName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "description" , value = "集中控制器回路描述")
    private String description;

    @ApiModelProperty(name = "controlId" , value = "集中控制器id")
    private Integer controlId;

    @ApiModelProperty(name = "controlName" , value = "集中控制器名称")
    private String controlName;

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "areaName" , value = "区域名称")
    private String areaName;

    @ApiModelProperty(name = "sn" , value = "序列号（通信地址）")
    private String sn;

    @ApiModelProperty(name = "orders" , value = "序号")
    private Integer orders;

    @ApiModelProperty(name = "isUse" , value = "是否启用场景")
    private Integer isUse;

}
