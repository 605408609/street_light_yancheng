package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 集控分组下拉框
 * @Date 2020/8/31
 */
@Data
public class DlmRespControlLoopWithOptionVO {

    @ApiModelProperty(name = "id" , value = "集控分组ID")
    private Integer id;

    @ApiModelProperty(name = "partId" , value = "区别ID")
    private String partId;

    @ApiModelProperty(name = "name" , value = "名称")
    private String name;
}
