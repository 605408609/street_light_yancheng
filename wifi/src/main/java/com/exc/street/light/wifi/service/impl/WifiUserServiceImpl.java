/**
 * @filename:WifiUserServiceImpl 2020-03-12
 * @project wifi  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.wifi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.wifi.WifiAcDevice;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.entity.wifi.WifiUser;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.resp.WifiRespUserVO;
import com.exc.street.light.wifi.mapper.WifiAcDeviceDao;
import com.exc.street.light.wifi.mapper.WifiApDeviceDao;
import com.exc.street.light.wifi.mapper.WifiUserDao;
import com.exc.street.light.wifi.qo.WifiUserQueryObject;
import com.exc.street.light.wifi.service.WifiUserService;
import com.exc.street.light.wifi.utils.Constants;
import com.exc.street.light.wifi.utils.SNMPUtils;
import com.exc.street.light.wifi.utils.StringDealUtils;
import com.exc.street.light.wifi.webservice.WebServiceInfo;
import org.snmp4j.smi.VariableBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: wifi用户(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WifiUserServiceImpl extends ServiceImpl<WifiUserDao, WifiUser> implements WifiUserService {

    @Autowired
    private WifiUserDao wifiUserDao;

    @Autowired
    private WifiAcDeviceDao wifiAcDeviceDao;

    @Autowired
    private WifiApDeviceDao wifiApDeviceDao;

    @Autowired
    private LogUserService userService;

    @Override
    public Result getList(HttpServletRequest request, WifiUserQueryObject qo) {
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        Page<WifiRespUserVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<WifiRespUserVO> list = wifiUserDao.getList(page, qo);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result getWifiUserInfo() {
        // 获取所有ac设备
        List<WifiAcDevice> acDeviceList = wifiAcDeviceDao.selectList(null);
        // 过滤出所有ac设备的ip地址
        List<String> acIpList = acDeviceList.stream().map(WifiAcDevice::getIp).distinct().collect(Collectors.toList());
        for (String acIp : acIpList) {
            // 用户关联的apMac集合
            List<VariableBinding> apMacList = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.userAssociateApMac);
            // STA发送的总的字节数，上行流量
            List<VariableBinding> STAUpFlowList = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.STASendTotalByte);
            // STA接收的总的字节数，下行流量
            List<VariableBinding> STADownFlowList = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.STARecvTotalByte);
            // 认证成功的用户集合
            List<WifiUser> successUserList = WebServiceInfo.getCertSuccessUserList();
            updateOrInsertWifiUserList(apMacList, STAUpFlowList, STADownFlowList, successUserList);
            // 认证失败的用户集合
            List<WifiUser> failUserList = WebServiceInfo.getCertFailUserList();
            updateOrInsertWifiUserList(apMacList, STAUpFlowList, STADownFlowList, failUserList);
        }
        Result result = new Result();
        return result.success("数据同步成功");
    }

    /**
     * 新增或更新wifi用户信息到数据库
     *
     * @param apMacList
     * @param wifiUserList
     */
    public void updateOrInsertWifiUserList(List<VariableBinding> apMacList, List<VariableBinding> STAUpFlowList, List<VariableBinding> STADownFlowList, List<WifiUser> wifiUserList) {
        if (wifiUserList != null && wifiUserList.size() > 0) {
            for (WifiUser wifiUser : wifiUserList) {
                if (apMacList != null && apMacList.size() > 0) {
                    for (VariableBinding binding : apMacList) {
                        String userMac = StringDealUtils.stringDeal(binding.getOid().toString());
                        String apMac = binding.getVariable().toString();
                        apMac = apMac.replace(":", "-");
                        // 根据Mac地址判断用户归属于哪个ap
                        if (wifiUser.getUserMac().equals(userMac)) {
                            LambdaQueryWrapper<WifiApDevice> wrapper = new LambdaQueryWrapper<>();
                            wrapper.eq(WifiApDevice::getMac, apMac);
                            WifiApDevice apDevice = wifiApDeviceDao.selectOne(wrapper);
                            wifiUser.setDeviceId(apDevice.getId());
                            long sumUpFlow = 0;
                            long sumDownFlow = 0;
                            // 上行流量
                            if (STAUpFlowList != null && STAUpFlowList.size() > 0) {
                                for (VariableBinding upFlow : STAUpFlowList) {
                                    // 获取用户的Mac地址
                                    String userMacByUpFlow = StringDealUtils.stringDeal(upFlow.getOid().toString());
                                    // 找出认证成功的用户
                                    if (wifiUser.getUserMac().equals(userMacByUpFlow) && wifiUser.getCertifResult() == 1) {
                                        // 单位为字节
                                        sumUpFlow = upFlow.getVariable().toLong();
                                    }
                                }
                            }
                            // 下行流量
                            if (STADownFlowList != null && STADownFlowList.size() > 0) {
                                for (VariableBinding downFlow : STADownFlowList) {
                                    String userMacByDownFlow = StringDealUtils.stringDeal(downFlow.getOid().toString());
                                    if (wifiUser.getUserMac().equals(userMacByDownFlow) && wifiUser.getCertifResult() == 1) {
                                        sumDownFlow = downFlow.getVariable().toLong();
                                    }
                                }
                            }
                            int internetTraffic = (int) ((sumUpFlow + sumDownFlow) / (1024 * 1024));
                            // 单位为字节，将其转换为MB
                            wifiUser.setInternetTraffic(internetTraffic);
                            // 判断数据库中是否存在相同的数据，防止定时刷新出现重复数据
                            LambdaQueryWrapper<WifiUser> wrap = new LambdaQueryWrapper<>();
                            // 根据用户Mac和认证时间确定唯一性
                            wrap.eq(WifiUser::getUserMac, wifiUser.getUserMac())
                                    .eq(WifiUser::getCertifTime, wifiUser.getCertifTime());
                            WifiUser wfUser = wifiUserDao.selectOne(wrap);
                            // 存在就更新，不存在就新增
                            if (wfUser != null) {
                                LambdaQueryWrapper<WifiUser> wr = new LambdaQueryWrapper<>();
                                wr.eq(WifiUser::getId, wfUser.getId());
                                wifiUserDao.update(wifiUser, wr);
                            } else {
                                wifiUserDao.insert(wifiUser);
                            }
                        }
                    }
                }
            }
        }
    }
}