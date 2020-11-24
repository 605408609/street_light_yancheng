package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公共广播节目素材查询对象
 *
 * @author LeiJing
 * @Date 2020/04/26
 */
@Data
public class PbRadioMaterialQueryObject extends PageParam {

    /**
     * 素材名称
     */
    @ApiModelProperty(name = "name" , value = "素材名称")
    private String name;

    /**
     * 素材名称
     */
    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;
}
