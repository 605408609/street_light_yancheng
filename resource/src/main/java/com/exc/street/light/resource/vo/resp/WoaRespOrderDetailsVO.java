package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 工单详情返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespOrderDetailsVO {

    @ApiModelProperty(name = "woaRespOrderVO" , value = "工单对象")
    private WoaRespOrderVO woaRespOrderVO;

    @ApiModelProperty(name = "woaRespOrderPicVOList" , value = "工单图片对象集合")
    private List<WoaRespOrderPicVO> woaRespOrderPicVOList;

    @ApiModelProperty(name = "woaRespOrderAlarmVOList" , value = "工单告警对象集合")
    private List<WoaRespOrderAlarmVO> woaRespOrderAlarmVOList;

    @ApiModelProperty(name = "woaRespOrderProcessVOList" , value = "工单进程对象集合")
    private List<WoaRespOrderProcessVO> woaRespOrderProcessVOList;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "currentTime", value = "服务当前时间")
    private Date currentTime;
}
