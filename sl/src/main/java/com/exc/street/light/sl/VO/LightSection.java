package com.exc.street.light.sl.VO;

import java.util.List;

public class LightSection {

    //控制id
    private String control_id	;
    //采集数据
    private List<LightCollector> collectors;
    //回路(线圈)控制uid
    private String control_uid	;
    //客户自定义的控制名称
    private String controller_name	;
    //控制器在线状态
    private Integer online	;
    //回路(线圈)号
    private Integer id	;
    //控制端口号
    private Integer ep	;
    //控制器数据上报时间
    private String refresh_time	;
    //回路(线圈)开闭状态
    private Integer status	;

}
