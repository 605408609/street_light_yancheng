package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 告警返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespAlarmVO {

    @ApiModelProperty(name = "id" , value = "告警表id")
    private Integer id;

    @ApiModelProperty(name = "content" , value = "告警内容")
    private String content;

    @ApiModelProperty(name = "addr" , value = "告警发生地址")
    private String addr;

    @ApiModelProperty(name = "status" , value = "告警状态（1：未处理，2：处理中，3：已处理）默认1")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "disposeTime" , value = "开始处理时间")
    private Date disposeTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "durationTime" , value = "持续处理时间")
    private Date durationTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "disposeCompleteTime" , value = "处理完成时间")
    private Date disposeCompleteTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "currentTime" , value = "当前时间")
    private Date currentTime;

    @ApiModelProperty(name = "typeId" , value = "告警类型id")
    private Integer typeId;

    @ApiModelProperty(name = "typeName" , value = "告警名称")
    private String typeName;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;
    
    
   
	
	@ApiModelProperty(name = "serviceName" , value = "服务名称")
	private String serviceName;
	
	@ApiModelProperty(name = "serviceId" , value = "服务Id(值为1:故障告警，值为2：一键呼叫)")
	private Integer serviceId;
	
	@ApiModelProperty(name = "haveRead" , value = "是否已读（0：未读，1：已读）")
	private Integer haveRead;
	
	@ApiModelProperty(name = "deviceName" , value = "设备名称")
	private String deviceName;


}
