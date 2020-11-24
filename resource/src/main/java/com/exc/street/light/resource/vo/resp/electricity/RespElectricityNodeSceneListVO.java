package com.exc.street.light.resource.vo.resp.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 强电节点列表场景信息视图
 *
 * @author LeiJing
 * @date 2019/6/24
 */
@Setter
@Getter
@ToString
public class RespElectricityNodeSceneListVO {

	/**
	 * 节点Id
	 */
    private Integer id;

    /**
     * 节点编号
     */
    private String num;
    
    /**
     * 节点名称
     */
    private String name;

    /**
     * 网络状态 0:在线 1:离线
     */
    private Integer isOffline;

    /**
     * 节点已编辑场景名称集合
     */
    private List<String> scenes;
}