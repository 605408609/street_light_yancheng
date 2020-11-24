package com.exc.street.light.resource.vo.resp;

import com.exc.street.light.resource.entity.dlm.SlLampPost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 告警自动生成工单设置返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespAlarmOrderSetDetailsVO {

    @ApiModelProperty(name = "setId" , value = "自动生成工单设置id")
    private Integer setId;

    @ApiModelProperty(name = "lampPostList" , value = "灯杆集合")
    private List<SlLampPost> lampPostList;

    @ApiModelProperty(name = "user" , value = "处理人")
    private UaRespSimpleUserVO user;

    @ApiModelProperty(name = "handleDuration" , value = "处理时长（单位：小时）")
    private Integer handleDuration;
}
