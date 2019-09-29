package com.kzh.sys.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 */
public class NumberUtil {
    private static final Logger LOG = LoggerFactory.getLogger(NumberUtil.class);
    public static final int DAY_TIMEMILLIS = 86400000;  //一天=86400000毫秒
    public static final int PRECISION = 2;              //precision  精度
    public static final double COMPARE_PRECISION = 0.00000001;//两个duoble值的比较结果

    public static final int MONEY_PRECISION = 2;

    public static final String SPLIT_REGEX = ",";
    public static final int AMOUNT_PRECISION = 2;


    /**
     * 都为Null时return true.
     *
     * @param value1
     * @param value2
     * @return
     */
    public static boolean isEqualIgnoreNull(Long value1, Long value2) {
        if (value1 == null && value2 == null) {
            return true;
        } else if (value1 == null || value2 == null) {
            return false;
        }
        return value1.equals(value2);
    }

    public static boolean isEqual(Double value1, Double value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        return value1.equals(value2);
    }

    public static boolean isEqual(Double value1, Double value2, Double precision) {
        if (precision == null) return isEqual(value1, value2);
        return isEqual(value1, value2) || Math.abs(value1 - value2) < precision;
    }

    public static boolean isEqual(Integer value1, Integer value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        return value1.equals(value2);
    }

    public static boolean isZero(Double val) {
        return val == null || Math.abs(val) <= 0;
    }

    public static boolean isZero(Double val, double precision) {
        return Math.abs(val) <= precision;
    }


    public static Long additionLong(Object... params) {
        if (params == null) return 0l;
        Long total = 0l;
        for (Object param : params) {
            if (param == null) continue;
            total += Long.valueOf(param.toString());

        }
        return total;
    }

    //30000000->30,000,000
    public static String formatDoubleWithComma(Double num, String defVal) {
        if (num == null) return defVal;
        try {
            return String.valueOf(NumberFormat.getInstance().format(NumberUtil.round(num, 2)));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return defVal;
    }

    /**
     * 如果传入参数无小位数，则格式化后亦无小数位。如果有，则返回最大2位小数
     *
     * @param num
     * @return 最大两位小数
     */
    public static String formatDouble(Double num) {
        if (num == null) return "";
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(num);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "N/A";
        }
    }


    public static long longValue(String numStr, long defVal) {
        if (SysUtil.isEmpty(numStr)) {
            return defVal;
        }
        try {
            return Long.parseLong(numStr);
        } catch (NumberFormatException nfe) {
            return defVal;
        }
    }

    public static Long longValue(String numStr) {
        return longValue(numStr, 0);
    }

    public static double objectToDouble(Object numStr) {
        if (numStr == null) {
            return 0d;
        }
        return doubleValue(numStr.toString(), 0);
    }

    public static double doubleValue(String numStr) {
        return doubleValue(numStr, 0);
    }

    public static double doubleValue(String numStr, double defVal) {
        if (SysUtil.isEmpty(numStr)) {
            return defVal;
        }
        try {
            return Double.parseDouble(numStr.toString());
        } catch (NumberFormatException nfe) {
            return defVal;
        }
    }

    public static double doubleValue(Double doubleValue) {
        return doubleValue == null ? 0d : doubleValue;
    }


    public static float floatValue(String numStr) {
        return floatValue(numStr, 0);
    }

    public static float floatValue(String numStr, float defVal) {
        if (SysUtil.isEmpty(numStr)) {
            return defVal;
        }
        try {
            return Float.parseFloat(numStr.toString());
        } catch (NumberFormatException nfe) {
            return defVal;
        }
    }

    public static int intValue(String numStr, int defVal) {
        if (SysUtil.isEmpty(numStr)) {
            return defVal;
        }
        try {
            return Integer.parseInt(numStr);
        } catch (NumberFormatException nfe) {
            return defVal;
        }
    }

    public static Integer intValue(String numStr) {
        return intValue(numStr, 0);
    }

    public static Integer intValue(Integer integer) {
        return integer == null ? 0 : integer.intValue();
    }

    public static Integer intValue(Long longVal) {
        return longVal == null ? 0 : longVal.intValue();
    }


    public static Integer objectToInt(Object o) {
        if (o == null) {
            return 0;
        }
        return intValue(o.toString(), 0);
    }

    public static Long objectToLong(Object o) {
        if (o == null) {
            return 0l;
        }
        return longValue(o.toString(), 0);
    }

    /**
     * 科学计数法转成十进制数字串
     *
     * @param value
     * @return
     */
    public static String scientificToDecimal(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        return decimalFormat.format(value);
    }

    /**
     * 判断字符串能否转为数值
     *
     * @param value
     * @return
     */
    public static boolean isNumber(String value) {
        if (SysUtil.isEmpty(value)) {
            return false;
        }
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串能否转为Long
     *
     * @param value
     * @return
     */
    public static boolean isLongNumber(String value) {
        if (SysUtil.isEmpty(value)) {
            return false;
        }
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isPositiveNumber(String value) {
        if (SysUtil.isEmpty(value)) {
            return false;
        }
        try {
            return Double.parseDouble(value) >= 0l;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * 四舍五入 保留小数
     *
     * @param value 需要保留小数的float值
     * @param num   需要保留的位数
     * @return
     */
    public static float round(Float value, int num) {
        if (value == null) {
            return 0f;
        }
        BigDecimal bigDecimal = new BigDecimal(value);
        value = bigDecimal.setScale(num, BigDecimal.ROUND_HALF_UP).floatValue();
        return value;
    }


    /**
     * 四舍五入 保留小数
     *
     * @param value 需要保留小数的double值
     * @param num   需要保留的位数
     * @return
     */
    public static double round(Double value, int num) {
        if (value == null) {
            return 0d;
        }
        BigDecimal bigDecimal = new BigDecimal(value.toString());
        value = bigDecimal.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }

    /**
     * 四舍五入 保留2位有效数字
     *
     * @param value 需要保留小数的double值
     * @return
     */
    public static String roundToString2(Double value) {
        if (value == null) {
            return "";
        }
        return new DecimalFormat("0.00").format(value);
    }

    public static int roundInt(Object value) {
        if (value == null || !isNumber(value.toString())) {
            return 0;
        }

        BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(value.toString()));
        return bigDecimal.setScale(NumberUtil.MONEY_PRECISION, BigDecimal.ROUND_HALF_UP).intValue();
    }


    /**
     * 四舍五入 保留小数
     *
     * @param value 需要保留小数的double值
     * @param num   需要保留的位数
     * @return
     */
    public static double toReserve(Double value, int num) {
        if (value == null) {
            value = 0.0;
        }
        BigDecimal bigDecimal = new BigDecimal(value);
        value = bigDecimal.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }

    public static long longValue(Long value) {
        if (value == null) {
            return 0;
        }
        return value.longValue();
    }

    //a==b  true
    public static boolean compareDouble(Double a, Double b) {
        if (null == a && null == b) {
            return true;
        }

        if (null != a && null != b) {
            double ab = Math.abs(a - b);
            if (ab <= 0.0001) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static boolean equalInteger(Integer a, Integer b) {
        if (null == a && null == b) {
            return true;
        }

        if (null != a && null != b) {
            return a.intValue() == b.intValue();
        } else {
            return false;
        }

    }

    public static boolean equalLong(Long a, Long b) {
        if (null == a && null == b) {
            return true;
        }

        if (null != a && null != b) {
            return a.longValue() == b.longValue();
        } else {
            return false;
        }

    }

    /**
     * a>b 返回 1，a = b 返回 0，a<b 返回 -1
     * null 值认为是最小的，
     *
     * @param a
     * @param b
     * @return
     */
    public static int compareInteger(Integer a, Integer b) {
        if (null != a && null != b) {
            return a >= b ? (a.intValue() == b ? 0 : 1) : -1;
        }
        if (a == null && b != null) {
            return -1;
        }
        if (a != null) {
            return 1;
        }
        return 0;
    }

    /**
     * 四舍五入 保留小数
     *
     * @param value 需要保留小数的double值 默认保留一位小数
     * @return
     */
    public static double toReserve(Double value) {
        if (value == null) {
            value = 0.0;
        }
        BigDecimal bigDecimal = new BigDecimal(value);
        value = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     *
     * @param code
     * @return
     */
    public static String autoGenericCode(String code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0     
        // num 代表长度为4     
        // d 代表参数为正数型 
        result = String.format("%0" + num + "d", Integer.parseInt(code) + 1);

        return result;
    }

    public static boolean isInteger(String value) {
        if (SysUtil.isEmpty(value)) {
            return false;
        }
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
