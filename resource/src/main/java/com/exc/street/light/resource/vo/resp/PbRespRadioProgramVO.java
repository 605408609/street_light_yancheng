package com.exc.street.light.resource.vo.resp;

import com.exc.street.light.resource.entity.pb.RadioMaterial;
import com.exc.street.light.resource.entity.pb.RadioProgramMaterial;
import com.exc.street.light.resource.vo.req.PbReqRadioProgramMaterialVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 公共广播节目返回对象
 */
@Getter
@Setter
@ToString
public class PbRespRadioProgramVO {

    @ApiModelProperty(name = "id" , value = "节目表id")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "节目名称")
    private String name;

    @ApiModelProperty(name = "description" , value = "节目描述")
    private String description;

    @ApiModelProperty(name = "duration" , value = "节目时长")
    private Integer duration;

    @ApiModelProperty(name = "capacity" , value = "节目容量")
    private Float capacity;

    @ApiModelProperty(name = "creator" , value = "创建人（运营原型出来后为不可空，关联用户）")
    private Integer creator;

    @ApiModelProperty(name = "creatorName" , value = "创建人名称")
    private String creatorName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "修改时间")
    private Date updateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "radioMaterialList" , value = "节目素材集合")
    private List<RadioMaterial> radioMaterialList;
}
