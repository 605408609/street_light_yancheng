package com.exc.street.light.sl.VO;

public class TestVO {

    //调用方唯一标识
    String OperatorID;
    //各接口具体参数信息
    String Data;
    //时间戳
    String TimeStamp;
    //自增序列
    String Seq;
    //参数签名
    String Sig;

    public String getOperatorID() {
        return OperatorID;
    }

    public void setOperatorID(String operatorID) {
        OperatorID = operatorID;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getSeq() {
        return Seq;
    }

    public void setSeq(String seq) {
        Seq = seq;
    }

    public String getSig() {
        return Sig;
    }

    public void setSig(String sig) {
        Sig = sig;
    }
}
