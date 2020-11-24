package com.exc.street.light.resource.vo.sl;

import lombok.Data;

@Data
public class LoraCreateNodeParam {

    private int appID;//应用ID
    private String devName;//节点名称
    private String devEUI;//16位16进制符串，节点设备唯一标识符
    private String appEUI;//16位16进制符串，节点应用唯一标识符
    private String region;//节点所属区域
    private String subnet;//节点所属子网
    private boolean supportClassB;//节点是否支持classB
    private boolean supportClassC;//节点是否支持classC
    private String authType;//认证类型，可选abp或otaa
    private String appKey;//32位16进制字符串，otaa入网必填
    private String devAddr;//8位16进制字符串，abp入网必填
    private String appSKey;//32位16进制字符串，abp入网必填
    private String nwkSKey;//32位16进制字符串，abp入网必填
    private String macVersion;//LoRaWAN协议版本，可选1.0.2或1.0.3，默认为1.0.2



}
