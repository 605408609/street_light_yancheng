/**
 * @filename:RadioPlayDevice 2020-05-11
 * @project pb  V1.0
 * Copyright(c) 2020 XiaoKun Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.pb;
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

/**   
 * @Description:(广播播放设备中间表实体类)
 * 
 * @version: V1.0
 * @author: XiaoKun
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RadioPlayDevice extends Model<RadioPlayDevice> {

	private static final long serialVersionUID = 1589161022902L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "广播播放设备中间表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "playId" , value = "广播id")
	private Integer playId;
    
	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;

	@ApiModelProperty(name = "groupId" , value = "分组id")
	private Integer groupId;
    
	@ApiModelProperty(name = "groupType" , value = "分组类型")
	private Integer groupType;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
