/**
 * @filename:SystemDeviceThreshold 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.vo.req;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SlReqThresholdAddListVO {

	@ApiModelProperty(name = "slReqThresholdAddVOList" , value = "设备阈值数据集合")
	private List<SlReqThresholdAddVO> slReqThresholdAddVOList;

	@ApiModelProperty(name = "deviceIdList" , value = "设备id集合")
	private List<Integer> deviceIdList;
}
