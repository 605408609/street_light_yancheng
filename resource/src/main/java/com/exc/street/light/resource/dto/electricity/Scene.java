package com.exc.street.light.resource.dto.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景
 *
 * @author LinShiWen
 * @date 2018/4/20
 */
@Setter
@Getter
@ToString
public class Scene {
    /**
     * 场景编码 1-32
     */
    private int sn;

    /**
     * 场景状态 0-空闲，1-启用，2-停用, 3-立即
     */
    private int status = 1;

    /**
     * 节点id
     */
    private int nid;

    /**
     * 站点场景id
     */
    private int siteSceneId;

    /**
     * 场景名称
     */
    private String name;

    /**
     * 控制对象集合
     */
    List<ControlObject> controlObjects = new ArrayList<ControlObject>();
}
