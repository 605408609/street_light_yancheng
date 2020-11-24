package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanDeviceMapper;
import com.exc.street.light.electricity.service.CanDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.CanDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 强电设备服务接口实现类
 *
 * @author Linshiwen
 * @date 2018/05/22
 */
@Service
public class CanDeviceServiceImpl extends ServiceImpl<CanDeviceMapper, CanDevice> implements CanDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(CanDeviceServiceImpl.class);
    @Autowired
    private CanDeviceMapper canDeviceMapper;

    @Override
    public List<CanDevice> getByNid(Integer id) {
        return canDeviceMapper.getByNid(id);
    }

    @Override
    public CanDevice getAddressByNid(Integer nid) {
        return canDeviceMapper.getAddressByNid(nid);
    }

    @Override
    public CanDevice getByNidAndIndex(Integer nid, Integer canIndex) {
        return canDeviceMapper.getByNidAndIndex(nid, canIndex);
    }

    @Override
    public Result add(CanDevice canDevice, HttpServletRequest request) {
        logger.info("判断设备索引是否已存在");
        CanDevice canDevice1 = canDeviceMapper.getByNidAndIndex(canDevice.getNid(), canDevice.getCanIndex());
        //日志
        if (canDevice1 != null) {
            return new Result().error("设备索引canIdx已存在");
        }
        String address1 = canDevice.getAddress();
        if (address1 != null) {
            int num = canDeviceMapper.countByAddress(address1);
            if (num > 0) {
                return new Result().error("设备物理地址已存在");
            }
        }
        canDeviceMapper.insert(canDevice);
        return new Result().success(canDevice.getId());
    }

    @Override
    public Result patchUpdate(List<CanDevice> canDevices, HttpServletRequest request) {
        logger.info("批量编辑强电设备");
        //遍历设备集合
        for (CanDevice canDevice : canDevices) {
            String address = canDevice.getAddress();
            if (address != null) {
                int num = canDeviceMapper.countByAddressAndId(address, canDevice.getId());
                if (num > 0) {
                    return new Result().error("设备物理地址已存在");
                }
            }
            //更新设备
            canDeviceMapper.updateById(canDevice);
        }
        //systemLogService.save(log);
        return new Result().success("");
    }

    @Override
    public int countByAddress(String address1) {
        return canDeviceMapper.countByAddress(address1);
    }

    @Override
    public Result deletePatch(Integer[] ids, HttpServletRequest request) {
        for (int i = 0; i < ids.length; i++) {
            canDeviceMapper.deleteById(ids[i]);
        }
        return new Result().success("");
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        CanDevice canDevice = canDeviceMapper.selectById(id);
        canDeviceMapper.deleteById(id);
        logger.info("创建删除强电设备:{} 日志", canDevice.getName());
        return new Result().success("删除成功");
    }

    @Override
    public void deleteByNid(Integer nid) {
        canDeviceMapper.deleteByNid(nid);
    }

    @Override
    public CanDevice getByNidAndBatchNumber(Integer nid, String batchNumber) {
        return canDeviceMapper.getByNidAndBatchNumber(nid,batchNumber);
    }

}
