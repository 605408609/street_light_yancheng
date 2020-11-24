package com.exc.street.light.resource.vo.sl;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LoraReportData implements Serializable {

    private String gwid;
    private Double rssi;
    private Double snr;
    private Double freq;
    private Double dr;
    private boolean adr;

    @JSONField(name = "class")
    private String clazz;

    private Double fCnt;
    private Double fPort;
    private Boolean confirmed;
    private String data;
    private List<Gws> gws;

}
