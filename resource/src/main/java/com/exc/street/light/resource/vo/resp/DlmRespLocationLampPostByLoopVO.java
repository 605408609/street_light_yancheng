package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 集控分组下的灯杆对象
 * @Date 2020/8/25
 */
@Data
public class DlmRespLocationLampPostByLoopVO {

    @ApiModelProperty(name = "id" , value = "灯杆表id，自增")
    private Integer id;

    @ApiModelProperty(name = "partId" , value = "分别id")
    private String partId;

    public void setPartId(String partId) {
        this.partId = "post" + partId;
    }

    @ApiModelProperty(name = "name" , value = "灯杆名称")
    private String name;

    @ApiModelProperty(name = "childrenList" , value = "设备集合（仅公共字段）")
    private List<DlmRespDeviceByLoopVO> childrenList;

}
