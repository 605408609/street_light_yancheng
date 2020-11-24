package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: Xiezhipeng
 * @Description: 场景策略下发请求对象
 * @Date: 2020/11/9 15:16
 */
@Data
public class DlmReqSceneStrategyExecuteVO {

    @ApiModelProperty(name = "strategyId", value = "策略id")
    private Integer strategyId;

    @ApiModelProperty(name = "LoopIdList", value = "回路id集合")
    private List<Integer> LoopIdList;

}
