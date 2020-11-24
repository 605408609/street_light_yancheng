package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 查询对象父类
 *
 * @author Linshiwen
 * @date 2018/5/11
 */
@Setter
@Getter
@ToString
public class QueryObject {
    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum" , value = "当前页,默认1")
    private int pageNum = 1;
    /**
     * 每页显示的记录数
     */
    @ApiModelProperty(name = "pageSize" , value = "每页显示的记录数，默认10")
    private int pageSize = 10;
    /**
     * 选择阶段（1：今日，2：近一周，3：近一月）
     */
    @ApiModelProperty(name = "stage", value = "选择阶段（1：今日，2：近一周，3：近一月）")
    private Integer stage;
}
