package com.exc.street.light.resource.enums.sl.shuncom;

/**
 * 顺舟云盒 命令类型
 *
 * @author Xiaok
 * @date 2020/8/20 14:27
 */
public enum CommandTypeEnums {

    REPORT_HEART_BEAT(101, "网关上传心跳包", 1001),
    REPORT_DEVICE_CONTROL(102, "云平台控制设备", 1002),
    ORDER_DEVICE_DELETE(103, "网关，设备删除", 1003),
    REPORT_DEVICE_INFO(104, "上报网关信息、上报设备状态改变、子设备添加上报、上报设备告警", 1004),
    REPORT_DEVICE_REGISTER(105, "新设备注册", 1005),
    ORDER_SETTING_SEARCH_OR_MODIFY(106, "修改/查询配置", 1006),
    ORDER_DELETE_ALL_DEVICE(114, "云平台删除所有设备", 1014),

    ORDER_GROUP_CREATE(201, "云平台创建分组", 2001),
    ORDER_GROUP_MODIFY(202, "云平台修改分组", 2002),
    ORDER_GROUP_REQUEST_SETTING(203, "云平台设置分组状态请求命令", 2003),
    ORDER_GROUP_DELETE(204, "云平台删除分组", 2004),
    REPORT_GROUP_DELETE(205, "网关上报删除分组,网关修改分组,网关创建分组", 2005),
    ORDER_GET_GROUP_INFO(206, "平台获取所有分组信息、平台获取指定分组信息", 2006),
    ORDER_GROUP_DELETE_ALL(207, "平台删除所有分组", 2007),

    ORDER_STRATEGY_ADD_OR_MODIFY(401, "云平台创建/修改策略", 4001),
    ORDER_STRATEGY_SET_STATE(402, "云平台设置策略使能", 4002),
    ORDER_STRATEGY_DELETE(403, "云平台删除策略", 4003),
    REPORT_STRATEGY_DELETE(404, "网关删除策略上报、网关修改策略上报、网关创建策略上报、网关策略触发上报", 4004),
    ORDER_STRATEGY_GET_INFO(405, "云平台查询策略信息", 4005);

    /**
     * code
     */
    private int code;
    /**
     * 名称
     */
    private String name;
    /**
     * 返回code
     */
    private Integer orderCode;

    CommandTypeEnums(int code, String name, Integer orderCode) {
        this.code = code;
        this.name = name;
        this.orderCode = orderCode;
    }

    public int code() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer orderCode() {
        return orderCode;
    }

    public static String getTypeByCode(int code) {
        for (CommandTypeEnums c : CommandTypeEnums.values()) {
            if (c.code() == code) {
                return c.name;
            }
        }
        return null;
    }

    public static CommandTypeEnums getByCode(int code) {
        for (CommandTypeEnums c : CommandTypeEnums.values()) {
            if (c.code() == code) {
                return c;
            }
        }
        return null;
    }
}
