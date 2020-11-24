package com.exc.street.light.resource.dto.electricity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网关电表模块数据dto
 * @Author: Xiaok
 * @Date: 2020/10/24 11:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityMeterModuleDataDTO {

    /**
     * 网关MAC地址
     *
     */
    private String gatewayMac;

    /**
     * 电表端口
     * 范围 1~3
     */
    private Integer electricMeterPort;

    /**
     * 电表地址
     * 范围 1~99
     */
    private Integer electricMeterAddr;

    /**
     * 当前组合有功总电能
     * 范围： 0~6.8E+35
     * 单位： kWh
     */
    private Float currentCombinedActiveTotalEnergy;

    /**
     * 当前正向有功总电能
     * 范围： 0~6.8E+35
     * 单位： kWh
     */
    private Float currentTotalPositiveActiveEnergy;

    /**
     * 当前反向有功总电能
     * 范围： 0~6.8E+35
     * 单位： kWh
     */
    private Float currentTotalReverseActiveEnergy;

    /**
     * 当前正向无功总电能
     * 范围： 0~6.8E+35
     * 单位： kWh
     */
    private Float currentTotalPositiveReactivePower;

    /**
     * 当前反向视在总电能
     * 范围： 0~6.8E+35
     * 单位： kWh
     */
    private Float currentReverseApparentTotalEnergy;

    /**
     * A相电压
     * 范围： 0~999.999
     * 单位： V
     */
    private Float aPhaseVoltage;

    /**
     * B相电压
     * 范围： 0~999.9
     * 单位： V
     */
    private Float bPhaseVoltage;

    /**
     * C相电压
     * 范围： 0~999.9
     * 单位： V
     */
    private Float cPhaseVoltage;

    /**
     * A相电流
     * 范围： 0~99.999
     * 单位： A
     */
    private Float aPhaseCurrent;

    /**
     * B相电流
     * 范围： 0~99.999
     * 单位： A
     */
    private Float bPhaseCurrent;

    /**
     * C相电流
     * 范围： 0~99.999
     * 单位： A
     */
    private Float cPhaseCurrent;

    /**
     * A相有功功率
     * 范围： -99.999~99.999
     * 单位： kW
     */
    private Float aPhaseActivePower;

    /**
     * A相无功功率
     * 范围： -99.999~99.999
     * 单位： kW
     */
    private Float aPhaseReactivePower;

    /**
     * A相视在功率
     * 范围： -99.999~99.999
     * 单位： kW
     */
    private Float aPhaseApparentPower;

    /**
     * A相功率因数字
     * 范围： -1.000~1.000
     * 单位：
     */
    private Float aPhasePowerFactorNumber;

    /**
     * B相有功功率
     * 范围： -99.999~99.999
     * 单位： kW
     */
    private Float bPhaseActivePower;

    /**
     * B相无功功率
     * 范围： -99.999~99.999
     * 单位： kW
     */
    private Float bPhaseReactivePower;

    /**
     * B相视在功率
     * 范围： -99.999~99.999
     * 单位： kW
     */
    private Float bPhaseApparentPower;

    /**
     * B相功率因数字
     * 范围： -1.000~1.000
     * 单位：
     */
    private Float bPhasePowerFactorNumber;

    /**
     * C相有功功率
     * 范围： -99.999~99.999
     * 单位： kW
     */
    private Float cPhaseActivePower;

    /**
     * C相无功功率
     * 范围： -99.999~99.999
     * 单位： kW
     */
    private Float cPhaseReactivePower;

    /**
     * C相视在功率
     * 范围： -99.999~99.999
     * 单位： kW
     */
    private Float cPhaseApparentPower;

    /**
     * C相功率因数字
     * 范围： -1.000~1.000
     * 单位：
     */
    private Float cPhasePowerFactorNumber;

}
