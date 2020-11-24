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
 * 传感器关联显示屏显示数据设置接收参数
 *
 * @author Longshuangyang
 * @date 2020/11/10
 */
@Setter
@Getter
@ToString
public class IrReqScreenSubtitleEmVO {

    @ApiModelProperty(name = "id" , value = " 传感器关联显示屏显示数据设置表")
    private Integer id;

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "areaName" , value = "区域名称")
    private String areaName;

    @ApiModelProperty(name = "resolvingPower" , value = "分辨率（宽*高）")
    private String resolvingPower;

    @ApiModelProperty(name = "emDeviceId" , value = "传感器设备id")
    private Integer emDeviceId;

    @ApiModelProperty(name = "emDataField" , value = "支持的传感器数据字段（温度：temperature，湿度：humidity，气压：airPressure，风速平均值：windSpeedAverage，风向平均值：windDirectionAverage，PM2.5：pm25，PM10：pm10，噪音平均值：noiseAverage）")
    private String emDataField;

    @ApiModelProperty(name = "emDataFieldList" , value = "支持的传感器数据字段（温度：temperature，湿度：humidity，气压：airPressure，风速平均值：windSpeedAverage，风向平均值：windDirectionAverage，PM2.5：pm25，PM10：pm10，噪音平均值：noiseAverage）")
    private List<String> emDataFieldList;

    @ApiModelProperty(name = "num" , value = "字幕滚动次数（填0停止滚动，填负数永久滚动）")
    private Integer num;

    @ApiModelProperty(name = "html" , value = "字幕格式")
    private String html;

    @ApiModelProperty(name = "stepInterval" , value = "步进间隔（单位：S 秒）（25缓慢，50正常，100快）")
    private Integer stepInterval;

    @ApiModelProperty(name = "step" , value = "步进距离（单位：px 像素）")
    private Integer step;

    @ApiModelProperty(name = "direction" , value = "滚动方向（left往左滚动，right往右滚动）")
    private String direction;

    @ApiModelProperty(name = "align" , value = "显示位置（top上方，center中间，bottom底部）")
    private String align;

    @ApiModelProperty(name = "typefaceSize" , value = "字体大小（单位：px像素）")
    private Integer typefaceSize;

    @ApiModelProperty(name = "typefaceColor" , value = "字体颜色")
    private String typefaceColor;

    @ApiModelProperty(name = "backgroundColor" , value = "背景色")
    private String backgroundColor;

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

    @ApiModelProperty(name = "reqEmLampPostId", value = "传感器灯杆id")
    private Integer reqEmLampPostId;

    @ApiModelProperty(name = "reqIrLampPostIdList", value = "显示屏灯杆id集合")
    private List<Integer> reqIrLampPostIdList;

    @ApiModelProperty(name = "reqEmDeviceId", value = "传感器设备id")
    private Integer reqEmDeviceId;

    @ApiModelProperty(name = "reqIrDeviceIdList", value = "显示屏设备id集合")
    private List<Integer> reqIrDeviceIdList;

}
