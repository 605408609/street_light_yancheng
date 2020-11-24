package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 灯具分组列表返回对象
 *
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SlRespSingleVO {

    @ApiModelProperty(name = "id" , value = "灯具id")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "灯具名称")
    private String name;

    @ApiModelProperty(name = "partId" , value = "拼接灯具id")
    private String partId;
}
