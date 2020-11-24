package com.exc.street.light.ed.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: XuJiaHao
 * @Description: 数据传输序列化类
 * @Date: Created in 20:58 2020/4/2
 * @Modified:
 */
@Data
public class KafkaMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "传输数据类型")
    private int type;
    @ApiModelProperty(value = "传输数据")
    private String Message;
    @ApiModelProperty(value = "用户id数组")
    private String[] userIds;
    @ApiModelProperty(value = "是否群发: 1、群发 2、以用户id")
    private int is2All;
}
