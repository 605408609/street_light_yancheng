package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@ToString
public class DlmRespGroupVO {

    /**
     * 分组id
     */
    @ApiModelProperty(name = "groupId", value = "分组id")
    private Integer groupId;

    /**
     * 分组名称
     */
    @ApiModelProperty(name = "groupName", value = "分组名称")
    private String groupName;

    /**
     * 创建人
     */
    @ApiModelProperty(name = "creator", value = "创建人")
    private Integer creator;

    /**
     * 创建人名称
     */
    @ApiModelProperty(name = "creatorName", value = "创建人名称")
    private String creatorName;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 备注
     */
    @ApiModelProperty(name = "description", value = "备注")
    private String description;

    /**
     * 挂载灯杆数量
     */
    @ApiModelProperty(name = "lampPostNumber", value = "挂载灯杆数量")
    private Integer lampPostNumber;

    /**
     * 分组类型（1：灯杆分组，2：灯具分组）
     */
    @ApiModelProperty(name = "typeId", value = "分组类型（1：灯杆分组，2：灯具分组）")
    private Integer typeId;


}
