package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class LightSetStrategy {

    /*private Integer devtype;
    private String ontime;
    private Integer onbri;
    private String offtime;
    private Integer offbri;

    public Integer getDevtype() {
        return devtype;
    }

    public void setDevtype(Integer devtype) {
        this.devtype = devtype;
    }

    public String getOntime() {
        return ontime;
    }

    public void setOntime(String ontime) {
        this.ontime = ontime;
    }

    public Integer getOnbri() {
        return onbri;
    }

    public void setOnbri(Integer onbri) {
        this.onbri = onbri;
    }

    public String getOfftime() {
        return offtime;
    }

    public void setOfftime(String offtime) {
        this.offtime = offtime;
    }

    public Integer getOffbri() {
        return offbri;
    }

    public void setOffbri(Integer offbri) {
        this.offbri = offbri;
    }*/

    private Integer devtype;

    private String devaddr;
    private Integer idx;
    private Integer enabled;

    private String time1;
    private String attr1;
    private Integer val1;

    private String time2;
    private String attr2;
    private Integer val2;
}
