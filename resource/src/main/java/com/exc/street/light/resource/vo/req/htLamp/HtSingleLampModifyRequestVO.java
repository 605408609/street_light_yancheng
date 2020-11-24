package com.exc.street.light.resource.vo.req.htLamp;

import com.exc.street.light.resource.enums.sl.ht.HtSingleLampModifyTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * 华体单灯控制器 更新(插入 修改 删除)类
 *
 * @Author: Xiaok
 * @Date: 2020/8/13 15:34
 */
@Getter
@Setter
@ToString
public class HtSingleLampModifyRequestVO {

    /**
     * 操作类型
     */
    private HtSingleLampModifyTypeEnum typeEnum;

    /**
     * 单灯控制器map   key:序号  value:通讯地址
     */
    private Map<Integer, String> deviceMap;
}
