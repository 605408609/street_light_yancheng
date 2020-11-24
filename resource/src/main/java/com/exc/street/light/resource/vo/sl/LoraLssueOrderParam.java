package com.exc.street.light.resource.vo.sl;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoraLssueOrderParam implements Serializable {

    private String devEUI;
    private boolean confirmed;
    private int fPort;
    private String data;

}
