package com.kzh.sys.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by xinyu.qiu on 2015/7/9.
 */
public class OrderValidateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(OrderValidateUtil.class);

    /**
     * 如果两个值不相同则校验不过返回false,相同则过,校验通过返回true
     * toCheckVal =0,checkVal = 1 return false
     * toCheckVal =0,checkVal = null return false
     */
    public static <T> boolean validateNeedEqual(T toCheckVal, T checkVal, StringBuilder failMsg, String msgStr){
        if(checkVal == null){
            if(StringUtils.isNotEmpty(SysUtil.valueOf(toCheckVal))){
                failMsg.append(msgStr);
                return false;
            }
        }else {
            if (!checkVal.equals(toCheckVal)) {
                failMsg.append(msgStr);
                return false;
            }
        }
        return true;
    }

    /**
     *  如果两个值相同则校验不过返回false,校验通过返回true
     *  toCheckVal =1,checkVal = 1 return false
     *  toCheckVal =null/"",checkVal = null return false
     */
    public static <T> boolean validateNeedNotEqual(T toCheckVal, T checkVal, StringBuilder failMsg, String msgStr){
        if(checkVal == null) {
            if(StringUtils.isEmpty(SysUtil.valueOf(toCheckVal))){
                failMsg.append(msgStr);
                return false;
            }
        }else {
            if (checkVal.equals(toCheckVal)) {
                failMsg.append(msgStr);
                return false;
            }
        }
        return true;
    }

    //如果校验值的集合包含被校验值则校验通过返回true,校验不过返回false
    public static <T> boolean validateNeedContains(T toCheckVal, Collection<T> checkVal, StringBuilder failMsg, String msgStr){
        if(CollectionUtil.isEmpty(checkVal)){
            if(StringUtils.isNotEmpty(SysUtil.valueOf(toCheckVal))){
                failMsg.append(msgStr);
                return false;
            }
        }else {
            if (!checkVal.contains(toCheckVal)) {
                failMsg.append(msgStr);
                return false;
            }
        }
        return true;
    }


}
