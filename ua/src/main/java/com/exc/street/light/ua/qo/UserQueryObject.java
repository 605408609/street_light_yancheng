package com.exc.street.light.ua.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description  查询条件
 * @Date 2020/3/13
 */
@Data
public class UserQueryObject extends PageParam {

    @ApiModelProperty(name = "id", value = "用户id")
    private Integer id;

    @ApiModelProperty(name = "name", value = "姓名")
    private String name;

    @ApiModelProperty(name = "phone", value = "电话号码")
    private String phone;

    @ApiModelProperty(name = "roleId", value = "角色id")
    private Integer roleId;

    @ApiModelProperty(name = "founderId", value = "创建人id")
    private Integer founderId;
}
