package com.exc.street.light.resource.vo.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 下载信息
 *
 * @author Linshiwen
 * @date 2018/6/15
 */
@Setter
@Getter
@ToString
public class SceneDownLoadVO {
    private Integer successNum;
    private Integer defaultNum;
    private List<String> nums;
}
