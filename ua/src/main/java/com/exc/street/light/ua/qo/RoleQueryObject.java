package com.exc.street.light.ua.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 角色查询条件
 * @Date 2020/3/18
 */
@Data
public class RoleQueryObject extends PageParam {

    @ApiModelProperty(name = "roleId", value = "角色id")
    private Integer roleId;

    @ApiModelProperty(name = "userId", value = "用户id")
    private Integer userId;

    @ApiModelProperty(name = "founderId", value = "创建人id")
    private Integer founderId;

}
