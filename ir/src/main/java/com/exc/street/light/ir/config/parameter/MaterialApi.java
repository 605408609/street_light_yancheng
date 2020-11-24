package com.exc.street.light.ir.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "material")
public class MaterialApi {

    /**
     * 背景色
     */
    private String backgroundColor;

    /**
     * 文本翻页等待时长（单位：S 秒）
     */
    private Integer speed;

    /**
     * 请求发送超时时间
     */
    private Double lineHeight;

    /**
     * 多次字幕间的间隔
     */
    private Integer center;

    /**
     * 素材服务
     */
    private String servicePath;

    /**
     * 上传节目进度条
     */
    private String progressInterface;

    /**
     * 允许上传的素材格式(图片)
     */
    private String imageSuffix;

    /**
     * 允许上传的素材格式(视频)
     */
    private String videoSuffix;

}