package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 素材库查询对象
 *
 * @author Longshuangyang
 * @date 2019/8/7
 */
@Setter
@Getter
@ToString
public class IrMaterialQueryObject extends QueryObject {

    /**
     * 素材名称
     */
    @ApiModelProperty(name = "name" , value = "素材名称")
    private String name;

    /**
     * 素材类型
     */
    @ApiModelProperty(name = "type" , value = "素材类型")
    private String type;

    /**
     * 素材创建人名称
     */
    @ApiModelProperty(name = "creatorName" , value = "素材创建人名称")
    private String creatorName;

    /**
     * 素材创建时间，开始时间
     */
    @ApiModelProperty(name = "startTime" , value = "素材创建时间，开始时间")
    private String startTime;

    /**
     * 素材创建时间，结束时间
     */
    @ApiModelProperty(name = "endTime" , value = "素材创建时间，结束时间")
    private String endTime;
    
    @ApiModelProperty(value = "区域id")
	private Integer areaId;
}
