package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanChannelTypeMapper;
import com.exc.street.light.electricity.service.CanChannelTypeService;
import com.exc.street.light.resource.entity.electricity.CanChannelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Linshiwen
 * @date 2018/05/23
 */
@Service
public class CanChannelTypeServiceImpl extends ServiceImpl<CanChannelTypeMapper, CanChannelType> implements CanChannelTypeService {

    @Override
    public List<CanChannelType> getRelayType() {
        return baseMapper.getRelayType();
    }

    @Override
    public List<CanChannelType> find() {
        return baseMapper.find();
    }
}
