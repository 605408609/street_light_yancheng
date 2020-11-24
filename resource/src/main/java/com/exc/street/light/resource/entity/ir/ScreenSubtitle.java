/**
 * @filename:ScreenSubtitle 2020-10-23
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
 * @Description:TODO(显示屏字幕表实体类)
 * 
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ScreenSubtitle extends Model<ScreenSubtitle> {

	private static final long serialVersionUID = 1603420763421L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "显示屏字幕表")
	private Integer id;
    
	@ApiModelProperty(name = "num" , value = "字幕滚动次数（填0停止滚动，填负数永久滚动）")
	private Integer num;
    
	@ApiModelProperty(name = "html" , value = "字幕格式")
	private String html;
    
	@ApiModelProperty(name = "interval" , value = "步进间隔（单位：S 秒）")
	private Integer interval;
    
	@ApiModelProperty(name = "step" , value = "步进距离（单位：px 像素）")
	private Integer step;
    
	@ApiModelProperty(name = "direction" , value = "滚动方向（left往左滚动，right往右滚动）")
	private String direction;
    
	@ApiModelProperty(name = "align" , value = "显示位置（top上方，center中间，bottom底部）")
	private String align;
    
	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "endTime" , value = "字幕结束时间")
	private Date endTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@ApiModelProperty(name = "creator" , value = "创建人")
	private Integer creator;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
