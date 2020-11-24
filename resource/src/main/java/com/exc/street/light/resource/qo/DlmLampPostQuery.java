package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 路灯查询对象
 *
 * @author Longshuangyang
 * @date 2020/03/26
 */
@Setter
@Getter
@ToString
public class DlmLampPostQuery extends QueryObject{

    /**
     * 灯杆id
     */
    @ApiModelProperty(name = "id" , value = "灯杆id")
    private Integer id;

    /**
     * 灯杆id集合
     */
    @ApiModelProperty(name = "lampPostIdList" , value = "灯杆id集合")
    private List<Integer> lampPostIdList;

    /**
     * 灯杆名称或者编号
     */
    @ApiModelProperty(name = "lampPostNameOrNum" , value = "灯杆名称或者编号")
    private String lampPostNameOrNum;

    /**
     * 区域id
     */
    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    /**
     * 区域名称
     */
    @ApiModelProperty(name = "areaName" , value = "区域名称")
    private String areaName;

    /**
     * 街道id
     */
    @ApiModelProperty(name = "streetId" , value = "街道id")
    private Integer streetId;

    /**
     * 街道名称
     */
    @ApiModelProperty(name = "streetName" , value = "街道名称")
    private String streetName;

    /**
     * 站点id
     */
    @ApiModelProperty(name = "siteId" , value = "站点id")
    private Integer siteId;

    /**
     * 站点名称
     */
    @ApiModelProperty(name = "siteName" , value = "站点名称")
    private String siteName;

    /**
     * 设备类型id
     */
    @ApiModelProperty(name = "deviceTypeId" , value = "设备类型id")
    private Integer deviceTypeId;
}
