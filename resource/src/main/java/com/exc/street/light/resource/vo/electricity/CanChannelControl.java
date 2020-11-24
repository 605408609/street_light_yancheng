package com.exc.street.light.resource.vo.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * can回路控制对象
 *
 * @author Linshiwen
 * @date 2018/5/26
 */
@Setter
@Getter
@ToString
public class CanChannelControl {
    /**
     * 回路id
     */
    private Integer id;

    /**
     * 0:关 1:开
     */
    private Integer value;

    /**
     * 设备编号
     */
    private Integer dsn;

    /**
     * 回路名称
     */
    private String name;

    /**
     * 回路tagID
     */
    private Integer tagId;

    /**
     * 节点id
     */
    private Integer nid;

    /**
     * 模块设备地址
     */
    private Integer deviceAddress;

    /**
     * 控制id
     */
    private Integer controlId;
}
