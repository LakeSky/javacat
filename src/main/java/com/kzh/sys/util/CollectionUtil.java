package com.kzh.sys.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class CollectionUtil extends CollectionUtils {

  /**
   * 从一个string类型的list中移出没有实际内容（包括只含空格内容）的元素
   * 返回一个新的list
   *
   * @param list
   * @return
   */
  public static List<String> filterBlankElements(List<String> list) {
    List<String> result = new ArrayList<String>();
    if (list == null || list.isEmpty()) {
      return result;
    }
    for (String element : list) {
      if (StringUtils.isBlank(element) || StringUtils.isBlank(element.trim())) {
        continue;
      }
      result.add(element);
    }
    return result;
  }

  /**
   * 返回集合类的第一个元素, 如果集合为空返回Null
   *
   * @param list 集合类
   */
  public static <T> T getFirst(Collection<T> list) {
    if (list == null || list.isEmpty())
      return null;
    return list.iterator().next();
  }

    public static int getSize(Collection list) {
        if (list == null)
            return 0;
        return list.size();
    }
  /**
   * 将List中的Null元素去除.
   *
   * @param list 集合类
   */
  public static <T> void removeNullElements(Collection<T> list) {
    List<T> nullList = new ArrayList<T>();
    for (T item : list) {
      if (item == null) {
        nullList.add(item);
      }
    }
    list.removeAll(nullList);
  }

  public static <T> String collectionToCommaString(Collection<T> list) {
    if (CollectionUtils.isEmpty(list))
      return "";
    StringBuilder result = new StringBuilder();
    for (T item : list) {
      if (item != null) {
        if (result.length() > 0) {
          result.append(",");
        }
        result.append(item.toString());
      }
    }
    return result.toString();
  }

  public static <T> Set<T> listToSet(Collection<T> list) {
    Set<T> set = new HashSet<T>();
    if (list == null || list.isEmpty())
      return null;
    for (T t : list) {
      set.add(t);
    }
    return set;
  }

  /**
   * 把数组对象去空，去重，生成set
   */
  public static <T> Set<T> arrayToSet(T[] array) {
    Set<T> set = new HashSet<T>();
    if (array == null || array.length ==0){
      return set;
    }
    for (T t : array) {
      if(t != null){
        set.add(t);
      }
    }
    return set;
  }

  /**
   * 把数组对象去空，生成List
   */
  public static <T> List<T> arrayToList(T[] array) {
    List<T> list = new ArrayList<>();
    if (array == null || array.length == 0) {
      return list;
    }
    for (T t : array) {
      if (t != null) {
        list.add(t);
      }
    }
    return list;
  }



  public static <T> boolean hasUniqueElement(Collection<T> list){
    if(null!=list&&list.size()==1){
      return true;
    }
    return false;
  }

  public static <T> T uniqueResult(Collection<T> list){
    if(hasUniqueElement(list)){
      return list.iterator().next();
    }
    return null;
  }

  public static <T> boolean isNotEmpty(Collection<T> list) {
    return (list != null&&!list.isEmpty());
  }


  public static <T> Collection<T> add(Collection<T> list1,Collection<T> list2){
    if(list1==null||list2==null) return list1;
    for(T elem:list2){
      list1.add(elem);
    }
    return list1;
  }

  public static Set<String> splitStrToSet(String str,String splitRegex){
    if(StringUtils.isNotEmpty(str)){
      if(splitRegex == null){
        splitRegex = ",";
      }
      return arrayToSet(str.split(splitRegex));
    }
    return  new HashSet<>();
  }
}
