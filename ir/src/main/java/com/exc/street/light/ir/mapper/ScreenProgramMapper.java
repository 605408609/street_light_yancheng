/**
 * @filename:ScreenProgramDao 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.ir.ScreenProgram;
import com.exc.street.light.resource.qo.IrProgramQuery;
import com.exc.street.light.resource.vo.IrProgramVO;
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
public interface ScreenProgramMapper extends BaseMapper<ScreenProgram> {

    /**
     * 获取节目对象
     *
     * @param programId
     * @return
     */
    IrProgramVO getIrProgramVO(@Param("programId") Integer programId);

    /**
     * 分页查询显示屏节目
     * @param page
     * @param irProgramQuery
     * @return
     */
    IPage<ScreenProgram> query(IPage<ScreenProgram> page,@Param("irProgramQuery")  IrProgramQuery irProgramQuery);


    List <ScreenProgram> getScreenProgram();
}
