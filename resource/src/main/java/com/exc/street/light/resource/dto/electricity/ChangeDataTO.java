package com.exc.street.light.resource.dto.electricity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Linshiwen
 * @date 2018/6/27
 */
@Setter
@Getter
public class ChangeDataTO {
    private Integer id;
    private Float data;
    /**
     * 推送类型 1:开关状态变化 2:电流变化
     */
    private Integer type;
}
