package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分区表
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_partition")
public class Partition extends Model<Partition> implements Serializable {

    private static final long serialVersionUID = -1218084165292124383L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    @ApiModelProperty(name = "province", value = "省份")
    private String province;

    @ApiModelProperty(name = "city", value = "市")
    private String city;

    @ApiModelProperty(name = "district", value = "区")
    private String district;

    @ApiModelProperty(name = "addr", value = "地址")
    private String addr;

    //@Column(name = "energy_charge")
    @ApiModelProperty(name = "energyCharge", value = "分区电费")
    private BigDecimal energyCharge;

    @ApiModelProperty(name = "description", value = "描述")
    private String description;

    @ApiModelProperty(name = "name", value = "分区名")
    private String name;
}