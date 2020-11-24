package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 集控分组下的设备（灯具）对象
 * @Date 2020/8/25
 */
@Data
public class DlmRespDeviceByLoopVO {

    @ApiModelProperty(name = "id" , value = "设备id")
    private Integer id;

    @ApiModelProperty(name = "partId" , value = "分别id")
    private String partId;

    public void setPartId(String partId) {
        this.partId = "device" + partId;
    }

    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

}
