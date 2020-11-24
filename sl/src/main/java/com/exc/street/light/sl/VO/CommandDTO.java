package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class CommandDTO {

    //命令对应的服务 ID
    private String serviceId;
    //命令服务下具体的命令名称
    private String method;
    //命令参数
    private ObjectNode paras;
}
