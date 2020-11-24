package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 集控分组详情VO
 * @Date 2020/8/26
 */
@Data
public class DlmRespControlLoopDetailVO {

    @ApiModelProperty(name = "id" , value = "分组id")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "编号")
    private String num;

    @ApiModelProperty(name = "controlId" , value = "集中控制器id")
    private Integer controlId;

    @ApiModelProperty(name = "controlName" , value = "集中控制器名称")
    private String controlName;

    @ApiModelProperty(name = "description" , value = "集中控制器回路描述")
    private String description;

    @ApiModelProperty(name = "deviceIdList" , value = "设备id集合")
    private List<Integer> deviceIdList;

    @ApiModelProperty(name = "deviceNameList" , value = "设备名称集合")
    private List<String> deviceNameList;

    @ApiModelProperty(name = "sn" , value = "序列号（通信地址）")
    private String sn;

    @ApiModelProperty(name = "orders" , value = "序号")
    private Integer orders;

}
