package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 分组查询对象
 *
 * @author Longshuangyang
 * @date 2020/03/26
 */
@Setter
@Getter
@ToString
public class DlmGroupQuery extends QueryObject{

    /**
     * 分组名称
     */
    @ApiModelProperty(name = "groupName" , value = "分组名称")
    private String groupName;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startTime" , value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime" , value = "结束时间")
    private String endTime;

    /**
     * 分组类型（1：灯杆分组，2：灯具分组）
     */
    @ApiModelProperty(name = "typeId" , value = "分组类型（1：灯杆分组，2：灯具分组）")
    private Integer typeId;

}
