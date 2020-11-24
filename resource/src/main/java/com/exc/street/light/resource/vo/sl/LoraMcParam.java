package com.exc.street.light.resource.vo.sl;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoraMcParam implements Serializable {

    private String mcEUI;
    private int fPort;
    private String data;
}
