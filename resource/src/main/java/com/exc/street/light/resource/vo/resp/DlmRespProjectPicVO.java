package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Xiezhipeng
 * @Description 项目（区域）图片返回视图类
 * @Date 2020/10/21
 */
@Data
public class DlmRespProjectPicVO {

    @ApiModelProperty(name = "id" , value = "项目图片主键ID")
    private Integer id;

    public void setName(String name) {
        this.name = "projectPicture/" + name;
    }

    @ApiModelProperty(name = "name" , value = "图片名称")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;
}
