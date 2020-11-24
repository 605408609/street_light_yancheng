package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 强电设备
 *
 * @author Linshiwen
 * @date 2018/5/22
 */
@Setter
@Getter
@ToString
@TableName("can_device")
public class CanDevice extends Model<CanDevice> implements Serializable {
    private static final long serialVersionUID = -2742270193178348693L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    /**
     * can索引
     */
    @TableField(value = "can_index")
    @ApiModelProperty(name = "canIndex", value = "can索引")
    private Integer canIndex;

    /**
     * canId
     */
    @TableField(value = "can_id")
    @ApiModelProperty(name = "canId", value = "canId")
    private String canId;

    /**
     * 模块型号
     */
    @ApiModelProperty(name = "moduleType", value = "模块型号")
    @TableField(value = "module_type")
    private String moduleType;

    /**
     * 等待时间(单位毫秒)
     */
    @ApiModelProperty(name = "waitTime", value = "等待时间(单位毫秒)")
    @TableField(value = "wait_time")
    private Integer waitTime = 3000;

    /**
     * 模块名称
     */
    @ApiModelProperty(name = "name", value = "模块名称")
    private String name;

    /**
     * 模块序列号
     */
    @TableField(value = "batch_number")
    @ApiModelProperty(name = "batchNumber", value = "模块序列号")
    private String batchNumber;

    /**
     * 设备物理地址
     */
    @ApiModelProperty(name = "address", value = "设备物理地址")
    private String address;

    /**
     * 模块类型(1:场景模块 2:驱动模块 3:输入模块 4:电流模块 5:4路交流接触器模块 6：8路交流接触器模块 7：12路交流接触器模块)
     */
    @ApiModelProperty(name = "mid", value = "模块类型(1:场景模块 2:驱动模块 3:输入模块 4:电流模块 5:4路交流接触器模块 6：8路交流接触器模块 7：12路交流接触器模块)")
    private Integer mid;


    @ApiModelProperty(name = "nid", value = "")
    private Integer nid;

}