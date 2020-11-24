package com.exc.street.light.sl.utils;

public enum FrameHeadMatch {

    COLLECTINFORMATION("10"),
    GROUPREAD("38"),
    POWERQUERY("25"),
    GROUPNUMQUERY("36"),
    SETGROUP("37"),
    CONTROL("15"),
    GROUPCONTROL("39"),
    CURRENTALARM("22"),
    VOLTAGEALARM("23"),
    SETTIME("24"),
    TIMEREAD("32"),
    SETADDRESS("29"),
    SETSTRATEGY("AA55");

    private String frameHead;

    FrameHeadMatch(String frameHead){
        this.frameHead = frameHead;
    }

    public String getFrameHead(){
        return frameHead;
    }

}