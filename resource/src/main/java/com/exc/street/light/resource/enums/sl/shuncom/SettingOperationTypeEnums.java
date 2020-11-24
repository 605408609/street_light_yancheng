package com.exc.street.light.resource.enums.sl.shuncom;

/**
 * 顺舟云盒 修改/查询配置 类型
 * @Author: Xiaok
 * @Date: 2020/8/24 11:04
 */
public enum SettingOperationTypeEnums {

    CREATE_OR_MODIFY_MAPPING(0, "创建/修改 映射关系"),
    SET_LOOP_DEVICE_MAPPING(1, "设置网关下回路设备的映射关系"),
    SEARCH_GATEWAY_LOOP_DEVICE_MAPPING(2, "查询网关下回路设备的映射关系"),
    SEARCH_GATEWAY_ALL_APP_VERSION(3, "查询网关下各应用的版本"),
    SEARCH_GATEWAY_VERSION(4, "查询网关版本"),
    SET_GATEWAY_TIME(5, "设置网关时间"),
    SEARCH_GATEWAY_TIME(6, "查询网关时间"),
    SEARCH_GATEWAY_LAT_AND_LON(7, "查询网关经纬度"),
    SET_AP_MODE_DISTRIBUTION_NETWORK(8, "ap 模式配网"),
    SEARCH_INFRARED_FORWARDING_LEARNING_CODE(9, "查询红外转发学习码"),
    MODIFY_INFRARED_FORWARDING_LEARNING_CODE(10, "修改红外转发学习码说明"),
    DELETE_INFRARED_FORWARDING_LEARNING_CODE(11, "删除红外转发学习码"),
    ADD_VOICE_PRINT_USER_PERMISSIONS(12, "增加声纹用户权限"),
    DELETE_VOICE_PRINT_USER_PERMISSIONS(13, "删除声纹用户权限"),
    MODIFY_VOICE_PRINT_USER_PERMISSIONS(14, "修改声纹用户权限"),
    SEARCH_VOICE_PRINT_USER_PERMISSIONS(15, "获取声纹用户权限"),
    ADD_LIGHT_CONTROL_BLACKLIST(16, "增加灯控器黑名单"),
    DELETE_LIGHT_CONTROL_BLACKLIST(17, "删除灯控器黑名单"),
    SEARCH_LIGHT_CONTROL_BLACKLIST(18, "获取灯控器黑名单"),
    SEARCH_LIGHT_CONTROL_NOT_RECOGNIZED_ADDR(19, "查询灯控器不识别地址"),
    SET_GATEWAY_MODE(20, "设置网关模式"),
    SEARCH_DEVICE_POWER_CONSUMPTION(21, "查询网关下设备耗电量");


    /**
     * code
     */
    private int code;
    /**
     * 名称
     */
    private String name;

    SettingOperationTypeEnums(int code, String name) {
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
        for (SettingOperationTypeEnums c : SettingOperationTypeEnums.values()) {
            if (c.code() == code) {
                return c.name;
            }
        }
        return null;
    }

    public static SettingOperationTypeEnums getByCode(int code) {
        for (SettingOperationTypeEnums c : SettingOperationTypeEnums.values()) {
            if (c.code() == code) {
                return c;
            }
        }
        return null;
    }
}
