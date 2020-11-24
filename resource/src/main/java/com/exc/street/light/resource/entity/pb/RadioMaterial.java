/**
 * @filename:RadioMaterial 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.resource.entity.pb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description:TODO(实体类)
 * @version: V1.0
 * @author: LeiJing
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RadioMaterial extends Model<RadioMaterial> {

    private static final long serialVersionUID = 1584773703240L;

    /**
     * 素材表id，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "素材表id，自增")
    private Integer id;

    /**
     * 素材名称
     */
    @ApiModelProperty(name = "name", value = "素材名称")
    private String name;

    /**
     * 文件大小
     */
    @ApiModelProperty(name = "size", value = "文件大小")
    private Float size;

    /**
     * 素材时长
     */
    @ApiModelProperty(name = "duration", value = "素材时长(s)")
    private Integer duration;

    @ApiModelProperty(name = "durationStr", value = "节目时长显示(格式：x:x:x)")
    private transient String durationStr;

    /**
     * 设置文件名前缀
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        String prefix = "mp3/";
        if (fileName != null) {
            this.fileName = fileName.contains(prefix) ? fileName : prefix + fileName;
            return;
        }
        this.fileName = null;
    }

    /**
     * 文件存放名称
     */
    @ApiModelProperty(name = "fileName", value = "文件存放名称")
    private String fileName;

    /**
     * 文件路径
     */
    @ApiModelProperty(name = "path", value = "文件路径")
    private String path;

    /**
     * 雷拓服务器文件ID
     */
    @ApiModelProperty(name = "fileId", value = "雷拓服务器文件ID")
    private Integer fileId;

    /**
     * 创建人（运营原型出来后为不可空，关联用户）
     */
    @ApiModelProperty(name = "creator", value = "创建人（运营原型出来后为不可空，关联用户）")
    private Integer creator;

    /**
     * 创建人姓名
     */
    @ApiModelProperty(name = "creatorName", value = "创建人名称")
    private transient String creatorName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
