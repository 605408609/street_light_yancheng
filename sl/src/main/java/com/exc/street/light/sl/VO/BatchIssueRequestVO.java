package com.exc.street.light.sl.VO;

import lombok.Data;

import java.util.List;

@Data
public class BatchIssueRequestVO {

    //批量命令类型
    private String type;
    //设备 ID 列表
    private List<String> deviceList;
    //设备类型
    private String deviceType;
    //厂商 ID
    private String manufacturerId;
    //设备型号
    private String model;
    //设备位置
    private String deviceLocation;
    //群组 Id 列表或设备组名称列表
    private List<String> groupList;
    //命令信息
    private CommandDTO command;
    //命令执行结果的推送地址
    private String callbackUrl;
    //命令下发最大重传次数取值范围：0-3
    private String maxRetransmit;
    //群组标签
    private String groupTag;
}
