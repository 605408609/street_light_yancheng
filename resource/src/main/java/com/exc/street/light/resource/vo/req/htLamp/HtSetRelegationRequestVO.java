package com.exc.street.light.resource.vo.req.htLamp;

import lombok.Data;

/**
 * 归属设置类
 * @Author: Xiaok
 * @Date: 2020/8/18 17:22
 */
@Data
public class HtSetRelegationRequestVO {

    /**
     * io检测支路归属列表，默认为８项  HtBranchOwnershipEnum
     */
    private int[] io;

    /**
     * 模拟量检测支路归属列表，默认为20项，其中前三项为电压检测  HtBranchOwnershipEnum
     */
    private int[] analog;
}
