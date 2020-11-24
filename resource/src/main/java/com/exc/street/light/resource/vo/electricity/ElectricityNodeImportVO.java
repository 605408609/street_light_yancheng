package com.exc.street.light.resource.vo.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 节点导入接收
 *
 * @author Linshiwen
 * @date 2018/5/25
 */
@Setter
@Getter
@ToString
public class ElectricityNodeImportVO {
    //@Excel(name = "节点名称", orderNum = "0")
    private String name;

    /**
     * 编号
     */
    //@Excel(name = "节点编号", orderNum = "1")
    private String num;

    /**
     * 省
     */
    //@Excel(name = "省", orderNum = "2")
    private String province;

    /**
     * 市
     */
    //@Excel(name = "市", orderNum = "3")
    private String city;

    /**
     * 区
     */
    //@Excel(name = "区", orderNum = "4")
    private String district;

    /**
     * 地址
     */
    //@Excel(name = "地址", orderNum = "5")
    private String addr;

    /**
     * ip
     */
    //@Excel(name = "IP", orderNum = "6")
    private String ip;

    /**
     * 端口
     */
    //@Excel(name = "端口", orderNum = "7")
    private String port;

    /**
     * 安装位置
     */
    //@Column(name = "install_addr")
    //@Excel(name = "安装位置", orderNum = "8")
    private String installAddr;

    /**
     * 施工单位
     */
    //@Excel(name = "施工单位", orderNum = "9")
    private String constructionUnits;

}