package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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
public class SlRespLampGroupSingleVO {

    @ApiModelProperty(name = "id" , value = "分组id")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "分组名称")
    private String name;

    @ApiModelProperty(name = "singleList" , value = "灯具列表")
    private List<SlRespSingleVO> singleList;

    @ApiModelProperty(name = "partId" , value = "拼接分组id")
    private String partId;


}
