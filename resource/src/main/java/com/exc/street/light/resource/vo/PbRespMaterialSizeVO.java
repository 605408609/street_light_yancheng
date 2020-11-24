package com.exc.street.light.resource.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 节目大小对象
 * @Author: Xiaok
 * @Date: 2020/10/12 16:25
 */
@Data
public class PbRespMaterialSizeVO {
    @ApiModelProperty(name = "id" , value = "id")
    private Integer id;

    @ApiModelProperty(name = "duration" , value = "节目时长(s)")
    private Integer duration;

    @ApiModelProperty(name = "size" , value = "节目容量")
    private Float size;
}
