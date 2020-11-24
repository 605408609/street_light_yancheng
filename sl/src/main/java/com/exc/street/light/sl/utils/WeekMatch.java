package com.exc.street.light.sl.utils;

public enum WeekMatch {

    Sunday(1,"80"),
    Monday(2,"02"),
    Tuesday(3,"04"),
    Wednesday(4,"08"),
    Thursday(5,"10"),
    Friday(6,"20"),
    Saturday(7,"40");


    private Integer id;
    private String num;

    public Integer getId() {
        return id;
    }
    public String getNum() {
        return num;
    }


    WeekMatch(Integer id,String num){
        this.id = id;
        this.num = num;
    }

    public String getNumById(Integer id){
        for(WeekMatch weekMatch : WeekMatch.values()){
            if(weekMatch.getId() == id){
                return weekMatch.getNum();
            }
        }
        return "";
    }

}
