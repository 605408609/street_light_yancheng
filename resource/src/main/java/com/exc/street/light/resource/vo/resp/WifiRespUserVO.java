package com.exc.street.light.resource.vo.resp;

import com.exc.street.light.resource.core.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Xiezhipeng
 * @Description wifi用户返回视图类
 * @Date 2020/3/27
 */
@Data
public class WifiRespUserVO extends PageParam {

    /**
     * wifi用户id
     */
    private Integer id;

    /**
     * 用户Mac
     */
    private String userMac;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 终端类型
     */
    private String terminal;

    /**
     * 认证时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date certifTime;

    /**
     * 认证AP位置
     */
    private String location;

    /**
     * 认证结果
     */
    private Integer certifResult;

    /**
     * 下线时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date logoutTime;

    /**
     * 上网时长
     */
    private Integer internetTime;

    /**
     * 上网流量
     */
    private Integer internetTraffic;
}
