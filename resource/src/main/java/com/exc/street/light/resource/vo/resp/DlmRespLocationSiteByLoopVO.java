package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 集控分组下的站点对象
 * @Date 2020/8/25
 */
@Data
public class DlmRespLocationSiteByLoopVO {

    @ApiModelProperty(name = "id" , value = "站点id")
    private Integer id;

    @ApiModelProperty(name = "partId" , value = "区别id")
    private String partId;

    public void setPartId(String partId) {
        this.partId = "site" + partId;
    }

    @ApiModelProperty(name = "name" , value = "站点名称")
    private String name;

    @ApiModelProperty(name = "childrenList" , value = "灯杆集合")
    private List<DlmRespLocationLampPostByLoopVO> childrenList;

}
