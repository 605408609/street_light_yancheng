package com.exc.street.light.resource.vo.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 节点在线率
 *
 * @author Linshiwen
 * @date 2018/7/3
 */
@Setter
@Getter
@ToString
public class OffLineVO {
    private Integer nodeNum;
    private Integer offLineNum;
    private Integer offLineRate;
}
