package com.exc.street.light.electricity.web;

import com.exc.street.light.electricity.service.ComChannelService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 串口回路控制器
 *
 * @author Linshiwen
 * @date 2018/07/04
 */
@Api(tags = "串口回路控制器")
@RestController
@RequestMapping("/api/com/channel")
public class ComChannelController {

    private Logger logger = LoggerFactory.getLogger(CanChannelController.class);

    @Value("${upload.path}")
    public String uploadPath;
    @Autowired
    private ComChannelService comChannelService;

    /**
     * 点表txt文件导入回路
     *
     * @param file
     * @return
     */
    /*@PostMapping("/txt/import")
    public Result batchImport(MultipartFile file, Integer nid) {
        if (file.isEmpty()) {
            return ResultGenerator.getFailResult("文件为空");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.info("上传的后缀名为：" + suffixName);
        String flag = "txt";
        if (!flag.equals(suffixName)) {
            return ResultGenerator.getFailResult("文件类型错误,不是txt文件");
        }
        String realName = UUID.randomUUID() + "." + suffixName;
        File dest = new File(uploadPath + realName);
        Result result = null;
        try {
            file.transferTo(dest);
            List<ComChannel> comChannels = ReadTxtUtil.readComChannel(dest);
            result = comChannelService.saveList(comChannels, nid);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dest.delete();
        }
        return result;

    }*/
}
