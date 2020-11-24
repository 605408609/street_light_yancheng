package com.exc.street.light.em.netty;

import java.util.*;

/**
 * @author xujiahao
 * @version 1.0
 * @date 2019/12/27 11:38
 */
public class IotDataUtil {

    private static String bytes2String;

    private static  List<String> splitList;
    /**
     * 获取SN
     * @param bytes
     * @return
     */
    public static String getDevice1Sn(byte[] bytes){
        String SN=null;
        String bytes2String = new String(bytes, 0, bytes.length);
        boolean b = bytes2String.startsWith("##");
        if(b){
            splitList= new ArrayList<>();
            String[] split = bytes2String.split(";");
            for (int i = 0; i < split.length; i++) {
                String[] split1 = split[i].split("&&");
                for (String s : split1) {
                    splitList.add(s);
                }
            }
            for (String s : splitList) {
                if (s.contains("MN=")) {
                    SN = s.substring(s.lastIndexOf("=") + 1);
                }
            }
        }
        return SN;
    }

    /**
     * 获取字段集合
     * @param bytes
     * @return
     */
    public static Map<String, String> getDevice1DataMap(byte[] bytes){
        Map<String, String> dataMap = new HashMap<>(20);
        String bytes2String = new String(bytes, 0, bytes.length);
        boolean b = bytes2String.startsWith("##");
        if(b){
            splitList= new ArrayList<>();
            String[] split = bytes2String.split(";");
            for (int i = 0; i < split.length; i++) {
                String[] split1 = split[i].split("&&");
                for (String s : split1) {
                    splitList.add(s);
                }
            }
            for (String s : splitList) {
                if (s.contains(",")) {
                    Arrays.stream(s.split(","))
                            .filter(kv -> kv.contains("="))
                            .map(kv -> kv.split("="))
                            .forEach(array -> dataMap.put(array[0], array[1]));
                }
            }
        }
        return  dataMap;
    }

    public static String getDevice1DataUpdateTime(byte[] bytes){
        String updateTime=null;
        String bytes2String = new String(bytes, 0, bytes.length);
        boolean b = bytes2String.startsWith("##");
        if(b){
            splitList= new ArrayList<>();
            String[] split = bytes2String.split(";");
            for (int i = 0; i < split.length; i++) {
                String[] split1 = split[i].split("&&");
                for (String s : split1) {
                    splitList.add(s);
                }
            }
            for (String s : splitList) {
                if (s.contains("DataTime=")) {
                    updateTime = s.substring(s.lastIndexOf("=") + 1);
                }
            }
        }
        return updateTime;
    }

    public static String getDevice1Flag(byte[] bytes){
        String Flag=null;
        String bytes2String = new String(bytes, 0, bytes.length);
        boolean b = bytes2String.startsWith("##");
        if(b){
            splitList= new ArrayList<>();
            String[] split = bytes2String.split(";");
            for (int i = 0; i < split.length; i++) {
                String[] split1 = split[i].split("&&");
                for (String s : split1) {
                    splitList.add(s);
                }
            }
            for (String s : splitList) {
                if (s.contains("Flag=")) {
                    Flag = s.substring(s.lastIndexOf("=") + 1);
                }
            }
        }
        return Flag;
    }

    public static String getDevice1Response(byte[] bytes){
        String QN=null;
        String PW=null;
        String MN=null;
        String CP=null;
        String response=null;
        String bytes2String = new String(bytes, 0, bytes.length);
        boolean b = bytes2String.startsWith("##");
        if(b){
            splitList= new ArrayList<>();
            String[] split = bytes2String.split(";");
            for (int i = 0; i < split.length; i++) {
                String[] split1 = split[i].split("&&");
                for (String s : split1) {
                    splitList.add(s);
                }
            }
            for (String s : splitList) {
                if (s.contains("QN=")) {
                    QN = s.substring(s.lastIndexOf("=") + 1);
                }
                if (s.contains("PW=")) {
                    PW = s.substring(s.lastIndexOf("=") + 1);
                }
                if (s.contains("MN=")) {
                    MN = s.substring(s.lastIndexOf("=") + 1);
                }
                if (s.contains("CP=")) {
                    CP = s.substring(s.lastIndexOf("=") + 1);
                }
            }
        }
        response= "QN=" + QN + ";ST=91;CN=9012;PW=" + PW + ";MN=" + MN
                + ";Flag=4;CP=" + CP + "&&ExeRtn=1&&";

        return response;
    }
}
