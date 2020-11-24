package com.exc.street.light.resource.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 下发字幕接口接收参数
 *
 * @author Longshuangyang
 * @date 2020/10/23
 */
@Setter
@Getter
@ToString
public class    IrReqScreenSubtitlePlayVO {

    @ApiModelProperty(name = "id" , value = "显示屏字幕表")
    private Integer id;

    @ApiModelProperty(name = "num" , value = "字幕滚动次数（填0停止滚动，填负数永久滚动）")
    private Integer num;

    @ApiModelProperty(name = "html" , value = "字幕格式")
    private String html;

    @ApiModelProperty(name = "interval" , value = "步进间隔（单位：S 秒）")
    private Integer interval;

    @ApiModelProperty(name = "step" , value = "步进距离（单位：px 像素）")
    private Integer step;

    @ApiModelProperty(name = "direction" , value = "滚动方向（left往左滚动，right往右滚动）")
    private String direction;

    @ApiModelProperty(name = "align" , value = "显示位置（top上方，center中间，bottom底部）")
    private String align;

    @ApiModelProperty(name = "deviceId" , value = "设备id")
    private Integer deviceId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "endTime" , value = "字幕结束时间")
    private Date endTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "creator" , value = "创建人")
    private Integer creator;

    @ApiModelProperty(name = "isAll" , value = "是否控制所有(0：否，1：是，默认0)")
    private Integer isAll;

    @ApiModelProperty(name = "numList" , value = "设备编号集合")
    private List<String> numList;

    @ApiModelProperty(name = "prototype" , value = "用户输入的原始字符串")
    private String prototype;

    @ApiModelProperty(name = "fontSize" , value = "字体大小")
    private Integer fontSize;

}
