package com.exc.street.light.resource.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 添加节目接口接收参数
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class IrReqScreenProgramVO {

    @ApiModelProperty(name = "playId" , value = "播放列表id")
    private Integer playId;

    @ApiModelProperty(name = "id" , value = "节目表id，自增")
    private Integer id;

    @ApiModelProperty(name = "name" , value = "节目名称")
    private String name;

    @ApiModelProperty(name = "description" , value = "节目描述")
    private String description;

    @ApiModelProperty(name = "totalSize" , value = "所有素材size总和（单位：B  字节）")
    private Integer totalSize;

    @ApiModelProperty(name = "creator" , value = "创建人（运营原型出来后为不可空，关联用户）")
    private Integer creator;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "updateTime" , value = "修改时间")
    private Date updateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    /**
     * 素材关系对象
     */
    @ApiModelProperty(name = "programMaterialArrayList" , value = "素材关系对象")
    private List<IrReqScreenProgramMaterialVO> programMaterialArrayList;

    /**
     * 用户名称
     */
    @ApiModelProperty(name = "creatorName" , value = "用户名称")
    private String creatorName;

    /**
     * 节目发布接收参数
     */
    @ApiModelProperty(name = "irReqSendProgramVO" , value = "节目发布接收参数")
    private IrReqSendProgramVO irReqSendProgramVO;

    /**
     * 节目是否可以审核
     *
     * */
    @ApiModelProperty(name = "isCanVerify" , value = "是否可以审核 0-否 1-是")
    private transient Integer isCanVerify;

    /**
     * 节目审核状态
     *
     */
    @ApiModelProperty(name = "verifyStatus" , value = "审核状态(0-待审核 1-审核成功 2-审核失败)")
    private Integer verifyStatus;

}
