package com.exc.street.light.resource.enums.sl.ht;

/**
 * 支路归属
 * @Author: Xiaok
 * @Date: 2020/7/24 18:39
 */
public enum HtBranchOwnershipEnum {
    NONE(0, "无"),
    CONTROL_1(1, "控1"),
    CONTROL_2(2, "控2"),
    CONTROL_3(3, "控3"),
    CONTROL_4(4, "控4"),
    WITH_INPUT(5, "有输入"),
    NO_INPUT(6, "无输入");


    private int code;
    private String type;

    HtBranchOwnershipEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int code() {
        return code;
    }

    public String type() {
        return type;
    }

    public static String getTypeByCode(int code) {
        for (HtBranchOwnershipEnum c : HtBranchOwnershipEnum.values()) {
            if (c.code() == code) {
                return c.type;
            }
        }
        return null;
    }

}
