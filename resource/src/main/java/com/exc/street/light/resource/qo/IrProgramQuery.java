package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 节目查询对象
 *
 * @author Longshuangyang
 * @date 2020/04/01
 */
@Setter
@Getter
@ToString
public class IrProgramQuery extends QueryObject {

    /**
     * 节目名称
     */
    @ApiModelProperty(name = "programName" , value = "节目名称")
    private String programName;

    /**
     * 素材名称
     */
    @ApiModelProperty(name = "materialName" , value = "素材名称")
    private String materialName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name = "creatorName" , value = "创建人名称")
    private String creatorName;

    /**
     * 创建时间，开始时间
     */
    @ApiModelProperty(name = "startTime" , value = "创建时间，开始时间")
    private String startTime;

    /**
     * 创建时间，结束时间
     */
    @ApiModelProperty(name = "endTime" , value = "创建时间，结束时间")
    private String endTime;

    /**
     * 分区id
     */
    @ApiModelProperty(name = "areaId" , value = "分区id")
    private Integer areaId;
}
