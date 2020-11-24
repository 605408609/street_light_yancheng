package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 集控分组回路请求对象
 * @Date 2020/8/24
 */
@Data
public class DlmReqControlLoopVO {

    @ApiModelProperty(name = "id" , value = "集中控制器回路（分组）ID")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "编号")
    private String num;

    @ApiModelProperty(name = "controlId" , value = "集中控制器id")
    private Integer controlId;

    @ApiModelProperty(name = "description" , value = "集中控制器回路描述")
    private String description;

    @ApiModelProperty(name = "deviceIdList" , value = "设备id集合")
    private List<Integer> deviceIdList;

    @ApiModelProperty(name = "sn" , value = "序列号（通信地址）")
    private String sn;

    @ApiModelProperty(name = "orders" , value = "序号")
    private Integer orders;

}
