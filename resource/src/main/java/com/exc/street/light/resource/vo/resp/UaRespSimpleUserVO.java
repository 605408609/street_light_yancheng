package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 简单用户返回信息
 * @Date 2020/3/18
 */
@Data
public class UaRespSimpleUserVO {

    @ApiModelProperty(name = "id" , value = "主键id")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "用户名称")
    private String name;

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "areaUserId" , value = "区域id和用户id")
    private List<String> areaUserId;

    @ApiModelProperty(name = "roleId" , value = "角色id")
    private Integer roleId;

    @ApiModelProperty(name = "roleName" , value = "角色名称")
    private String roleName;

    @ApiModelProperty(name = "distinctId" , value = "区别id")
    private String distinctId;
}
