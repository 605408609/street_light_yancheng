package com.exc.street.light.resource.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 下发节目获取节目素材中间表参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class IrProgramMaterialVO {

    @ApiModelProperty(name = "id" , value = "节目素材中间表id，自增")
    private Integer id;

    @ApiModelProperty(name = "programId" , value = "节目id")
    private Integer programId;

    @ApiModelProperty(name = "materialId" , value = "素材id")
    private Integer materialId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "playTime" , value = "在时间轴上的相对起始播放时间 单位秒")
    private Integer playTime;

    @ApiModelProperty(name = "timeSpan" , value = "持续时长 单位秒")
    private Integer timeSpan;

    @ApiModelProperty(name = "irMaterialVO" , value = "素材对象")
    private IrMaterialVO irMaterialVO;
}
