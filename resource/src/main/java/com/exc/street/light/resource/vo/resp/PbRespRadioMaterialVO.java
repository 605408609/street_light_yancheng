package com.exc.street.light.resource.vo.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class PbRespRadioMaterialVO {

    /**
     * 素材表id，自增
     */
    @ApiModelProperty(name = "id" , value = "素材表id，自增")
    private Integer id;

    /**
     * 素材名称
     */
    @ApiModelProperty(name = "name" , value = "素材名称")
    private String name;

    /**
     * 文件大小
     */
    @ApiModelProperty(name = "size" , value = "文件大小")
    private Float size;

    /**
     * 素材时长
     */
    @ApiModelProperty(name = "duration" , value = "素材时长")
    private Integer duration;


    /**
     * 文件存放名称
     */
    @ApiModelProperty(name = "fileName" , value = "文件存放名称")
    private String fileName;

    /**
     * 文件路径
     */
    @ApiModelProperty(name = "path" , value = "文件路径")
    private String path;

    /**
     * 雷拓服务器文件ID
     */
    @ApiModelProperty(name = "fileId" , value = "雷拓服务器文件ID")
    private Integer fileId;

    /**
     * 创建人（运营原型出来后为不可空，关联用户）
     */
    @ApiModelProperty(name = "creator" , value = "创建人（运营原型出来后为不可空，关联用户）")
    private Integer creator;

    /**
     * 创建人姓名
     */
    @ApiModelProperty(name = "creatorName" , value = "创建人名称")
    private transient String creatorName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;
}
