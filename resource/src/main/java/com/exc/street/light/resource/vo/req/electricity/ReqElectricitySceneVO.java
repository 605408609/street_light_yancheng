package com.exc.street.light.resource.vo.req.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 站点日程  强电场景接收对象
 *
 * @author Linshiwen
 * @date 2018/7/11
 */
@Setter
@Getter
@ToString
public class ReqElectricitySceneVO {
    /**
     * 站点场景id
     */
    private Integer siteSceneId;
    /**
     * 场景名称
     */
    private String name;

    /**
     * 定时时间
     */
    private String time;
}
