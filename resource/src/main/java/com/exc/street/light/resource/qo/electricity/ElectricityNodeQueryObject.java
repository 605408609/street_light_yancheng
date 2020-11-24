package com.exc.street.light.resource.qo.electricity;

import com.exc.street.light.resource.qo.QueryObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 强电节点查询对象
 *
 * @author Linshiwen
 * @date 2018/5/11
 */
@Setter
@Getter
@ToString
public class ElectricityNodeQueryObject extends QueryObject {
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点编号
     */
    private String num;
    private Integer offline;
    /**
     * 开门检测 0:开 1:关
     */
    private Integer isOpen;

    private Integer networkType;
    private Integer routerIsOffline;

    @ApiModelProperty(name = "areaId",value = "区域id")
    private Integer areaId;

    @ApiModelProperty("灯杆ID")
    private Integer lampPostId;

    @ApiModelProperty("灯杆ID集合")
    private List<Integer> lampPostIdList;
}
