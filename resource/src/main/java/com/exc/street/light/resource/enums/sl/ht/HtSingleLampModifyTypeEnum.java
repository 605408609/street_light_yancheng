package com.exc.street.light.resource.enums.sl.ht;

/**
 * 单灯控制器编辑操作类型
 * @Author: Xiaok
 * @Date: 2020/7/25 9:19
 */
public enum HtSingleLampModifyTypeEnum {
    UNKNOWN(0, "未知"),
    SAVE(1, "节点新增或替换操作"),
    DELETE(2, "节点删除操作"),
    DELETE_ALL(3, "删除所有节点信息");

    private int code;
    private String type;

    public int code() {
        return code;
    }

    public String type() {
        return type;
    }
    HtSingleLampModifyTypeEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static String getTypeByCode(int code) {
        for (HtSingleLampModifyTypeEnum c : HtSingleLampModifyTypeEnum.values()) {
            if (c.code() == code) {
                return c.type;
            }
        }
        return null;
    }
}
