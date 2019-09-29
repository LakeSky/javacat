package com.kzh.sys.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLUtils {

    /**
     * 读取一个网页全部内容
     */
    public static String getHtmlByUrl(final String htmlurl, String contentType)
            throws Exception {
        if (contentType == null || contentType.isEmpty()) {
            contentType = "UTF-8";
        }
        URL urlmy = new URL(htmlurl);
        StringBuffer sb = new StringBuffer("");
        HttpURLConnection con = null;
        BufferedReader br = null;
        try {
            con = (HttpURLConnection) urlmy.openConnection();
            con.setFollowRedirects(true);
            con.setInstanceFollowRedirects(false);
            con.connect();
            if (con.getResponseCode() == 200) {
                br = new BufferedReader(new InputStreamReader(
                        con.getInputStream(), contentType));
                String s = "";
                while ((s = br.readLine()) != null) {
                    sb.append(s + " ");
                }
                /*char[] buffer = new char[1024];
				
				while(true){
					int num = br.read(buffer);
					if(num==-1){
						break;
					}
					sb.append(new String(buffer));
				}*/
            }
        } catch (final MalformedURLException me) {
            System.out.println("read url error!");
            return "";
        } catch (final IOException e) {
            return "";
        } catch (Exception e) {
            return "";
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return sb.toString();

    }

    /**
     * @param s
     * @return 获得链接
     */
    public static List<String> getLink(final String s) {
        String regex;
        List<String> list = new ArrayList<String>();
        try {
            regex = "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>";
            final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
            final Matcher ma = pa.matcher(s);
            while (ma.find()) {
                String link = ma.group(2);
                if (SysUtil.isEmpty(link)) {
                    link = ma.group(3);
                }
                if (SysUtil.isEmpty(link)) {
                    continue;
                } else if (link.contains("javascript") || link.contains("<") || link.contains(">")) {
                    continue;
                } else if (link.equals("#")) {
                    continue;
                }
                list.add(link);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param s
     * @return 获得网页标题
     */
    public static String getTitle(final String s) {
        String regex;
        String title = "";
        final List<String> list = new ArrayList<String>();
        try {
            regex = "<title>.*?</title>";
            final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
            final Matcher ma = pa.matcher(s);
            while (ma.find()) {
                list.add(ma.group());
            }
            for (int i = 0; i < list.size(); i++) {
                title = title + list.get(i);
            }
        } catch (Exception e) {
        }
        return outTag(title);
    }

    /**
     * @param s
     * @return 去掉标记
     */
    public static String outTag(final String s) {
        return s.replaceAll("<.*?>", "");
    }

    /**
     * 获取域名
     *
     * @param urlString
     * @return
     * @date 2013-4-9 上午9:32:34
     */
    public static String getHostName(String urlString) {
        try {
            int index = urlString.indexOf("://");
            if (index != -1) {
                urlString = urlString.substring(index + 3);
            }
            index = urlString.indexOf("/");
            if (index != -1) {
                urlString = urlString.substring(0, index);
            }
        } catch (Exception e) {
        }
        return urlString;
    }

    /**
     * @param source
     * @param beginmark
     * @param endmark
     * @return
     * @date 2013-4-10 下午4:00:46
     */
    public static String getTagByMark(final String source, String beginmark, String endmark, boolean isall) {
        if (SysUtil.isEmpty(beginmark) || SysUtil.isEmpty(endmark)) {
            return "";
        }
        String regex;
        String tag = "";
        final List<String> list = new ArrayList<String>();
        regex = beginmark.trim() + ".*?" + endmark.trim();
        final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
        final Matcher ma = pa.matcher(source);
        while (ma.find()) {
            if (!SysUtil.isEmpty(ma.group())) {
                list.add(ma.group());
                if (!isall) {
                    break;
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            tag = tag + list.get(i);
        }
        return outTag(tag);
    }


    public static void main(String[] args) {
        try {
            String source = HTMLUtils.getHtmlByUrl("http://www.lpdyz.com/article-87.html", "UTF-8");
            String startmark = "<div class=\" textcenter fontnormal fontcolorGray pubdate\">";
            String endmark = "<div class=\"GoodsSearchWrap\">";
            String str = getTagByMark(source, startmark, endmark, false);
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
