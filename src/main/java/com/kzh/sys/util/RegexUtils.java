package com.kzh.sys.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xinyu.qiu on 2015/8/10.
 */
public class RegexUtils {
    private static Pattern PATTERN_MOBILE = Pattern.compile("^(\\+86)?0?1[3|4|5|7|8]\\d{9}$");// 移动电话
    private static Pattern PATTERN_USER_NAME = Pattern.compile("^[0-9A-Za-z_]{6,20}$");//用户名只允许6-20位英文字母、数字或者下画线

    //校验一个日期是否为yyyy-MM-dd格式 兼容闰年
    private static Pattern PATTERN_DATE = Pattern.compile(
            "^(((((([02468][048])|([13579][26]))[0]{2})" +
                    "|([\\d]{2}(([2468][048])|([13579][26])))" +
                    "|([\\d]{2}([0][48])))" +
                    "[\\-]02[\\-]29)" +
                    "|(\\d{4}[\\-](" +
                    "(((0[13578])|(1[02]))[\\-]((0[1-9])|([1-2][0-9])|(3[01])))" +
                    "|(((0[469])|(11))[\\-]((0[1-9])|([1-2][0-9])|(30)))" +
                    "|(02[\\-]((0[1-9])|(1[0-9])|(2[0-8]))))))$"
    );


    //校验excel 2003 后缀
    private static Pattern PATTERN_EXCEL_2003_VERSION_FILENAME = Pattern.compile("^([\\d\\D]+\\.[xX][lL][sS])$");

    private static Pattern PATTERN_FILE_NAME_CHARACTER = Pattern.compile("^[a-zA-Z0-9_\\-\\u4e00-\\u9fa5]+$");


    public static boolean isMobile(String mobile) {
        return mobile != null && PATTERN_MOBILE.matcher(mobile.trim()).matches();
    }

    public static boolean isNotMobile(String mobile) {
        return !isMobile(mobile);
    }

    public static boolean isDateYearMonthDay(String dateStr) {
        return dateStr != null && PATTERN_DATE.matcher(dateStr.trim()).matches();
    }

    public static boolean isExcel2003VersionFileName(String fileName) {
        return fileName != null && PATTERN_EXCEL_2003_VERSION_FILENAME.matcher(fileName.trim()).matches();
    }


    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isNotEmail(String email) {
        return !isEmail(email);
    }


    public static boolean isUserName(String username) {
        return username != null && PATTERN_USER_NAME.matcher(username.trim()).matches();
    }

    public static String filterFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return SysUtil.valueOf(System.currentTimeMillis());
        } else {
            String fileNamePrefix = fileName;
            String fileNameSuffix = "";
            if (fileName.lastIndexOf(".") >= 0) {
                fileNamePrefix = fileName.substring(0, fileName.lastIndexOf("."));
                fileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
            }

            if (PATTERN_FILE_NAME_CHARACTER.matcher(fileNamePrefix).matches()) {
                return fileName;
            } else {
                return System.currentTimeMillis() + SysUtil.valueOf(fileNameSuffix);
            }
        }
    }

    public static boolean isIdCardNo(String id) {
        return "YES".equals(IDCard.IDCardValidate(id));
    }

    public static void main(String[] args) {
//        System.out.println(isUserName("123_"));
        System.out.println(filterFileName("ImageSelector_20170301_152930.JPEG"));
    }
}
