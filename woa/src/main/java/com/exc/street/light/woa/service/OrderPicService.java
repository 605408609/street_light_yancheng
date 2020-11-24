/**
 * @filename:OrderPicService 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.woa.OrderPic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.resp.WoaRespOrderPicVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface OrderPicService extends IService<OrderPic> {

    /**
     * 关联图片与工单
     *
     * @param imgIdList
     * @param processId
     * @param id
     */
    void relationOrderImg(List<Integer> imgIdList, Integer processId, Integer id);

    /**
     * 上传图片
     *
     * @param file
     * @param httpServletRequest
     * @return
     */
    Result uploadImage(MultipartFile file, HttpServletRequest httpServletRequest);

    /**
     * 根据图片id删除图片
     *
     * @param id
     * @param request
     * @return
     */
    Result delete(Integer id, HttpServletRequest request);

    /**
     * 获取工单详情图片对象
     *
     * @param id
     * @return
     */
    List<WoaRespOrderPicVO> getWoaRespOrderPicVO(Integer id);
}