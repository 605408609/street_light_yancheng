package com.exc.street.light.resource.vo.electricity;

import com.exc.street.light.resource.dto.electricity.Scene;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 站点场景编辑接收对象
 *
 * @author Linshiwen
 * @date 2018/8/14
 */
@Setter
@Getter
@ToString
public class SiteSceneVO {
    List<Scene> scenes;
    /**
     * 场景名称
     */
    private String name;
    /**
     * 站点id
     */
    private Integer siteId;
}
