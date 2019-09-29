package com.kzh.sys.util;

/**
 * User: kzh
 * Date: 13-10-24
 * Time: 上午10:15
 */
public class QRandom {

    //生成指定位数的整数随机数
    public static String generateRandom(int len) {
        String num = String.valueOf(Math.random());
        while (num.length() < len + 2) {
            num = String.valueOf(Math.random());
        }
        return num.substring(2, len + 2);
    }
}
