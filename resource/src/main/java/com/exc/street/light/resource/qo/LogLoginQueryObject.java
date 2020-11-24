package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 登录日志查询对象
 * @Date 2020/6/9
 */
@Data
public class LogLoginQueryObject extends PageParam {

    @ApiModelProperty(name = "operatorName" , value = "操作人员名称")
    private String operatorName;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(name = "beginTime" , value = "开始时间")
    private String beginTime;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(name = "endTime" , value = "结束时间")
    private String endTime;

}
