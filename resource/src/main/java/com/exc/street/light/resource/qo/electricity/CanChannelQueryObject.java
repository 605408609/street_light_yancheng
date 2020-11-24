package com.exc.street.light.resource.qo.electricity;

import com.exc.street.light.resource.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 强电回路查询对象
 *
 * @author Linshiwen
 * @date 2018/5/11
 */
@Setter
@Getter
@ToString
public class CanChannelQueryObject extends QueryObject {
    /**
     * 回路类型
     */
    private Integer channelTypeId;

    /**
     * 网关id
     */
    private Integer nid;

    /**
     * 网关名称
     */
    private String nodeName;


    /**
     * 回路名称
     */
    private String name;

    /**
     * 开关状态
     */
    private Integer status;

    /**
     * 场景编号
     */
    private Integer sid;

    /**
     * 场景tagId
     */
    private Integer tagId;

    /**
     * 网关状态 0:在线 1:离线
     */
    private Integer offline;

    /**
     * 区域id
     */
    private Integer areaId;
}
