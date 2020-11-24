package com.exc.street.light.ss.manager;

import com.exc.street.light.ss.common.enums.ProtocolEnum;
import com.exc.street.light.ss.common.enums.StreamTypeEnum;
import com.exc.street.light.ss.common.enums.TransModeEnum;
import com.exc.street.light.ss.dto.PreviewUrlsDTO;
import com.exc.street.light.ss.dto.request.PreviewUrlsRequestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * CamerasPreviewUrlsManagerTest
 *
 * @author liufei
 * @date 2020/06/05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CamerasPreviewUrlsManagerTest {

    @Autowired
    private CamerasPreviewUrlsManager camerasPreviewUrlsManager;

    @Test
    public void getCameraPreviewUrl() {
        PreviewUrlsRequestDTO previewUrlsRequestDTO = new PreviewUrlsRequestDTO();
        previewUrlsRequestDTO.setCameraIndexCode("c4cdf66ba4874fb7b294a79cc2b7f0d7");
        previewUrlsRequestDTO.setStreamType(StreamTypeEnum.MAIN_CODE_STREAM.getCode());
        previewUrlsRequestDTO.setProtocol(ProtocolEnum.HLS.getCode());
        previewUrlsRequestDTO.setTransMode(TransModeEnum.TCP.getCode());
        // previewUrlsRequestDTO.setExpand();
        PreviewUrlsDTO previewUrlsDTO = camerasPreviewUrlsManager.getPreviewUrls(previewUrlsRequestDTO);
        System.out.println("result:" + previewUrlsDTO.toString());
    }

}
