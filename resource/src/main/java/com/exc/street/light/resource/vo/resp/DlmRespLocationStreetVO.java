package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description:街道复杂集合返回对象
 *
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
@Getter
@Setter
@ToString
public class DlmRespLocationStreetVO {

    /**
     * 街道id
     */
    @ApiModelProperty(name = "id" , value = "街道id")
    private Integer id;

    /**
     * 区别id
     */
    @ApiModelProperty(name = "partId" , value = "区别id")
    private String partId;

    /**
     * 街道名称
     */
    @ApiModelProperty(name = "name" , value = "街道名称")
    private String name;

    /**
     * 街道描述
     */
    @ApiModelProperty(name = "description" , value = "街道描述")
    private String description;

    /**
     * 街道创建时间
     */
    @ApiModelProperty(name = "createTime" , value = "街道创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 街道下设备数量
     */
    @ApiModelProperty(name = "deviceNumber" , value = "街道下设备数量")
    private Integer deviceNumber;

    /**
     * 区域id
     */
    @ApiModelProperty(name = "superId" , value = "区域id")
    private Integer superId;

    /**
     * 区域name
     */
    @ApiModelProperty(name = "superName" , value = "区域name")
    private String superName;

    /**
     * 站点集合
     */
    @ApiModelProperty(name = "childrenList" , value = "站点集合")
    private List<DlmRespLocationSiteVO> childrenList;

}
