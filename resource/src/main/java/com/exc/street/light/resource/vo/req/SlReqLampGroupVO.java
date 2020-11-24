package com.exc.street.light.resource.vo.req;

import com.exc.street.light.resource.entity.sl.LampGroupSingle;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 新增灯具分组接口接收参数
 *
 * @author Longshuangyang
 * @date 2020/07/16
 */
@Setter
@Getter
@ToString
public class SlReqLampGroupVO {

    @ApiModelProperty(name = "id" , value = "灯具分组id")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "灯具分组名称")
    private String name;

    @ApiModelProperty(name = "description" , value = "灯具分组描述")
    private String description;

    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "creator" , value = "创建人（运营原型出来后为不可空，关联用户）")
    private Integer creator;

    @ApiModelProperty(name = "singleIdList" , value = "灯具id集合")
    private List<Integer> singleIdList;

    @ApiModelProperty(name = "lampGroupSingleList" , value = "灯具id集合")
    private List<LampGroupSingle> lampGroupSingleList;

}
