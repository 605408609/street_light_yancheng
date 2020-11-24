/**
 * @filename:ScreenProgram 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.ir;
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
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ScreenProgram extends Model<ScreenProgram> {

	private static final long serialVersionUID = 1585821576248L;
	
	@TableId(value = "id", type = IdType.AUTO)
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

	@ApiModelProperty(name = "verifyStatus" , value = "审核状态(0-待审核 1-审核成功 2-审核失败)")
	private Integer verifyStatus;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
