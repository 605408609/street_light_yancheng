package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 显示屏输出列表返回对象
 *
 * @author Longshuangyang
 * @date 2020/04/03
 */
@Setter
@Getter
@ToString
public class IrRespScreenMaterialVO {
    @ApiModelProperty(name = "id" , value = "素材表id，自增")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "素材名称")
    private String name;

    @ApiModelProperty(name = "type" , value = "素材类型（Video视频，Image图片，MultiLineText文本）")
    private String type;

    @ApiModelProperty(name = "creator" , value = "创建人id")
    private Integer creator;

    @ApiModelProperty(name = "creatorName" , value = "创建人名称")
    private String creatorName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "maxPlayTime" , value = "视频时长 单位：S 秒")
    private Integer maxPlayTime;

    @ApiModelProperty(name = "mime" , value = "固定标识（视频：mp4格式：video/mp4，其他：video/quicktime）（图片：GIF使用image/gif，其他：image/jpeg）")
    private String mime;

    @ApiModelProperty(name = "size" , value = "视频/图片文件大小（单位：B  字节）")
    private Long size;

    public void setFileName(String fileName) {
        this.fileName = "orderPicture/" + fileName;
    }

    @ApiModelProperty(name = "fileName" , value = "文件存放名称")
    private String fileName;

    @ApiModelProperty(name = "oldfilePath" , value = "文件路径")
    private String oldfilePath;

    @ApiModelProperty(name = "fileExt" , value = "文件扩展名")
    private String fileExt;

    @ApiModelProperty(name = "width" , value = "视频/图片 宽")
    private Integer width;

    @ApiModelProperty(name = "height" , value = "视频/图片 高")
    private Integer height;

    @ApiModelProperty(name = "backgroundColor" , value = "文本背景色")
    private String backgroundColor;

    @ApiModelProperty(name = "speed" , value = "文本翻页等待时长（单位：S 秒）")
    private Integer speed;

    @ApiModelProperty(name = "lineHeight" , value = "文本文字行高")
    private Double lineHeight;

    @ApiModelProperty(name = "center" , value = "文本是否居中（0：false，1：true）")
    private Integer center;

    @ApiModelProperty(name = "html" , value = "文本内容")
    private String html;
}
