package com.exc.street.light.resource.enums.sl.shuncom;

/**
 * 顺舟云盒 返回结果类型
 * @Author: Xiaok
 * @Date: 2020/8/20 18:17
 */
public enum ResultEnums {

    SUCCESS(0, "成功"),
    MEMORY_ALLOCATION_FAILED(1, "内存分配失败"),
    PARAM_ERROR(2, "参数错误"),
    EXECUTE_FAIL(3, "执行失败"),
    object_not_exists(4, "对象/资源不存在");


    /**
     * code
     */
    private int code;
    /**
     * 名称
     */
    private String name;

    ResultEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int code() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static String getTypeByCode(int code) {
        for (ResultEnums c : ResultEnums.values()) {
            if (c.code() == code) {
                return c.name;
            }
        }
        return null;
    }

    public static ResultEnums getByCode(int code) {
        for (ResultEnums c : ResultEnums.values()) {
            if (c.code() == code) {
                return c;
            }
        }
        return null;
    }
}
