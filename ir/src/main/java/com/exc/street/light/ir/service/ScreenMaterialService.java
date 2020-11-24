/**
 * @filename:ScreenMaterialService 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.qo.IrMaterialQueryObject;
import com.exc.street.light.resource.vo.IrMaterialVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface ScreenMaterialService extends IService<ScreenMaterial> {

    /**
     * 上传文本素材
     *
     * @param material
     * @param httpServletRequest
     * @return
     */
    Result uploadText(ScreenMaterial material, HttpServletRequest httpServletRequest);

    /**
     * 上传视频图片
     *
     * @param file
     * @param httpServletRequest
     * @return
     */
    Result uploadVideoImage(MultipartFile file, HttpServletRequest httpServletRequest);

    /**
     * 获取素材库列表
     *
     * @param irMaterialQueryObject
     * @param httpServletRequest
     * @return
     */
    Result getQuery(IrMaterialQueryObject irMaterialQueryObject, HttpServletRequest httpServletRequest);

    /**
     * 根据素材id删除素材
     *
     * @param id
     * @param request
     * @return
     */
    Result delete(Integer id, HttpServletRequest request);

    /**
     * 批量删除显示屏素材
     *
     * @param ids
     * @param request
     * @return
     */
    Result batchDelete(String ids, HttpServletRequest request);

    /**
     * 根据素材id获取关联节目列表
     *
     * @param ids
     * @param request
     * @return
     */
    Result programList(String ids, HttpServletRequest request);

    /**
     * 获取对应的素材集合
     *
     * @param materialIdList
     * @return
     */
    List<IrMaterialVO> getIrMaterialVO(List<Integer> materialIdList);
}