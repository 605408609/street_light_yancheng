/**
 * @filename:DeviceUpgradeLogStatus 2020-08-25
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.sl;
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

/**   
 * @Description:TODO(设备升级状态表实体类)
 * 
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeviceUpgradeLogStatus extends Model<DeviceUpgradeLogStatus> {

	private static final long serialVersionUID = 1598335243537L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "设备升级状态表")
	private Integer id;
    
	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;
    
	@ApiModelProperty(name = "editionOld" , value = "当前版本号")
	private String editionOld;
    
	@ApiModelProperty(name = "isSuccess" , value = "是否成功（1：成功，2：失败）")
	private Integer isSuccess;
    
	@ApiModelProperty(name = "logId" , value = "升级记录id")
	private Integer logId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
