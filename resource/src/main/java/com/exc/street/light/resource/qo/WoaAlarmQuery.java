package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.vo.resp.WoaRespAlarmVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 告警查询对象
 *
 * @author Longshuangyang
 * @date 2020/03/26
 */
@Setter
@Getter
@ToString
public class WoaAlarmQuery extends QueryObject{

    /**
     * 设备名称
     */
    @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;

    /**
     * 告警类型id
     */
    @ApiModelProperty(name = "alarmTypeId" , value = "告警类型id")
    private Integer alarmTypeId;

    /**
     * 告警状态（1：未处理，2：处理中，3：已处理）
     */
    @ApiModelProperty(name = "alarmStatus" , value = "告警状态（1：未处理，2：处理中，3：已处理）")
    private Integer alarmStatus;
    
    /**
     * 消息对象的集合
     */
    @ApiModelProperty(name = "listNews" , value = "消息对象的集合")
    private List<WoaRespAlarmVO> listNews;

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

}
