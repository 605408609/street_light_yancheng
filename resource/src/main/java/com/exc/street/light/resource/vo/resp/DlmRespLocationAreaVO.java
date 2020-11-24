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
 * @Description:区域复杂集合返回对象
 *
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
@Getter
@Setter
@ToString
public class DlmRespLocationAreaVO {

    /**
     * 区域id
     */
    @ApiModelProperty(name = "id" , value = "区域id")
    private Integer id;

    /**
     * 分别id
     */
    @ApiModelProperty(name = "partId" , value = "分别id")
    private String partId;

    /**
     * 区域名称
     */
    @ApiModelProperty(name = "name" , value = "区域名称")
    private String name;

    /**
     * 区域描述
     */
    @ApiModelProperty(name = "description" , value = "区域描述")
    private String description;

    /**
     * 区域创建时间
     */
    @ApiModelProperty(name = "createTime" , value = "区域创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 区域下设备数量
     */
    @ApiModelProperty(name = "deviceNumber" , value = "区域下设备数量")
    private Integer deviceNumber;

    /**
     * 街道集合
     */
    @ApiModelProperty(name = "childrenList" , value = "街道集合")
    private List<DlmRespLocationStreetVO> childrenList;

    @ApiModelProperty(name = "lampState" , value = "区域灯具状态")
    private Integer lampState;

    @ApiModelProperty(name = "lampBrightness" , value = "区域灯具亮度")
    private Integer lampBrightness;

//    @ApiModelProperty(name = "projectPicVO" , value = "项目图片")
//    private DlmRespProjectPicVO projectPicVO;

}
