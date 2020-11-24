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
 * @Description:灯杆复杂集合返回对象
 *
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
@Getter
@Setter
@ToString
public class DlmRespLocationLampPostVO {
    /**
     * 灯杆表id，自增
     */
    @ApiModelProperty(name = "id" , value = "灯杆表id，自增")
    private Integer id;

    /**
     * 分别id
     */
    @ApiModelProperty(name = "partId" , value = "分别id")
    private String partId;

    /**
     * 灯杆名称
     */
    @ApiModelProperty(name = "name" , value = "灯杆名称")
    private String name;

    /**
     * 灯杆编号
     */
    @ApiModelProperty(name = "lampPostNum" , value = "灯杆编号")
    private String lampPostNum;

    /**
     * 灯杆型号
     */
    @ApiModelProperty(name = "lampPostModel" , value = "灯杆型号")
    private String lampPostModel;

    /**
     * 经度
     */
    @ApiModelProperty(name = "lampPostLongitude" , value = "经度")
    private Double lampPostLongitude;

    /**
     * 纬度
     */
    @ApiModelProperty(name = "lampPostLatitude" , value = "纬度")
    private Double lampPostLatitude;

    /**
     * 厂家
     */
    @ApiModelProperty(name = "lampPostManufacturer" , value = "厂家")
    private String lampPostManufacturer;

    /**
     * 安装位置
     */
    @ApiModelProperty(name = "lampPostLocation" , value = "安装位置")
    private String lampPostLocation;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 灯杆下设备数量
     */
    @ApiModelProperty(name = "deviceNumber" , value = "灯杆下设备数量")
    private Integer deviceNumber;

    /**
     * 站点id，分组id
     */
    @ApiModelProperty(name = "superId" , value = "站点id，分组id")
    private Integer superId;

    /**
     * 站点name，分组name
     */
    @ApiModelProperty(name = "superName" , value = "站点name，分组name")
    private String superName;

    /**
     * 区域id
     */
    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    /**
     * 区域name
     */
    @ApiModelProperty(name = "areaName" , value = "区域name")
    private String areaName;

    /**
     * 街道id
     */
    @ApiModelProperty(name = "streetId" , value = "街道id")
    private Integer streetId;

    /**
     * 街道name
     */
    @ApiModelProperty(name = "streetName" , value = "街道name")
    private String streetName;

    /**
     * 区域街道站点id顺序逗号拼接
     */
    @ApiModelProperty(name = "ids" , value = "区域街道站点id顺序逗号拼接")
    private String ids;

    /**
     * 区域街道站点名称顺序逗号拼接
     */
    @ApiModelProperty(name = "names" , value = "区域街道站点名称顺序逗号拼接")
    private String names;

    /**
     * 设备集合（仅公共字段）
     */
    @ApiModelProperty(name = "childrenList" , value = "设备集合（仅公共字段）")
    private List<DlmRespDevicePublicParVO> childrenList;
}
