package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 集中控制器分页查询对象
 * @Date 2020/8/24
 */
@Data
public class DlmLocationControlQuery extends PageParam {

    @ApiModelProperty(name = "name" , value = "名称")
    private String name;

    @ApiModelProperty(name = "isOnline" , value = "网络状态(0:离线，1:在线)")
    private Integer isOnline;

    @ApiModelProperty(name = "areaId" , value = "区域ID")
    private Integer areaId;

    /**
     * 该字段是用于区分自研集控和其他厂家的集控
     */
    @ApiModelProperty(name = "controlTypeIdList" , value = "集控类型ID集合")
    private List<Integer> controlTypeIdList;

}
