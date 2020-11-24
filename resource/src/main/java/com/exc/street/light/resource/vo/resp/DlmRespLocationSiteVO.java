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
 * @Description:站点复杂集合返回对象
 * @version: V1.0
 * @author: Longshuangyang
 */
@Getter
@Setter
@ToString
public class DlmRespLocationSiteVO<T> {

    /**
     * 站点id
     */
    @ApiModelProperty(name = "id" , value = "站点id")
    private Integer id;

    /**
     * 区别id
     */
    @ApiModelProperty(name = "partId" , value = "区别id")
    private String partId;

    /**
     * 站点名称
     */
    @ApiModelProperty(name = "name" , value = "站点名称")
    private String name;

    /**
     * 站点描述
     */
    @ApiModelProperty(name = "description" , value = "站点描述")
    private String description;

    /**
     * 站点创建时间
     */
    @ApiModelProperty(name = "createTime" , value = "站点创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 站点下设备数量
     */
    @ApiModelProperty(name = "deviceNumber" , value = "站点下设备数量")
    private Integer deviceNumber;

    /**
     * 街道id
     */
    @ApiModelProperty(name = "superId" , value = "街道id")
    private Integer superId;

    /**
     * 街道名称
     */
    @ApiModelProperty(name = "superName" , value = "街道名称")
    private String superName;

    /**
     * 灯杆集合
     */
    @ApiModelProperty(name = "childrenList" , value = "灯杆集合")
    private List<DlmRespLocationLampPostVO> childrenList;
}
