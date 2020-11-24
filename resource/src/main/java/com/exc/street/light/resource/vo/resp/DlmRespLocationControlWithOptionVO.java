package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 集中控制器下拉列表对象
 * @Date 2020/8/24
 */
@Data
public class DlmRespLocationControlWithOptionVO {

    @ApiModelProperty(name = "id" , value = "集中控制器ID")
    private Integer id;

    @ApiModelProperty(name = "partId" , value = "区别ID")
    private String partId;

    @ApiModelProperty(name = "name" , value = "名称")
    private String name;

    @ApiModelProperty(name = "controlLoopVoList" , value = "分组对象集合")
    private List<DlmRespControlLoopWithOptionVO> childrenList;

}
