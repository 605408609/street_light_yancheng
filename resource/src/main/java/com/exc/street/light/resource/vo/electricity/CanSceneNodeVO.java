package com.exc.street.light.resource.vo.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 场景查询节点列表视图
 *
 * @author Also
 */
@Setter
@Getter
@ToString
public class CanSceneNodeVO {

    private String name;
    /**
     * 节点数
     */
    private Integer nodeCount;

}
