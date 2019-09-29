package com.kzh.sys.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by gang on 2016/12/19.
 */
public class ListSortUtil<T> {
    /**
     * @param targetList 目标排序List
     * @param sortField  排序字段(实体类属性名)
     * @param sortMode   排序方式（asc or  desc）
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sortString(List<T> targetList, final String sortField, final String sortMode) {
        if (targetList == null) {
            return;
        }
        Collections.sort(targetList, new Comparator() {
            public int compare(Object obj1, Object obj2) {
                int retVal = 0;
                try {
                    //首字母转大写  
                    String newStr = sortField.substring(0, 1).toUpperCase() + sortField.replaceFirst("\\w", "");
                    String methodStr = "get" + newStr;
                    Class[] classes=new Class[0];
                    Object[] objs=new Object[0];
                    Method method1 = ((T) obj1).getClass().getMethod(methodStr, classes);
                    Method method2 = ((T) obj2).getClass().getMethod(methodStr, classes);
                    Object val1 = method1.invoke(((T) obj1), objs);
                    Object val2 = method2.invoke(((T) obj2), objs);
                    val1 = val1 == null ? "0" : val1;
                    val2 = val2 == null ? "0" : val2;


                    if ("desc".equals(sortMode)) {
                        retVal = val2.toString().compareTo(val1.toString()); // 倒序
                    } else {
                        retVal = val1.toString().compareTo(val2.toString()); // 正序  
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return retVal;
            }
        });
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sortInteger(List<T> targetList, final String sortField, final String sortMode) {
        if (targetList == null) {
            return;
        }
        Collections.sort(targetList, new Comparator() {
            public int compare(Object obj1, Object obj2) {
                int retVal = 0;
                try {
                    //首字母转大写  
                    String newStr = sortField.substring(0, 1).toUpperCase() + sortField.replaceFirst("\\w", "");
                    String methodStr = "get" + newStr;
                    
                    Class[] classes=new Class[0];
                    Object[] objs=new Object[0];
                    
                    Method method1 = ((T) obj1).getClass().getMethod(methodStr, classes);
                    Method method2 = ((T) obj2).getClass().getMethod(methodStr, classes);
                    Object val1 = method1.invoke(((T) obj1), objs);
                    Object val2 = method2.invoke(((T) obj2), objs);
                    Integer val1Int = val1 == null ? 0 : Integer.valueOf(val1.toString());
                    Integer val2Int = val2 == null ? 0 : Integer.valueOf(val2.toString());

                    if ("desc".equals(sortMode)) {
                        retVal = val2Int - val1Int; // 倒序
                    } else {
                        retVal = val1Int - val2Int; // 正序  
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return retVal;
            }
        });
    }

    public void sortDouble(List<T> targetList, final String sortField, final String sortMode) {
        if (targetList == null) {
            return;
        }
        Collections.sort(targetList, new Comparator() {
            public int compare(Object obj1, Object obj2) {
                Double retVal = 0d;
                try {
                    //首字母转大写  
                    String newStr = sortField.substring(0, 1).toUpperCase() + sortField.replaceFirst("\\w", "");
                    String methodStr = "get" + newStr;

                    Class[] classes=new Class[0];
                    Object[] objs=new Object[0];

                    Method method1 = ((T) obj1).getClass().getMethod(methodStr, classes);
                    Method method2 = ((T) obj2).getClass().getMethod(methodStr, classes);
                    Object val1 = method1.invoke(((T) obj1), objs);
                    Object val2 = method2.invoke(((T) obj2), objs);
                    Double val1Int = val1 == null ? 0 : Double.valueOf(val1.toString());
                    Double val2Int = val2 == null ? 0 : Double.valueOf(val2.toString());

                    if ("desc".equals(sortMode)) {
                        retVal = val2Int - val1Int; // 倒序
                    } else {
                        retVal = val1Int - val2Int; // 正序  
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return retVal.intValue();
            }
        });
    }

}  
