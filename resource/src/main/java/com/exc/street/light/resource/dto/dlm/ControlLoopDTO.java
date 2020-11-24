package com.exc.street.light.resource.dto.dlm;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Auther: Xiezhipeng
 * @Description: 集控回路传输对象
 * @Date: 2020/11/7 14:49
 */
@Data
public class ControlLoopDTO {

    @ApiModelProperty(name = "id" , value = "集中控制器回路（分组）表")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "编号")
    private String num;

    @ApiModelProperty(name = "controlId" , value = "集中控制器id")
    private Integer controlId;

    @ApiModelProperty(name = "controlName" , value = "集控控制器名称")
    private String controlName;

    @ApiModelProperty(name = "ip" , value = "ip")
    private String ip;

    @ApiModelProperty(name = "port" , value = "端口")
    private String port;

    @ApiModelProperty(name = "mac" , value = "mac地址")
    private String mac;

}
