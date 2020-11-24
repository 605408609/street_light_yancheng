package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description:分组复杂集合返回对象
 *
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
@Getter
@Setter
@ToString
public class DlmRespLocationGroupVO {

    @ApiModelProperty(name = "id" , value = "分组id")
    private Integer id;

    @ApiModelProperty(name = "partId" , value = "分别id")
    private String partId;

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

    @ApiModelProperty(name = "deviceNumber" , value = "分组下设备数量")
    private Integer deviceNumber;

    /**
     * 灯杆集合
     */
    @ApiModelProperty(name = "childrenList" , value = "灯杆集合")
    private List<DlmRespLocationLampPostVO> childrenList;

}
