package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 强电场景表
 *
 * @author Linshiwen
 * @date 2018/5/28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("can_scene")
public class CanScene extends Model<CanScene> {

    private static final long serialVersionUID = 3658085224162556928L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    @ApiModelProperty(name = "sceneId", value = "场景编码")
    private Integer sceneId;

    @ApiModelProperty(name = "nid", value = "强电节点编号")
    private Integer nid;

    @ApiModelProperty(name = "siteSceneId", value = "站点场景id")
    private Integer siteSceneId;

    @ApiModelProperty(name = "inputTime", value = "录入时间")
    private Date inputTime;

    @ApiModelProperty(name = "name", value = "场景名称")
    private String name;

    @ApiModelProperty(name = "status", value = "场景状态(0-空闲，1-启用，2-停用)")
    private Integer status = 1;

    @ApiModelProperty(name = "controlObjectList", value = "节点控制对象集合")
    private transient List<CanControlObject> controlObjectList = new ArrayList<>();

    @ApiModelProperty(name = "electricityNode", value = "所属节点对象")
    private transient ElectricityNode electricityNode;
}