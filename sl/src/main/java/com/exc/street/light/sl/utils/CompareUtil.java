package com.exc.street.light.sl.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

public class CompareUtil {
    /**
     * 比较两个实体属性值，返回一个boolean,true则表时两个对象中的属性值无差异
     * @param oldObject 进行属性比较的对象1
     * @param newObject 进行属性比较的对象2
     * @return 属性差异比较结果boolean
     */
    public static boolean compareObject(Object oldObject, Object newObject) {
        Map<String, List<Object>> resultMap=compareFields(oldObject,newObject,null);
         
        //System.out.println("resultMap------------"+resultMap);
        if(resultMap.size()>0) {
            return false;
        }else {
            return true;
        }
    }
     
    /**
     * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个Map分别存oldObject,newObject此属性名的值
     * @param obj1 进行属性比较的对象1
     * @param obj2 进行属性比较的对象2
     * @param ignoreArr 忽略比较的字段
     * @return 属性差异比较结果map
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, List<Object>> compareFields(Object obj1, Object obj2, String[] ignoreArr) {
        try{ 
            Map<String, List<Object>> map = new HashMap<String, List<Object>>();
            List<String> ignoreList = null; 
            if(ignoreArr != null && ignoreArr.length > 0){ 
                // array转化为list 
                ignoreList = Arrays.asList(ignoreArr);
            } 
            if (obj1.getClass() == obj2.getClass()) {// 只有两个对象都是同一类型的才有可比性 
                Class clazz = obj1.getClass(); 
                // 获取object的属性描述 
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
                        Object.class).getPropertyDescriptors(); 
                for (PropertyDescriptor pd : pds) {// 这里就是所有的属性了 
                    String name = pd.getName();// 属性名 
                    if(ignoreList != null && ignoreList.contains(name)){// 如果当前属性选择忽略比较，跳到下一次循环 
                        continue; 
                    } 
                    Method readMethod = pd.getReadMethod();// get方法 
                    // 在obj1上调用get方法等同于获得obj1的属性值 
                    Object o1 = readMethod.invoke(obj1); 
                    // 在obj2上调用get方法等同于获得obj2的属性值 
                    Object o2 = readMethod.invoke(obj2); 
                    if(o1 instanceof Timestamp){ 
                        o1 = new Date(((Timestamp) o1).getTime()); 
                    } 
                    if(o2 instanceof Timestamp){ 
                        o2 = new Date(((Timestamp) o2).getTime()); 
                    } 
                    if(o1 == null && o2 == null){ 
                        continue; 
                    }else if(o1 == null && o2 != null){ 
                        List<Object> list = new ArrayList<Object>();
                        list.add(o1); 
                        list.add(o2); 
                        map.put(name, list); 
                        continue; 
                    } 
                    if (!o1.equals(o2)) {// 比较这两个值是否相等,不等就可以放入map了 
                        List<Object> list = new ArrayList<Object>(); 
                        list.add(o1); 
                        list.add(o2); 
                        map.put(name, list); 
                    } 
                } 
            } 
            return map; 
        }catch(Exception e){ 
            e.printStackTrace(); 
            return null; 
        } 
    }

    /**
     * 获取到对象中属性为null的属性名
     * @param source
     * @return 要拷贝的对象
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        //获取包装对象的PropertyDescriptors
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            //获取属性值为空的属性名
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 拷贝对象非空属性值
     * @param source 原对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
 
}