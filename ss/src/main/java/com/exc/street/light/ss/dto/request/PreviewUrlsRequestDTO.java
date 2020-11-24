package com.exc.street.light.ss.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * PreviewUrlsRequestDTO 获取监控点预览取流URL请求参数对象
 *
 * @author liufei
 * @date 2020/06/05
 */
@Getter
@Setter
@ToString
public class PreviewUrlsRequestDTO {

    /**
     * 监控点编号
     */
    private String cameraIndexCode;

    /**
     * 码流类型
     */
    private Integer streamType;

    /**
     * 协议类型
     */
    private String protocol;

    /**
     * 协议类型
     */
    private Integer transMode;

    /**
     * 拓展字段
     */
    private String expand;

}
