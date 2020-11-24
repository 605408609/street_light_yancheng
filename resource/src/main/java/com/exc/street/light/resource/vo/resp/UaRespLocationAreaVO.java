package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 区域用户集合
 * @Date 2020/4/22
 */
@Data
public class UaRespLocationAreaVO {

    /**
     * 区域id
     */
    private Integer id;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区别id,防止前端生成树的时候id值冲突
     */
    private String distinctId;

    /**
     * 用户集合
     */
    private List<UaRespSimpleUserVO> userList;

    @ApiModelProperty(name = "projectName" , value = "图片名称")
    private String projectName;

    public void setProjectName(String projectName) {
        this.projectName = "projectPicture/" + projectName;
    }
}
