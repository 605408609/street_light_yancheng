package com.exc.street.light.resource.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 下发节目获取节目参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class IrProgramVO {

    @ApiModelProperty(name = "id" , value = "节目表id，自增")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "节目名称")
    private String name;

    @ApiModelProperty(name = "description" , value = "节目描述")
    private String description;

    @ApiModelProperty(name = "totalSize" , value = "所有素材size总和（单位：B  字节）")
    private Integer totalSize;

    @ApiModelProperty(name = "creator" , value = "创建人（运营原型出来后为不可空，关联用户）")
    private Integer creator;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "修改时间")
    private Date updateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "irProgramMaterialVO" , value = "节目素材中间对象")
    private IrProgramMaterialVO irProgramMaterialVO;

}
