package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Xiezhipeng
 * @Description
 * @Date 2020/3/18
 */
@Data
public class UaRespRoleVO {

    @ApiModelProperty(name = "id" , value = "主键id")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "角色名称")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "founderId" , value = "创建人id")
    private Integer founderId;

    @ApiModelProperty(name = "founderName" , value = "创建人名称")
    private String founderName;

}
