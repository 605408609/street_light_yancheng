package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: Xiezhipeng
 * @Description: 场景策略查询类
 * @Date: 2020/11/7 15:10
 */
@Data
public class DlmSceneStrategyQuery extends PageParam{

    @ApiModelProperty(name = "name" , value = "场景策略名称")
    private String name;

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

}
