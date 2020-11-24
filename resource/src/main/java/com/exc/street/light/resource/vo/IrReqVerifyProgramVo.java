package com.exc.street.light.resource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @Author liuquan
 * @Data 2020/11/9
 *
 */


@ApiModel("节目审核类")
@Data
public class IrReqVerifyProgramVo {

    @ApiModelProperty("节目Id")
    private Integer id;

    @ApiModelProperty("审核状态(1-审核成功,2-审核失败)")
    private Integer  verifyStatus;
}
