/**
 * @filename:RadioMaterialServiceImpl 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2018 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.pb.mapper.RadioMaterialDao;
import com.exc.street.light.pb.service.RadioMaterialService;
import com.exc.street.light.pb.service.RadioPlayService;
import com.exc.street.light.pb.service.RadioProgramMaterialService;
import com.exc.street.light.pb.utils.DateUtil;
import com.exc.street.light.pb.utils.FTPUtil2;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.pb.RadioMaterial;
import com.exc.street.light.resource.entity.pb.RadioProgramMaterial;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.PbRadioMaterialQueryObject;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: LeiJing
 */
@Service
public class RadioMaterialServiceImpl extends ServiceImpl<RadioMaterialDao, RadioMaterial> implements RadioMaterialService {
    private static final Logger logger = LoggerFactory.getLogger(RadioMaterialServiceImpl.class);

    @Autowired
    private RadioMaterialDao radioMaterialDao;

    @Autowired
    private RadioPlayService radioPlayService;

    @Autowired
    private RadioProgramMaterialService programMaterialService;

    @Value("${upload.path}")
    private String uploadPath;


    @Autowired
    private LogUserService userService;

    @Override
    public Result add(MultipartFile file, HttpServletRequest request) throws IOException {
        logger.info("新增公共广播素材，接收参数：file = {}", file);
        Result result = new Result();
        if (file.isEmpty()) {
            logger.info("文件上传失败，文件不存在！");
            return result.error("文件上传失败，文件不存在！");
        }

        //获取文件后缀名
        String OriginalFilename = file.getOriginalFilename();
        String suffix = OriginalFilename.substring(OriginalFilename.lastIndexOf(".") + 1);
        if (!suffix.equals("mp3")) {
            logger.info("文件上传失败，不是mp3文件！");
            return result.error("文件上传失败，不是mp3文件！");
        }
        //判断上传路径文件夹是否存在，不存在则创建文件夹
        File srcDirPath = new File(uploadPath);
        if (!srcDirPath.exists()) {
            srcDirPath.mkdirs();
        }

        //获取雷拓IP广播平台音频文件上传FTP信息
        JSONObject fileJson = new JSONObject();
        Integer fileType = 1;
        fileJson.put("Type", fileType);
        JSONObject returnJson = radioPlayService.interfaceCall("FileUpload", fileJson);
        if (returnJson != null && "0".equals(returnJson.getString("Ret"))) {
            //文件 ID, 文件在服务器上的唯一标识
            String fileId = returnJson.getString("FileId");
            //文件上传的地址
            String ftpUrl = returnJson.getString("FtpUrl");
            //FTP 服务器用户名
            String ftpUsr = returnJson.getString("FtpUsr");
            //FTP 服务器密码
            String ftpPwd = returnJson.getString("FtpPwd");

            //传入文件对象，文件保存至服务器
            String fileName = fileId + "." + suffix;
            String srcFileUrl = uploadPath + fileName;
            File srcFile = new File(srcFileUrl);
            try {
                file.transferTo(srcFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //FTP上传音频文件至雷拓IP广播平台服务器文件夹
            //FTP文件上传
            String hostname = ftpUrl.substring(ftpUrl.lastIndexOf("://") + 3, ftpUrl.lastIndexOf(":"));
            //hostname="129.204.186.242";
            Integer port = Integer.valueOf(ftpUrl.substring(ftpUrl.lastIndexOf(":") + 1, ftpUrl.lastIndexOf(":") + 5));
            boolean uploadBool = FTPUtil2.upload(hostname, port, ftpUsr, ftpPwd, "/", srcFile);
            //boolean uploadBool = FtpUtil.uploadMP3File(hostname, ftpUsr, ftpPwd, port, fileName, srcFileUrl);

            if (!uploadBool) {
                logger.info("文件上传失败,请检查ftp服务是否打开或ftp 2125端口是否开启");
                return result.error("文件上传失败,请检查ftp服务是否打开或端口是否开启");
            }

            //雷拓接口--4.10 媒体库操作:创建媒体库节点 $(function)=MLCreateNode
            JSONObject createJson = new JSONObject();
            createJson.put("ParentId", 0);
            createJson.put("Type", 1);
            createJson.put("Name", OriginalFilename);
            createJson.put("FileId", fileId);
            //雷拓IP广播平台接口
            JSONObject returnJson2 = radioPlayService.interfaceCall("MLCreateNode", createJson);
            if (returnJson2 == null || !"0".equals(returnJson2.getString("Ret"))) {
                String errorMessage = radioPlayService.getErrorMessage(returnJson2);
                logger.info("文件上传失败, {}", errorMessage);
                return result.error(errorMessage);
            }
            //雷拓IP广播平台音频文件唯一id
            Integer fileIdInteger = returnJson2.getInteger("ID");

            //音频素材信息保存至数据库
            //素材文件大小，保留两位小数
            float fileSize = (float) file.getSize() / 1048576;
            BigDecimal bigDecimal = new BigDecimal(fileSize);
            fileSize = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

            //获取用户id
            Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
            RadioMaterial radioMaterial = new RadioMaterial();
            radioMaterial.setName(OriginalFilename);
            radioMaterial.setSize(fileSize);
            radioMaterial.setDuration(getMP3PlayTime(srcFileUrl));
            radioMaterial.setFileName(fileName);
            radioMaterial.setPath(uploadPath);
            radioMaterial.setFileId(fileIdInteger);
            radioMaterial.setCreator(userId);
            radioMaterial.setCreateTime(new Date());
            radioMaterialDao.insert(radioMaterial);
            logger.info("文件上传成功, {}", OriginalFilename);
            return result.success("新增成功");
        } else {
            String errorMessage = radioPlayService.getErrorMessage(returnJson);
            logger.info("文件上传失败, {}", errorMessage);
            return result.error(errorMessage);
        }
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        logger.info("删除公共广播素材，接收参数：id = {}", id);
        Result result = new Result();
        radioMaterialDao.deleteById(id);
        //删除与节目的关联信息
        LambdaQueryWrapper<RadioProgramMaterial> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RadioProgramMaterial::getMaterialId, id);
        programMaterialService.remove(wrapper);
        return result.success("删除成功");
    }

    @Override
    public Result batchDelete(List<Integer> idList) {
        logger.info("删除公共广播素材，接收参数：idList = {}", idList);
        Result result = new Result();
        if (idList == null || idList.isEmpty()) {
            return result.error("参数为空");
        }
        boolean bool = this.removeByIds(idList);
        if (bool) {
            //删除与节目的关联信息
            LambdaQueryWrapper<RadioProgramMaterial> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(RadioProgramMaterial::getMaterialId, idList);
            programMaterialService.remove(wrapper);
            return result.success("删除成功");
        } else {
            return result.error("删除失败");
        }
    }

    @Override
    public Result getInfo(Integer id, HttpServletRequest request) {
        logger.info("获取公共广播素材详细信息，接收参数：id = {}", id);
        Result result = new Result();
        RadioMaterial radioMaterial = radioMaterialDao.selectById(id);
        //处理创建人名称和时长字符串
        if (radioMaterial != null && radioMaterial.getCreator() != null) {
            radioMaterial.setCreatorName(getNameByUserId(radioMaterial.getCreator()));
            radioMaterial.setDurationStr(DateUtil.secondToTime(radioMaterial.getDuration()));
        }
        return result.success(radioMaterial);
    }

    @Override
    public Result getList(PbRadioMaterialQueryObject qo, HttpServletRequest request) {
        logger.info("获取公共广播素材列表，接收参数：radioMaterial = {}", qo);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        Page<RadioMaterial> page = new Page<RadioMaterial>(qo.getPageNum(), qo.getPageSize());
        IPage<RadioMaterial> list = radioMaterialDao.getPageList(page, qo);
        if (list != null && list.getRecords() != null && !list.getRecords().isEmpty()) {
            for (RadioMaterial record : list.getRecords()) {
                record.setDurationStr(DateUtil.secondToTime(record.getDuration()));
            }
        }
        return result.success(list);
    }

    @Override
    public String getNameByUserId(Integer userId) {
        return userId == null ? null : radioMaterialDao.getNameByUserId(userId);
    }

    /**
     * 获取本地MP3音频文件的播放时长
     *
     * @param MP3FileURL 音频文件地址，全路径, 带扩展名
     * @return 音频文件播放时长，单位：*秒
     */
    public static int getMP3PlayTime(String MP3FileURL) {
        File file = new File(MP3FileURL);
        int MP3PlayTime = 0;
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(file);
            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();
            MP3PlayTime = audioHeader.getTrackLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MP3PlayTime;
    }

    public static void main(String[] args) {
        File file = new File("D:\\360安全浏览器下载\\一生有你.mp3");
        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        String fileUrl = file.getPath();
        if (fileUrl.indexOf(".mp3") > 1) {
            System.out.println("file is mp3");
        } else {
            System.out.println("file is no mp3");
        }

        int MP3PlayTime = 0;
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(file);
            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();
            MP3PlayTime = audioHeader.getTrackLength();
            System.out.println("歌曲时长 = " + MP3PlayTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}