package com.exc.street.light.resource.dto.dlm;

import lombok.Data;

/**
 * 集控回路场景设置状态 dto
 * @Author: Xiaok
 * @Date: 2020/11/5 11:38
 */
@Data
public class ControlLoopSceneStatusDTO {

    /**
     * 记录id
     */
    private Integer id;

    /**
     * 集控ip
     */
    private String ip;

    /**
     * 集控端口
     */
    private Integer port;

    /**
     * 回路编号 1-8
     */
    private String num;

    /**
     * 是否添加 开 场景,0-否 1-是
     */
    private Integer addOpenStatus;

    /**
     * 是否添加 关 场景,0-否 1-是
     */
    private Integer addCloseStatus;


}
