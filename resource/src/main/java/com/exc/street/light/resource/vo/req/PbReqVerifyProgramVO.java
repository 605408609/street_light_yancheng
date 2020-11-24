package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Xiaok
 * @Date: 2020/10/20 17:49
 */
@ApiModel("节目审核类")
@Data
public class PbReqVerifyProgramVO {

    @ApiModelProperty("节目ID")
    private Integer id;

    @ApiModelProperty("审核状态(1-审核成功 2-审核失败)")
    private Integer verifyStatus;
}
