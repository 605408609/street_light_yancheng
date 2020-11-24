package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 集中控制器类型下拉返回对象
 * @Date 2020/8/22
 */
@Data
public class DlmRespLocationControlTypeWithOptionVO {

    @ApiModelProperty(name = "id" , value = "集中控制器类型ID")
    private Integer id;

    @ApiModelProperty(name = "type" , value = "类型")
    private String type;

}
