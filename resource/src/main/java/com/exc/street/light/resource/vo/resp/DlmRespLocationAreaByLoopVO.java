package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 集控分组下的区域对象
 * @Date 2020/8/25
 */
@Data
public class DlmRespLocationAreaByLoopVO {

    @ApiModelProperty(name = "id" , value = "区域id")
    private Integer id;

    @ApiModelProperty(name = "partId" , value = "分别id")
    private String partId;

    public void setPartId(String partId) {
        this.partId = "area" + partId;
    }

    @ApiModelProperty(name = "name" , value = "区域名称")
    private String name;

    @ApiModelProperty(name = "childrenList" , value = "街道集合")
    private List<DlmRespLocationStreetByLoopVO> childrenList;
}
