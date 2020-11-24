package com.exc.street.light.resource.dto.sl.ht;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.enums.sl.ht.HtCommandTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 华体集控器消息生成工具类的返回结果
 * @Author: Xiaok
 * @Date: 2020/9/2 11:21
 */
@Data
@AllArgsConstructor
public class HtCommandDTO {

    /**
     * 添加的json 由工具类方法生成
     */
    private JSONObject dataObj;

    /**
     * 命令类型
     */
    private HtCommandTypeEnum cmdTypeEnum;
}
