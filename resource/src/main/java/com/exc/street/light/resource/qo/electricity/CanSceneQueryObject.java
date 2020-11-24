package com.exc.street.light.resource.qo.electricity;

import com.exc.street.light.resource.qo.QueryObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 强电分组查询对象
 *
 * @author Linshiwen
 * @date 2018/5/11
 */
@Setter
@Getter
@ToString
public class CanSceneQueryObject extends QueryObject {
    /**
     * 名称
     */
    private String name;

    @ApiModelProperty(name = "areaId",value = "区域id")
    private Integer areaId;
}
