package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description
 * @Date 2020/3/18
 */
@Data
public class UaRespPermissionVO {

    @ApiModelProperty(name = "id", value = "主键id")
    private Integer id;

    @ApiModelProperty(name = "code", value = "权限名")
    private String code;

    @ApiModelProperty(name = "name", value = "权限中文名")
    private String name;

    @ApiModelProperty(name = "uri", value = "uri")
    private String uri;

    @ApiModelProperty(name = "sort", value = "种类(1:模块菜单 2:一级菜单 3:二级菜单 4:权限按钮)")
    private Integer sort;

    @ApiModelProperty(name = "parentId", value = "父类id")
    private Integer parentId;

    @ApiModelProperty(name = "type", value = "类型")
    private String type;

    @ApiModelProperty(name = "isShow" , value = "是否显示（0：隐藏 1：显示，默认显示）")
    private Integer isShow;

    @ApiModelProperty(name = "respPermissionVOList" , value = "子集集合")
    private List<UaRespPermissionVO> respPermissionVOList;
}
