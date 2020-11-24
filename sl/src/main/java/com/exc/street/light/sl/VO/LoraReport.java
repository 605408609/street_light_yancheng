package com.exc.street.light.sl.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoraReport implements Serializable {


    private String devEUI;
    private Double appID;
    private String type;
    private Long time;
    private LoraReportData data;


    /*"devEUI":"8cf957200000cfc4",
            "appID":792,
            "type":"uplink",
            "time":1587036794804,
            "data":{"gwid":"b827ebfffe7f3e6c",
            "rssi":-58,
            "snr":10,
            "freq":470.5,
            "dr":5,
            "adr":true,
            "class":"C",
            "fCnt":143,
            "fPort":8,
            "confirmed":false,
            "data":"YwAAAAEQMAEKAAAJEQAAAAAAAAAAAAAA2QAAANyMag==",
            "gws":[{"id":"b827ebfffe7f3e6c","rssi":-58,"snr":10}]}*/

            /*"devEUI":"8cf957200000cfc4",
            "appID":792,
            "type":"uplink",
            "time":1587041683648,
            "data":{"gwid":"b827ebfffe7f3e6c",
                    "rssi":-58,
                    "snr":9.75,
                    "freq":471.7,
                    "dr":5,
                    "adr":true,
                    "class":"C",
                    "fCnt":16,
                    "fPort":8,
                    "confirmed":false,
                    "data":"YwAAAAEQMAEKAAAJJwAAAAAAAAAAAAAADwAAASAzxw==","gws":[{"id":"b827ebfffe7f3e6c","rssi":-58,"snr":9.75}]}*/

}
