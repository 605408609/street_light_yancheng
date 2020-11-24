/**
 * @filename:ScreenMaterialDao 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.ir.ScreenMaterial;
import com.exc.street.light.resource.qo.IrMaterialQueryObject;
import com.exc.street.light.resource.vo.IrMaterialVO;
import com.exc.street.light.resource.vo.resp.IrRespScreenMaterialVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Repository
@Mapper
public interface ScreenMaterialMapper extends BaseMapper<ScreenMaterial> {

    /**
     * 条件分页查询
     *
     * @param page
     * @param irMaterialQueryObject
     * @return
     */
    IPage<IrRespScreenMaterialVO> query(IPage<IrRespScreenMaterialVO> page,@Param("irMaterialQueryObject") IrMaterialQueryObject irMaterialQueryObject);

    /**
     * 获取对应的素材列表
     * @param materialIdList
     * @return
     */
    List<IrMaterialVO> getIrMaterialVO(@Param("list") List<Integer> materialIdList);
}
