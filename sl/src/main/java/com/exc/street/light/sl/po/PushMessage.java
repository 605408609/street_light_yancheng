package com.exc.street.light.sl.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: XuJiaHao
 * @Description: 推送消息类
 * @Date: Created in 14:16 2020/4/2
 * @Modified:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PushMessage {
    @ApiModelProperty(value = "用户的token")
    private String userToken;

    @ApiModelProperty(value = "推送的内容")
    private String content;

    @ApiModelProperty(value = "用户id")
    private String userId;
}
