package com.exc.street.light.resource.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 添加分组接口接收参数
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class DlmReqGroupVO {

    @ApiModelProperty(name = "id" , value = "分组表id，自增")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "分组名称")
    private String name;

    @ApiModelProperty(name = "description" , value = "分组描述")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "creator" , value = "创建人（运营原型出来后为不可空，关联用户）")
    private Integer creator;

    @ApiModelProperty(name = "typeId" , value = "分组类型id（1：灯杆分组，2：灯具分组）")
    private Integer typeId;

    @ApiModelProperty(name = "lampPostIdList" , value = "灯杆或灯具id集合")
    private List<Integer> lampPostIdList;

    @ApiModelProperty(name = "lampPostNameList" , value = "灯杆或灯具名称集合")
    private List<String> lampPostNameList;

}
