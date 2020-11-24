package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单图片返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespOrderPicVO {

    @ApiModelProperty(name = "orderPicId" , value = "工单图片id")
    private Integer orderPicId;

    public void setOrderPicName(String orderPicName) {
        this.orderPicName = "orderPicture/" + orderPicName;
    }

    @ApiModelProperty(name = "orderPicName" , value = "图片名称")
    private String orderPicName;

    @ApiModelProperty(name = "processId" , value = "工单进程id")
    private Integer processId;

    @ApiModelProperty(name = "statusId" , value = "工单状态id")
    private Integer statusId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;


}
