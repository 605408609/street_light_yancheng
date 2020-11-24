package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class LoraResponse {

    private String devEui;
    private String data;
    private Long timestamp;

}
