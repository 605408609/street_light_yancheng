 package com.exc.street.light.resource.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 字符串转换工具类
 * @author Huang Min
 * @date 2020/03/14
 */
public class StringConversionUtil {
    /**
     * 获取ID的字符串,转换为ID集合,例如: 将 从前端接收的 String "1,2,3" 转换为List [1,2,3]
     * @param ids
     * @return 
     */
    public static List<Integer> getIdListFromString(String ids) {
        List<String> idStringList = stringToStringList(ids);
        List<Integer> idList = listStringToListInteger(idStringList);
        return idList;
    }
    
    /**
     * 将String类型的集合转换为 Integer类型的集合
     * @param ids   
     * @return
     */
    public static List<Integer> listStringToListInteger(List<String> ids){
        List<Integer> idList =new ArrayList<Integer>(ids.size());
        try{   
           for(int i=0,j=ids.size();i<j;i++){
             idList.add(Integer.parseInt(ids.get(i)));   
           }   
          }catch(Exception  e){
        }
        return idList;
    }
    /**
     * 将String字符串转换为 String类型的集合
     * @param ids
     * @return 
     */
    public static List<String> stringToStringList(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        return idList;
    }
  
}
