package com.exc.street.light.sl.utils;

public enum NoParamMatch {

    PARAMETERCLEAR("2008","0001");

    private String id;
    private String info;

    NoParamMatch(String id,String info){
        this.id = id;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }
}
