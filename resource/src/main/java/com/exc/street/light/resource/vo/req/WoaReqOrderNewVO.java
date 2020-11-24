package com.exc.street.light.resource.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 新工单接受对象
 *
 * @author Longshuangyang
 * @date 2019/09/08
 */
@Getter
@Setter
@ToString
public class WoaReqOrderNewVO {

    /**
     * 工单id
     */
    @ApiModelProperty(name = "id" , value = "工单id")
    private Integer id;

    /**
     * 工单名称
     */
    @ApiModelProperty(name = "name" , value = "工单名称")
    private String name;

    /**
     * 工单地址
     */
    @ApiModelProperty(name = "addr" , value = "工单地址")
    private String addr;

    /**
     * 告警类型id
     */
    @ApiModelProperty(name = "alarmTypeId" , value = "告警类型id")
    private Integer alarmTypeId;

    /**
     * 描述
     */
    @ApiModelProperty(name = "description" , value = "描述")
    private String description;

    /**
     * 告警id集合
     */
    @ApiModelProperty(name = "alarmIdList" , value = "告警id集合")
    private List<Integer> alarmIdList;

    /**
     * 图片id集合
     */
    @ApiModelProperty(name = "imgIdList" , value = "图片id集合")
    private List<Integer> imgIdList;

    /**
     * 创建方式（1：自动生成）
     */
    @ApiModelProperty(name = "foundMode" , value = "创建方式（1：自动生成）")
    private Integer foundMode;

    /**
     * 指定完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "finishTime" , value = "指定完成时间")
    private Date finishTime;

    /**
     * 处理人
     */
    @ApiModelProperty(name = "processor" , value = "处理人")
    private Integer processor;

    /**
     * 处理人角色
     */
    @ApiModelProperty(name = "processorRole" , value = "处理人角色")
    private Integer processorRole;

    /**
     * 审核意见（0：不通过  1：通过）
     */
    @ApiModelProperty(name = "opinion" , value = "审核意见（0：不通过  1：通过）")
    private Integer opinion;

    /**
     * 灯杆id
     */
    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

}
