package com.exc.street.light.resource.vo.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备excel导入失败vo
 * @Author: Xiaok
 * @Date: 2020/11/13 11:37
 */
@Data
@Accessors(chain = true)
public class ImportDeviceFailVO {
    /**
     * 行数/行列数
     */
    private String rows;

    /**
     * 报错信息列表
     */
    private List<String> failMsgList = new ArrayList<>();
}
