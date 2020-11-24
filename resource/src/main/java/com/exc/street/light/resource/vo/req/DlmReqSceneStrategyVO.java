package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 场景策略请求对象
 * @Date 2020/11/07
 */
@Data
public class DlmReqSceneStrategyVO {

    @ApiModelProperty(name = "id" , value = "回路场景策略表，主键id自增")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "场景名称")
    private String name;

    @ApiModelProperty(name = "dlmReqSceneActionVOList", value = "场景动作集合")
    private List<DlmReqSceneActionVO> dlmReqSceneActionVOList;

}
