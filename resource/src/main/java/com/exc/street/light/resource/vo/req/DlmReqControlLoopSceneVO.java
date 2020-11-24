package com.exc.street.light.resource.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 回路场景下发对象类
 * @Date 2020/8/25
 */
@Data
public class DlmReqControlLoopSceneVO {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "executionTime" , value = "场景执行时间")
    private Date executionTime;

    @ApiModelProperty(name = "isOpen" , value = "开关状态（0：关，1：开）")
    private Integer isOpen;

    @ApiModelProperty(name = "loopIdList" , value = "回路id集合")
    private List<Integer> loopIdList;

}
