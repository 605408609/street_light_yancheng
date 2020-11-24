package com.exc.street.light.resource.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Xiezhipeng
 * @Description 区域请求参数类
 * @Date 2020/10/21
 */
@Data
public class DlmReqLocationAreaVO {

    @ApiModelProperty(name = "id" , value = "区域表id，自增")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "区域名称（不可空）")
    private String name;

    @ApiModelProperty(name = "description" , value = "区域描述")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "lampState" , value = "区域灯具状态")
    private Integer lampState;

    @ApiModelProperty(name = "lampBrightness" , value = "区域灯具亮度")
    private Integer lampBrightness;

    @ApiModelProperty(name = "picId" , value = "图片id")
    private Integer picId;
}
