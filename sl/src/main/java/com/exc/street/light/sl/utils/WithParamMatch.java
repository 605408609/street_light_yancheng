package com.exc.street.light.sl.utils;

public enum WithParamMatch {

    ALARMVOLTAGE("2001"),
    ALARMCURRENT("2002"),
    ALARMTEMPERATURE("2003"),
    INTERVAL("2004"),
    OTA("1007"),
    LEAKAGECURRENT("2013");

    private String id;

    WithParamMatch(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
