package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 站点场景控制实体类
 *
 * @author LinShiWen
 * @date 2019/2/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("can_site_scene")
public class CanSiteScene  extends Model<CanSiteScene> {

    private static final long serialVersionUID = 6724798177240482283L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    /**
     * 录入时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(name = "createTime", value = "录入时间")
    private Date createTime;

    /**
     * 场景名称
     */
    @ApiModelProperty(name = "name", value = "场景名称")
    private String name;

    /**
     * 场景状态(0-失败 1-成功)
     */
    @ApiModelProperty(name = "state", value = "场景状态(0-失败 1-成功)")
    private Integer state;

    /**
     * 站点id
     */
    @TableField(value = "site_id")
    @ApiModelProperty(name = "siteId", value = "站点id")
    private Integer siteId;

    /**
     * 启动仪式策略id
     */
    @TableField(value = "strategy_inter_id")
    @ApiModelProperty(name = "strategyInterId", value = "启动仪式策略id")
    private Integer strategyInterId;

}