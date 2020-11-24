package com.exc.street.light.resource.vo.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Linshiwen
 * @date 2018/8/7
 */
@Setter
@Getter
@ToString
public class SceneVO {
    private Integer num;
    private List<Integer> tagIds;
    private Integer nid;
}
