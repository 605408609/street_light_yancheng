package com.exc.street.light.resource.vo.resp;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备导入结果类
 *
 * @Author: Xiaok
 * @Date: 2020/11/13 11:34
 */
@Data
public class ImportDeviceResultVO {

    /**
     * 成功数
     */
    private Integer successNum = 0;

    /**
     * 失败数
     */
    private Integer failNum = 0;

    /**
     * 失败信息
     */
    private List<ImportDeviceFailVO> failInfoList = new ArrayList<>();
}
