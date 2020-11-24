/**
 * @filename:UserDao 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.log_api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.ua.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xuJiaHao
 *
 */
@Mapper
public interface LogUserMapper extends BaseMapper<User> {

}
