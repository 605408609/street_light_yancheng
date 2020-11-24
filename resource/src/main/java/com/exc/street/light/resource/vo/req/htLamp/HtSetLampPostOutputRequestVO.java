package com.exc.street.light.resource.vo.req.htLamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: Xiaok
 * @Date: 2020/9/2 19:44
 */
@Data
@ApiModel("华体集中控制器回路手动输出接受类")
public class HtSetLampPostOutputRequestVO {

    @ApiModelProperty("华体集中控制器ID")
    private Integer locationControlId;

    @ApiModelProperty("华体集中控制器通讯地址（编号）")
    private String locationControlAddr;

    @ApiModelProperty("输出类型集合 长度：4  详见HtLoopOutputTypeEnum.code()")
    private List<Integer> actList;
}
