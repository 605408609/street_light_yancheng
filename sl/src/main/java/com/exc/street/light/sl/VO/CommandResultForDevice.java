package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class CommandResultForDevice {

    private String resultCode;
    private ObjectNode resultDetail;
}
