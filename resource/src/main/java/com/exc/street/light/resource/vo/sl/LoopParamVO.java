package com.exc.street.light.resource.vo.sl;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class LoopParamVO {

    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    @ApiModelProperty(name = "lampPositionId" , value = "灯具位置id")
    private Integer lampPositionId;

    @ApiModelProperty(name = "loopNum" , value = "回路数")
    private Integer loopNum;
}
