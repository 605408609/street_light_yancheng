/**
 * @filename:RadioProgram 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.pb;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author: LeiJing
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RadioProgram extends Model<RadioProgram> {

	private static final long serialVersionUID = 1584773703240L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "节目表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "节目名称")
	private String name;
    
	@ApiModelProperty(name = "description" , value = "节目描述")
	private String description;
    
	@ApiModelProperty(name = "duration" , value = "节目时长(s)")
	private Integer duration;

	@ApiModelProperty(name = "durationStr" , value = "节目时长显示(格式：x时x分x秒)")
	private transient String durationStr;
    
	@ApiModelProperty(name = "capacity" , value = "节目容量(MB)")
	private Float capacity;

	@ApiModelProperty(name = "verifyStatus" , value = "审核状态(0-待审核 1-审核成功 2-审核失败)")
	private Integer verifyStatus;
    
	@ApiModelProperty(name = "creator" , value = "创建人（运营原型出来后为不可空，关联用户）")
	private Integer creator;

	@ApiModelProperty(name = "creatorName" , value = "创建人名称")
	private transient String creatorName;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "修改时间")
	private Date updateTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@ApiModelProperty(name = "isCanVerify" , value = "是否可以审核 0-否 1-是")
	private transient Integer isCanVerify;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
