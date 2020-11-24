package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class IssueResponse {


    //设备命令 ID
    private String commandId;
    //设备命令所属应用 ID
    private String appId;
    //下发命令的设备 ID
    private String deviceId;
    //下发命令的信息
    private CommandDTO command;
    //命令状态变化通知地址
    private String callbackUrl;
    //下发命令的超时时间
    private Integer expireTime;
    //下发命令的状态
    private String status;
    //命令的创建时间
    private String creationTime;
    //命令执行的时间
    private String executeTime;
    //平台发送命令的时间
    private String platformIssuedTime;
    //平台将命令送达到设备的时间
    private String deliveredTime;
    //平台发送命令的次数
    private Integer issuedTimes;
    //命令下发最大重传次数
    private Integer maxRetransmit;

}
