package com.exc.street.light.ss.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.ss.common.constants.GlobalConstant;
import com.exc.street.light.ss.common.enums.ProtocolEnum;
import com.exc.street.light.ss.common.enums.StreamTypeEnum;
import com.exc.street.light.ss.common.enums.TransModeEnum;
import com.exc.street.light.ss.config.HikArtemisConfig;
import com.exc.street.light.ss.dto.PreviewUrlsDTO;
import com.exc.street.light.ss.dto.request.PreviewUrlsRequestDTO;
import com.exc.street.light.ss.dto.response.PreviewUrlsResponseDTO;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * CamerasPreviewUrlsManager 海康威视第三方接口Manager
 *
 * @author liufei
 * @date 2020/06/04
 */
@Service
public class CamerasPreviewUrlsManager {

    private static final Logger logger= LoggerFactory.getLogger(CamerasPreviewUrlsManager.class);

    @Autowired
    private HikArtemisConfig hikArtemisConfig;

    public PreviewUrlsDTO getPreviewUrls(PreviewUrlsRequestDTO previewUrlsRequestDTO) {
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

    public PreviewUrlsDTO getPreviewUrlsDtoByNum(String num) {
        PreviewUrlsDTO previewUrlsDTO = null;
        if (StringUtils.isNotBlank(num)) {
            PreviewUrlsRequestDTO previewUrlsRequestDTO = new PreviewUrlsRequestDTO();
            previewUrlsRequestDTO.setCameraIndexCode(num);
            previewUrlsRequestDTO.setStreamType(StreamTypeEnum.MAIN_CODE_STREAM.getCode());
            previewUrlsRequestDTO.setProtocol(ProtocolEnum.HLS.getCode());
            previewUrlsRequestDTO.setTransMode(TransModeEnum.TCP.getCode());
            // previewUrlsRequestDTO.setExpand();

            previewUrlsDTO = getPreviewUrls(previewUrlsRequestDTO);
        }
        return previewUrlsDTO;
    }

}
