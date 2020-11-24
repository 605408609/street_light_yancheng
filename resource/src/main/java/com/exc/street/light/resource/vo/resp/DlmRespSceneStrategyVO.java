package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Auther: Xiezhipeng
 * @Description: 场景策略返回视图类
 * @Date: 2020/11/9 10:13
 */
@Data
public class DlmRespSceneStrategyVO {

    @ApiModelProperty(name = "id" , value = "回路场景策略表，主键id自增")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "场景名称")
    private String name;

    @ApiModelProperty(name = "creator" , value = "创建人id")
    private Integer creator;

    @ApiModelProperty(name = "creatorName" , value = "创建人名称")
    private String creatorName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "startDate" , value = "开始日期")
    private String startDate;

    @ApiModelProperty(name = "endDate" , value = "结束日期")
    private String endDate;

    @ApiModelProperty(name = "dlmRespSceneActionVOList" , value = "场景动作集合")
    private List<DlmRespSceneActionVO> dlmRespSceneActionVOList;

}
