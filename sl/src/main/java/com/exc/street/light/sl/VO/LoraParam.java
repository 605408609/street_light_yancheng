package com.exc.street.light.sl.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoraParam implements Serializable {

    private String devEUI;
    private boolean confirmed;
    private int fPort;
    private String data;

}
