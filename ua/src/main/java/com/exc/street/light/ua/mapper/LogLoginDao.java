/**
 * @filename:LogLoginDao 2020-06-09
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.log.LogLogin;
import com.exc.street.light.resource.qo.LogLoginQueryObject;
import com.exc.street.light.resource.vo.resp.LogRespLoginVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**   
 * @Description:登录日志(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface LogLoginDao extends BaseMapper<LogLogin> {

    IPage<LogRespLoginVO> getPageList(Page<LogRespLoginVO> page, @Param("queryObject") LogLoginQueryObject queryObject);
}
