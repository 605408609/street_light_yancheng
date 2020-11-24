package com.exc.street.light.resource.vo.req;

import com.exc.street.light.resource.vo.resp.UaRespPermissionVO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description
 * @Date 2020/3/18
 */
@Data
public class UaReqRoleVO {

    @ApiModelProperty(name = "id" , value = "主键id")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "角色名称")
    private String name;

    @ApiModelProperty(name = "founderId" , value = "创建人id")
    @ApiParam(hidden = true)
    private Integer founderId;

    @ApiModelProperty(name = "permissionIdList" , value = "权限id集合")
    private List<Integer> permissionIdList;

    @ApiModelProperty(name = "respPermissionVOList" , value = "权限树状数据")
    @ApiParam(hidden = true)
    private List<UaRespPermissionVO> respPermissionVOList;

}
