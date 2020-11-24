package com.exc.street.light.resource.entity.dlm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeviceOnline extends Model<DeviceOnline>{

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id" , value = "在线表id，自增")
    private Integer id;

    @ApiModelProperty(name = "type" , value = "设备类型（不可空）")
    private int type;

    @ApiModelProperty(name = "deviceId" , value = "设备ID（不可空）")
    private int deviceId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "datetime" , value = "每天最后一次上线时间（不可空）")
    private Date datetime;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
