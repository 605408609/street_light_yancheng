/**
 * @filename:SsDeviceServiceImpl 2020-03-17
 * @project ss  V1.0
 * Copyright(c) 2018 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ss.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.SsDeviceVO;
import com.exc.street.light.ss.common.constants.GlobalConstant;
import com.exc.street.light.ss.common.enums.ProtocolEnum;
import com.exc.street.light.ss.common.enums.StreamTypeEnum;
import com.exc.street.light.ss.common.enums.TransModeEnum;
import com.exc.street.light.ss.config.HikArtemisConfig;
import com.exc.street.light.ss.dto.PreviewUrlsDTO;
import com.exc.street.light.ss.dto.request.PreviewUrlsRequestDTO;
import com.exc.street.light.ss.dto.response.PreviewUrlsResponseDTO;
import com.exc.street.light.ss.mapper.SsDeviceDao;
import com.exc.street.light.ss.service.SsDeviceService;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Huang Min
 * 
 */
@Service
public class SsDeviceServiceImpl  extends ServiceImpl<SsDeviceDao, SsDevice> implements SsDeviceService  {
	private static final Logger logger= LoggerFactory.getLogger(SsDeviceServiceImpl.class);

    @Autowired
    private LogUserService userService;

//	@Autowired
//    private CamerasPreviewUrlsManager camerasPreviewUrlsManager;
	
	@Autowired
	private HikArtemisConfig hikArtemisConfig;


    
    /* (non-Javadoc)
     * @see com.exc.street.light.occ.service.SlDeviceService#uniqueness(com.exc.street.light.occ.vo.SlDeviceVO)
     */
    @Override
	public Result uniqueness(SsDevice ssDevice) {
        List<SsDevice> listSlDevice = baseMapper.selectList(null);
        
        for (SsDevice slDevice : listSlDevice) {
        	
            if (slDevice.getName().equals(ssDevice.getName())) {
            	//当ID不为空，且等于当前节点ID，即为对某个节点进行编辑操作，校验时不判断设备名称和序列号，直接通过
            	if (slDevice.getId() != null && slDevice.getId() != ssDevice.getId()) {
            		return new Result<>().error("设备名称已存在,请重新输入"); 
            	}
            }
            if (slDevice.getNum().equals(ssDevice.getNum())) {
              	//当ID不为空，且等于当前节点ID，即为对某个节点进行编辑操作，校验时不判断设备名称和序列号，直接通过
            	if (slDevice.getId() != null && slDevice.getId() != ssDevice.getId()) {
            		return new Result<>().error("设备序列号已存在,请重新输入");
            	}
            }
            
            
        }

        return new Result<>().success("唯一性校验通过!");
    }
    
    /* (non-Javadoc)
     * @see com.exc.street.light.occ.service.SlDeviceService#page(com.baomidou.mybatisplus.extension.plugins.pagination.Page, com.baomidou.mybatisplus.core.conditions.query.QueryWrapper)
     */
    @Override
    public IPage<SsDeviceVO> page(Page<SsDeviceVO> page, QueryWrapper<SsDeviceVO> queryWrapper, SsDeviceVO t, HttpServletRequest request) {
    	// 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            t.setAreaId(user.getAreaId());
        }  
    	return baseMapper.selectPage(page, queryWrapper,t);
    }

    
    /* (non-Javadoc)
     * @see com.exc.street.light.occ.service.AhDeviceService#pulldownByLampPost(java.util.List, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request) {
        LambdaQueryWrapper<SsDevice> wrapper = new LambdaQueryWrapper();
        if (lampPostIdList != null && lampPostIdList.size() > 0) {
            wrapper.in(SsDevice::getLampPostId, lampPostIdList);
        }
        List<SsDevice> list = this.list(wrapper);
        //列表接口提供，不获取预览流地址
//        if (!CollectionUtils.isEmpty(list)) {
//            list.forEach(ssDevice -> {
//                String num = ssDevice.getNum();
//                if (StringUtils.isNotBlank(num)) {
//                    PreviewUrlsDTO previewUrlsDTO = getPreviewUrlsDTOByNum(num);
//                    ssDevice.setCameraPreviewUrl(previewUrlsDTO != null ? previewUrlsDTO.getCameraPreviewUrl() : null);
//                	ssDevice.setCameraPreviewUrl(null);
//                }
//            });
//        }
        Result result = new Result();
        return result.success(list);
    }

	@Override
	public IPage<SsDeviceVO> selectByIdWithApp(Page<SsDeviceVO> page,Long id) {
		// TODO Auto-generated method stub
        IPage<SsDeviceVO> pageData = baseMapper.selectByIdWithApp(page, id);
        pageData.getRecords().forEach(ssDeviceVO -> {
            String num = ssDeviceVO.getNum();
            if (StringUtils.isNotBlank(num)) {
                PreviewUrlsDTO previewUrlsDTO = getPreviewUrlsDTOByNum(num);
                ssDeviceVO.setCameraPreviewUrl(previewUrlsDTO != null ? previewUrlsDTO.getCameraPreviewUrl() : null);
            }
        });
		return pageData;
	}
	protected PreviewUrlsDTO getPreviewUrlsDTOByNum(String num) {
        PreviewUrlsDTO previewUrlsDTO = null;
        if (StringUtils.isNotBlank(num)) {
            PreviewUrlsRequestDTO previewUrlsRequestDTO = new PreviewUrlsRequestDTO();
            previewUrlsRequestDTO.setCameraIndexCode(num);
            previewUrlsRequestDTO.setStreamType(StreamTypeEnum.MAIN_CODE_STREAM.getCode());
            previewUrlsRequestDTO.setProtocol(ProtocolEnum.HLS.getCode());
            previewUrlsRequestDTO.setTransMode(TransModeEnum.UDP.getCode());
            // previewUrlsRequestDTO.setExpand();

            previewUrlsDTO = getPreviewUrls(previewUrlsRequestDTO);
        }
        return previewUrlsDTO;
    }
	
	protected PreviewUrlsDTO getPreviewUrls(PreviewUrlsRequestDTO previewUrlsRequestDTO) {
	        String cameraIndexCode = previewUrlsRequestDTO.getCameraIndexCode();
	        Integer streamType = previewUrlsRequestDTO.getStreamType();
	        String protocol = previewUrlsRequestDTO.getProtocol();
	        Integer transMode = previewUrlsRequestDTO.getTransMode();
	        String expand = previewUrlsRequestDTO.getExpand();

	        /**
	         * STEP1：设置平台参数，根据实际情况，设置host、appkey、appsecret三个参数.
	         */
	        // artemis网关服务器ip端口
	        ArtemisConfig.host = hikArtemisConfig.getHost();
	        // 秘钥appkey
	        ArtemisConfig.appKey = hikArtemisConfig.getAppKey();
	        // 秘钥appSecret
	        ArtemisConfig.appSecret = hikArtemisConfig.getAppSecret();

	        /**
	         * STEP2：设置OpenAPI接口的上下文
	         */
	        final String ARTEMIS_PATH = "/artemis";

	        /**
	         * STEP3：设置接口的URI地址
	         */
	        final String previewURLsApi = ARTEMIS_PATH + "/api/video/v1/cameras/previewURLs";
	        Map<String, String> path = new HashMap<String, String>(2) {
	            {
	                // 根据现场环境部署确认是http还是https
	                put(hikArtemisConfig.getHttpType(), previewURLsApi);
	            }
	        };

	        /**
	         * STEP4：设置参数提交方式
	         */
	        String contentType = "application/json";

	        /**
	         * STEP5：组装请求参数
	         */
	        JSONObject jsonBody = new JSONObject();
	        if (StringUtils.isBlank(cameraIndexCode)) {
	            logger.error("请求参数错误，cameraIndexCode字段不能为空");
	            return null;
	        }
	        jsonBody.put("cameraIndexCode", cameraIndexCode);
	        if (ObjectUtils.isNotEmpty(streamType)) {
	            jsonBody.put("streamType", streamType);
	        }
	        if (StringUtils.isNotBlank(protocol)) {
	            jsonBody.put("protocol", protocol);
	        }
	        if (ObjectUtils.isNotEmpty(transMode)) {
	            jsonBody.put("transmode", transMode);
	        }
	        if (StringUtils.isNotBlank(expand)) {
	            jsonBody.put("expand", expand);
	        }
	        String body = jsonBody.toJSONString();

	        /**
	         * STEP6：调用接口
	         */
	        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
	        if (StringUtils.isBlank(result)) {
	            logger.error("无法访问海康威视服务");
	            return null;
	        }
	        logger.debug("请求路径：[{}]，请求参数：[{}]，返回结果：[{}]" , previewURLsApi, body, result);
	        // 解析返回数据
	        Object resultObject = JSON.parse(result);
	        if (!(resultObject instanceof JSONObject)) {
	            logger.error("无法解析海康威视服务返回的数据");
	            return null;
	        }
	        JSONObject resultJsonObject = (JSONObject)resultObject;
	        String code = resultJsonObject.getString("code");
	        String msg = resultJsonObject.getString("msg");
	        if (!(GlobalConstant.HIKVISION_SUCCESS_CODE.equalsIgnoreCase(code)) && !(GlobalConstant.HIKVISION_SUCCESS_MSG.equalsIgnoreCase(msg))) {
	            logger.error("调用海康威视服务接口错误：code:[{}],msg:[{}]", code, msg);
	            return null;
	        }
	        PreviewUrlsResponseDTO previewUrlsResponseDTO = resultJsonObject.getObject("data", PreviewUrlsResponseDTO.class);
	        PreviewUrlsDTO previewUrlsDTO = new PreviewUrlsDTO();
	        previewUrlsDTO.setCameraPreviewUrl(previewUrlsResponseDTO.getUrl());

	        return previewUrlsDTO;
	    }

}