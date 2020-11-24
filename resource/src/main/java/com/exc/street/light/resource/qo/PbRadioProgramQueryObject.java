package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公共广播节目节目查询对象
 *
 * @author LeiJing
 * @Date 2020/04/26
 */
@Data
public class PbRadioProgramQueryObject extends PageParam {

    /**
     * 节目名称
     */
    @ApiModelProperty(name = "name" , value = "节目名称")
    private String name;

    /**
     * 区域id
     */
    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "verifyStatus" , value = "审核状态(0-待审核 1-审核成功 2-审核失败)")
    private Integer verifyStatus;
}
