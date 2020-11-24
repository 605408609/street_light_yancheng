package com.exc.street.light.resource.vo.req.electricity;

import lombok.Data;

/**
 * @Author: XuJiaHao
 * @Description: 更改定时执行状态
 * @Date: Created in 17:40 2020/8/24
 * @Modified:
 */
@Data
public class ReqTimingExecuteVO {
    private Integer nid;
    private Integer id;
    private Integer isExecute;
}
