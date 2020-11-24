/**
 * @filename:AhDeviceServiceImpl 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2018 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.occ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.occ.mapper.AhDeviceDao;
import com.exc.street.light.occ.mapper.AhPlayDao;
import com.exc.street.light.occ.service.AhDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.entity.occ.AhPlay;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.OccAhDeviceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Huang Min
 * 
 */
@Service
public class AhDeviceServiceImpl  extends ServiceImpl<AhDeviceDao, AhDevice> implements AhDeviceService  {
	private static final Logger logger = LoggerFactory.getLogger(AhDeviceServiceImpl.class);
    @Autowired
    private AhPlayDao ahPlayDao;
    @Autowired
    private LogUserService userService;
	/* (non-Javadoc)
     * @see com.exc.street.light.occ.service.SlDeviceService#uniqueness(com.exc.street.light.occ.vo.SlDeviceVO)
     */
    @Override
    public Result uniqueness(AhDevice ahDevice) {
    	 logger.info("参数是："+ahDevice.toString());
        List<AhDevice> listSlDevice = baseMapper.selectList(null);
        logger.info("报警盒list:"+listSlDevice.toString());
        for (AhDevice slDevice : listSlDevice) {
            if (slDevice.getName().equals(ahDevice.getName())) {
             	//当ID不为空，且等于当前节点ID，即为对某个节点进行编辑操作，校验时不判断设备名称和序列号，直接通过
            	if (slDevice.getId() != null && slDevice.getId() != ahDevice.getId()) {
            		return new Result<>().error("设备名称已存在,请重新输入");
            	}
            }
            if (slDevice.getNum().equals(ahDevice.getNum())) {
            	//当ID不为空，且等于当前节点ID，即为对某个节点进行编辑操作，校验时不判断设备名称和序列号，直接通过
            	if (slDevice.getId() != null && slDevice.getId() != ahDevice.getId()) {
            		return new Result<>().error("设备序列号已存在,请重新输入");
            	}
            }
        }

        return new Result<>().success("唯一性校验通过!");
    }
    
    /* (non-Javadoc)
     * @see com.exc.street.light.occ.service.SlDeviceService#page(com.baomidou.mybatisplus.extension.plugins.pagination.Page, com.baomidou.mybatisplus.core.conditions.query.QueryWrapper)
     */
    @Override
    public IPage<OccAhDeviceVO> page(Page<OccAhDeviceVO> page, QueryWrapper<OccAhDeviceVO> queryWrapper, OccAhDeviceVO t, HttpServletRequest request) {
    	// 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            t.setAreaId(user.getAreaId());
            return new Page<OccAhDeviceVO>() ;
        }
        
         return baseMapper.selectPage(page, queryWrapper,t);
    }

    /* (non-Javadoc)
     * @see com.exc.street.light.occ.service.AhDeviceService#pulldownByLampPost(java.util.List, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request) {
        LambdaQueryWrapper<AhDevice> wrapper = new LambdaQueryWrapper();
        if (lampPostIdList != null && lampPostIdList.size() > 0) {
            wrapper.in(AhDevice::getLampPostId, lampPostIdList);
        }
        List<AhDevice> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

	@Override
	public OccAhDeviceVO selectInfoByWorkbench(Long id) {
		OccAhDeviceVO oa= baseMapper.selectInfoByWorkbench(id);
		if (oa != null) {
			List<AhPlay> listAhPlay = ahPlayDao.selectPageByWorkbench(id);
			oa.setListAhPlay(listAhPlay);
		}
		return oa;
	}

}