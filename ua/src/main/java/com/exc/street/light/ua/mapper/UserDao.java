/**
 * @filename:UserDao 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.vo.UaUserVO;
import com.exc.street.light.resource.vo.resp.UaRespLocationAreaVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description: 用户数据访问层
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface UserDao extends BaseMapper<User> {

    /**
     * 根据电话号码和密码查询用户信息
     * @param phone
     * @param password
     * @return
     */
    User findByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password);

    /**
     * 更改用户状态
     * @param id
     */
    void updateStatus(Integer id);

    /**
     * 用户列表
     * @param page
     * @return
     */
    IPage<UaUserVO> getPageList(Page<UaUserVO> page, String name, String phone, Integer roleId, Integer founderId);

    /**
     * 根据用户id查询名称集合
     * @param idList
     * @return
     */
    List<User> selectNameByUserIds(List<Integer> idList);

    /**
     * 区域用户集合
     * @param areaIdList
     * @return
     */
    List<UaRespLocationAreaVO> selectAreaByAreaIdList(@Param("areaIdList") List<Integer> areaIdList);

    /**
     * 查询用户所属区域名称
     * @param areaId
     * @return
     */
    UaRespLocationAreaVO selectAreaById(Integer areaId);

    /**
     * 批量修改用户分区id
     * @param userIdList
     */
    void updateBatchUserAreaId(@Param("list") List<Integer> userIdList);

    /**
     * 根据用户id查询founderId
     * @param id
     * @return
     */
    Integer getUserId(Integer id);

    /**
     * 根据角色id查询正常用户
     * @param roleId
     * @return
     */
    List<User> selectRoleId(Integer roleId);

}
