/**
 * @filename:OrderPicServiceImpl 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.woa.OrderPic;
import com.exc.street.light.resource.vo.resp.WoaRespOrderPicVO;
import com.exc.street.light.woa.config.parameter.PathApi;
import com.exc.street.light.woa.mapper.OrderPicMapper;
import com.exc.street.light.woa.service.OrderPicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class OrderPicServiceImpl extends ServiceImpl<OrderPicMapper, OrderPic> implements OrderPicService {
    private static final Logger logger = LoggerFactory.getLogger(OrderPicServiceImpl.class);

    @Autowired
    private PathApi pathApi;

    @Override
    public void relationOrderImg(List<Integer> imgIdList, Integer processId, Integer id) {
        logger.info("关联图片与工单,接收参数：imgIdList={}，processId={},id={}", imgIdList, processId, id);
        this.baseMapper.relationOrderImg(imgIdList, processId, id);
    }

    @Override
    public Result uploadImage(MultipartFile file, HttpServletRequest httpServletRequest) {
        Result result = new Result();
        // 验证资源路径是否存在
        File srcDirPath = new File(pathApi.getUpload());
        if (!srcDirPath.exists()) {
            srcDirPath.mkdirs();
        }
        // 验证传输文件是否已存在
        // 文件名称
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || "".equals(originalFilename)) {
            return result.error("请选择文件");
        }
        // 文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        // 传入文件对象，文件保存到本地
        String fileName = UUID.randomUUID() + suffix;
        logger.info("上传图片保存地址：{}", pathApi.getUpload() + fileName);
        File srcFile = new File(pathApi.getUpload() + fileName);
        try {
            file.transferTo(srcFile);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
//        srcFile.renameTo(new File(uploadPath + fileName));
        // 保存到本地的文件对象
        OrderPic orderPic = new OrderPic();
        try {
            // 构建持久化对象
            orderPic.setCreateTime(new Date());
            orderPic.setName(fileName);
            logger.info("上传图片持久化保存对象：{}", orderPic);
            this.save(orderPic);
        } catch (Exception e) {
            // 如果保存失败则删除文件
            File file1 = new File(srcFile.getPath());
            file1.delete();
            logger.error(e.getMessage(), e);
            return result.error("图片上传失败");
        }
        return result.success(orderPic.getId());
    }

    @Override
    public Result delete(Integer picId, HttpServletRequest request) {
        logger.info("准备删除图片id：{}", picId);
        Result result = new Result();
        if (picId == null) {
            return result.error("id为空");
        }
        // 根据id获取工单图片对象
        OrderPic orderPic = this.getById(picId);
        //如果工单图片不等于空，删除源文件
        if (orderPic != null) {
            File file = new File(pathApi.getUpload() + orderPic.getName());
            //如果文件存在则删除文件
            if (file.exists()) {
                file.delete();
            }
        }
        //删除本地数据库数据
        this.removeById(picId);
        return result.success("删除成功");
    }

    @Override
    public List<WoaRespOrderPicVO> getWoaRespOrderPicVO(Integer id) {
        return this.baseMapper.getWoaRespOrderPicVO(id);
    }
}