/**
 * @filename:AhPlayServiceImpl 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2018 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.occ.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.occ.mapper.AhPlayDao;
import com.exc.street.light.occ.service.AhPlayService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.occ.AhPlay;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.OccAhPlayVO;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Huang Min
 * 
 */
@Service
public class AhPlayServiceImpl  extends ServiceImpl<AhPlayDao, AhPlay> implements AhPlayService  {
	@Autowired
	private AhPlayDao ahPlayDao;
    @Autowired
    private LogUserService userService;
    /* (non-Javadoc)
     * @see com.exc.street.light.occ.service.SlDeviceService#page(com.baomidou.mybatisplus.extension.plugins.pagination.Page, com.baomidou.mybatisplus.core.conditions.query.QueryWrapper)
     */
    @Override
    public IPage<OccAhPlayVO> page(Page<OccAhPlayVO> page, QueryWrapper<OccAhPlayVO> queryWrapper, OccAhPlayVO t, HttpServletRequest request) {
    	// 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            t.setAreaId(user.getAreaId());
        } 
    	return baseMapper.selectPage(page, queryWrapper,t);
    }

	@Override
	public OccAhPlayVO getByAhPlayId(Long id) {
		OccAhPlayVO ahPlay = ahPlayDao.getById(id);
		return ahPlay;
	}

	@Override
	public Result newsStatus(OccAhPlayVO occAhPlayVO, HttpServletRequest httpServletRequest) {
		Result result = new Result();
		List<AhPlay>  listAhPlay= occAhPlayVO.getListAhPlay();
		
		if (listAhPlay.size() > 0 ) {
			
			boolean bool= this.updateBatchById(listAhPlay);
			if (!bool) {
				return result.error("修改一键呼叫数据失败");
			}
		}
		// TODO Auto-generated method stub
		return result.success("成功");
	}

	@Override
	public Result newsAll(HttpServletRequest httpServletRequest) {
		Result result = new Result();
		
		List<AhPlay> listAlarm = baseMapper.selectList(null);
		for (AhPlay alarm : listAlarm) {
			alarm.setHaveRead(1);//设置状态为已读，1
		}
		if (listAlarm.size() > 0 ) {
			this.updateBatchById(listAlarm);
		}
		
		// TODO Auto-generated method stub
		return result.success("成功");
	}
}