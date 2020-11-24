package com.exc.street.light.resource.vo.req.htLamp;

import com.exc.street.light.resource.enums.sl.ht.HtSingleLampBranchOutputEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: Xiaok
 * @Date: 2020/9/23 11:22
 */
@Data
@ApiModel("")
public class HtSetSingleLampOutputRequestVO {

    @ApiModelProperty("华体集中控制器ID")
    private Integer locationControlId;

    @ApiModelProperty("华体集中控制器通讯地址（编号）")
    private String locationControlAddr;

    @ApiModelProperty("单灯控制器索引集合")
    private List<Integer> nodeList;

    @ApiModelProperty("输出类型集合 长度：3  HtSingleLampBranchOutputEnum  下标0，1，2对应灯具支路1，2，3")
    private List<Integer> actList;
}
