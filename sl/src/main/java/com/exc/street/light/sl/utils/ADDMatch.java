package com.exc.street.light.sl.utils;

public enum ADDMatch {

    /*STATE(0,"1001","state"),
    BRIGHTNESS(1,"1002","brightness"),
    MODULETEMPERATURE(2,"3001","moduleTemperature"),
    VOLTAGE(3,"3002","voltage"),
    ELECTRICCURRENTONE(4,"3003","electricCurrentOne"),
    POWERONE(5,"3004","powerOne"),
    ELECTRICENERGYONEHIGH(6,"3005","electricEnergyOneHigh"),
    ELECTRICENERGYONELOW(7,"3006","electricEnergyOneLow"),
    POWERTIMEHIGH(8,"3007","powerTimeHigh"),
    POWERTIMELOW(9,"3008","powerTimeLow"),
    MODULEATIMEHIGH(10,"3009","moduleATimeHigh"),
    MODULEATIMELOW(11,"300A","moduleATimeLow"),
    LAMPATIMEONEHIGH(12,"300B","lampATimeOneHigh"),
    LAMPATIMEONELOW(13,"300C","lampATimeOneLow"),
    LAMPTIMEONEHIGH(14,"300D","lampTimeOneHigh"),
    LAMPTIMEONELOW(15,"300E","lampTimeOneLow"),
    ALARM(16,"300F","alarm"),
    VOLTAGEALARM(17,"3010","voltageAlarm"),
    ELECTRICCURRENTALARM(18,"3011","electricCurrentAlarm"),
    TEMPERATUREALARM(19,"3012","temperatureAlarm"),
    FACTORYSERIALNUM(20,"3013","factorySerialNum"),
    FACTORYSERIALNUM1(21,"3014","factorySerialNum1"),
    FACTORYSERIALNUM2(22,"3015","factorySerialNum2"),
    FACTORYSERIALNUM3(23,"3016","factorySerialNum3"),
    ELECTRICCURRENTTWO(24,"3017","electricCurrentTwo"),
    POWERTWO(25,"3018","powerTwo"),
    ELECTRICENERGYTWOHIGH(26,"3019","electricEnergyTwoHigh"),
    ELECTRICENERGYTWOLOW(27,"301A","electricEnergyTwoLow"),
    LAMPATIMETWOHIGH(28,"301B","lampATimeTwoHigh"),
    LAMPATIMETWOLOW(29,"301C","lampATimeTwoLow"),
    LAMPTIMETWOHIGH(30,"301D","lampTimeTwoHigh"),
    LAMPTIMETWOLOW(31,"301E","lampTimeTwoLow");*/

    STATEONE(0,"1009","stateOne"),
    STATETWO(1,"100A","stateTwo"),
    ALARM(2,"3001","alarm"),
    SIGNALINTENSITY(3,"3002","signalIntensity"),
    VOLTAGEFREQUENCY(4,"3003","voltageFrequency"),
    MODULETEMPERATURE(5,"3004","moduleTemperature"),
    VOLTAGE(6,"3005","voltage"),
    ELECTRICCURRENTONE(7,"3006","electricCurrentOne"),
    POWERONE(8,"3007","powerOne"),
    ELECTRICENERGYONEHIGH(9,"3008","electricEnergyOneHigh"),
    ELECTRICENERGYONELOW(10,"3009","electricEnergyOneLow"),
    POWERFACTORONE(11,"300A","powerFactorOne"),
    ELECTRICCURRENTTWO(12,"300B","electricCurrentTwo"),
    POWERTWO(13,"300C","powerTwo"),
    ELECTRICENERGYTWOHIGH(14,"300D","electricEnergyTwoHigh"),
    ELECTRICENERGYTWOLOW(15,"300E","electricEnergyTwoLow"),
    POWERFACTORTWO(16,"300F","powerFactorTwo"),
    POLEOFFSETXY(17,"3010","poleOffsetXY"),
    POLEOFFSETZ(18,"3011","poleOffsetZ"),
    POWERTIMEHIGH(19,"3012","powerTimeHigh"),
    POWERTIMELOW(20,"3013","powerTimeLow"),
    MODULEATIMEHIGH(21,"3014","moduleATimeHigh"),
    MODULEATIMELOW(22,"3015","moduleATimeLow"),
    LAMPATIMEONEHIGH(23,"3016","lampATimeOneHigh"),
    LAMPATIMEONELOW(24,"3017","lampATimeOneLow"),
    LAMPTIMEONEHIGH(25,"3018","lampTimeOneHigh"),
    LAMPTIMEONELOW(26,"3019","lampTimeOneLow"),
    LAMPATIMETWOHIGH(27,"301A","lampATimeTwoHigh"),
    LAMPATIMETWOLOW(28,"301B","lampATimeTwoLow"),
    LAMPTIMETWOHIGH(29,"301C","lampTimeTwoHigh"),
    LAMPTIMETWOLOW(30,"301D","lampTimeTwoLow"),
    FACTORYSERIALNUM(31,"301E","factorySerialNum"),
    FACTORYSERIALNUM1(32,"301F","factorySerialNum1"),
    FACTORYSERIALNUM2(33,"3020","factorySerialNum2"),
    FACTORYSERIALNUM3(34,"3021","factorySerialNum3"),
    FACTORYSERIALNUM4(35,"3022","factorySerialNum4"),
    CARDNUMBER(36,"3023","cardNumber"),
    CARDNUMBER1(37,"3024","cardNumber1"),
    CARDNUMBER2(38,"3025","cardNumber2"),
    CARDNUMBER3(39,"3026","cardNumber3"),
    CARDNUMBER4(40,"3027","cardNumber4"),
    VOLTAGEALARM(41,"3028","voltageAlarm"),
    ELECTRICCURRENTALARM(42,"3029","electricCurrentAlarm"),
    TEMPERATUREALARM(43,"302A","temperatureAlarm"),
    XYAXISINFORMATION(44,"302B","XYAxisInformation"),
    ZAXISINFORMATION(45,"302C","ZAxisInformation"),
    LEAKAGEVALUE(46,"302D","leakageValue"),
    SOFTWAREVERSION(47,"302E","softwareVersion"),
    ATSUNRISE(48,"302F","atSunrise"),
    DOWNSUNRISE(49,"3030","downSunrise");


    private Integer index;
    private String ADD;
    private String attribute;


    ADDMatch(Integer index,String ADD,String attribute){
        this.index = index;
        this.ADD = ADD;
        this.attribute = attribute;
    }

    public Integer getIndex(){
        return index;
    }

    public String getADD(){
        return ADD;
    }

    private String getAttribute(){
        return attribute;
    }

    public Integer getIndexByADD(String ADD){
        for(ADDMatch addMatch : ADDMatch.values()){
            if(addMatch.getADD().equals(ADD)){
                return addMatch.getIndex();
            }
        }
        return -1;
    }

    public String getAttributeByIndex(Integer index){
        for(ADDMatch addMatch : ADDMatch.values()){
            if(addMatch.getIndex() == index){
                return addMatch.getAttribute();
            }
        }
        return "";
    }
}
