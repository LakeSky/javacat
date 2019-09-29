package com.kzh.sys.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * kzh
 * Date: 2014/05/15
 * Time: 上午11:05
 */
public class WebUtil {
    public static String getString(HttpServletRequest request, String name, String defaultValue) {
        String value = getRequestValue(request, name);
        if (value == null) {
            value = defaultValue;
        }
        if (value != null) {
            value = value.trim();
        }
        return value;
    }



    public static boolean getBoolean(HttpServletRequest request, String name, boolean defaultValue) {
        String value = getRequestValue(request, name);
        boolean r = false;
        if (value != null) {
            if ("true".equals(value) || "yes".equals(value)) {
                r = true;
            }
        } else {
            r = defaultValue;
        }
        return (r);
    }

    public static int getInt(HttpServletRequest request, String name, int defaultValue) {

        String value = getRequestValue(request, name);
        int r = 0;
        if (value != null) {
            try {
                r = Integer.parseInt(value);

            } catch (Exception e) {
                r = defaultValue;
            }
        } else {
            r = defaultValue;
        }
        return r;
    }

    public static Integer getInteger(HttpServletRequest request, String name, Integer defaultValue) {

        String value = getRequestValue(request, name);
        Integer r = null;
        if (value != null) {
            try {
                r = Integer.parseInt(value);

            } catch (Exception e) {
                r = defaultValue;
            }
        } else {
            r = defaultValue;
        }
        return r;
    }

    public static long getLong(HttpServletRequest request, String name, long defaultValue) {
        String value = getRequestValue(request, name);
        long r = 0;
        if (value != null) {
            try {
                r = Long.parseLong(value);
            } catch (Exception e) {
                r = defaultValue;
            }
        } else {
            r = defaultValue;
        }
        return r;
    }

    public static double getDouble(HttpServletRequest request, String name, double defaultValue) {
        String value = getRequestValue(request, name);
        double r = 0.0;
        if (value != null) {
            try {
                r = Double.parseDouble(value);
            } catch (Exception e) {
                r = defaultValue;
            }
        } else {
            r = defaultValue;
        }
        return r;
    }

    public static float getFloat(HttpServletRequest request, String name, float defaultValue) {
        String value = getRequestValue(request, name);
        float r = 0;
        if (value != null) {
            try {
                r = Float.parseFloat(value);
            } catch (Exception e) {
                r = defaultValue;
            }
        } else {
            r = defaultValue;
        }
        return r;
    }

    private static String getRequestValue(HttpServletRequest request, String name) {

        String value = null;
        try {
            value = request.getParameter(name);
        } catch (Exception e) {
            value = null;
        }
        return (value);
    }

    /**
     * 字符串编码
     *
     * @param sStr
     * @param sEnc
     * @return String
     */
    public final static String urlEncoder(String sStr, String sEnc) {
        String sReturnCode = "";
        try {
            sReturnCode = URLEncoder.encode(sStr, sEnc);
        } catch (UnsupportedEncodingException ex) {

        }
        return sReturnCode;
    }

    public final static String urlEncoder(String sStr) {
        return urlEncoder(sStr, "UTF-8");
    }

    public final static String urlDecoder(String sStr) {
        return urlDecoder(sStr, "UTF-8");
    }

    /**
     * 字符串解码
     *
     * @param sStr
     * @param sEnc
     * @return String
     */
    public final static String urlDecoder(String sStr, String sEnc) {
        String sReturnCode = "";
        try {
            sReturnCode = URLDecoder.decode(sStr, sEnc);
        } catch (UnsupportedEncodingException ex) {

        }
        return sReturnCode;
    }

    public final static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("HTTP_XIP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    //打印header
    public static void printHeader(HttpServletRequest request) {
        Enumeration names = request.getHeaderNames();
        StringBuilder sb = new StringBuilder("headerInfo---");
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            Enumeration headers = request.getHeaders(name);
            sb.append(name).append(":");
            while (headers.hasMoreElements()) {
                sb.append(headers.nextElement()).append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public static String getBasePath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    }

    //判断一个请求是否是Ajax请求
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return request != null && "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
    }
}
