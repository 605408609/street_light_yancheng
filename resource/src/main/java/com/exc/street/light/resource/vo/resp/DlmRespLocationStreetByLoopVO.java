package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 集控分组下的街道对象
 * @Date 2020/8/25
 */
@Data
public class DlmRespLocationStreetByLoopVO {

    @ApiModelProperty(name = "id" , value = "街道id")
    private Integer id;

    @ApiModelProperty(name = "partId" , value = "区别id")
    private String partId;

    public void setPartId(String partId) {
        this.partId = "street" + partId;
    }

    @ApiModelProperty(name = "name" , value = "街道名称")
    private String name;

    @ApiModelProperty(name = "childrenList" , value = "站点集合")
    private List<DlmRespLocationSiteByLoopVO> childrenList;

}
